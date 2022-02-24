package dm.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dm.entity.DataLocation;
import dm.model.DataDescription;
import dm.model.ModelInfo;
import dm.repository.DataSetRepository;
import dm.weka.DataUtils;
import dm.weka.ModelUtils;

/**
 * RESTfulController
 */
@RestController
@RequestMapping(value = "/api")
public class RESTfulController {

    private static final String TEMP_FLODER = "dataset/tmp/";

    private DataSetRepository dataSetRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(RESTfulController.class);

    @Autowired
    public RESTfulController(DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
    }

    @PostMapping(path = "/data", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE } )
    public ResponseEntity<Object> postDataFile(@RequestParam("dataset") MultipartFile uploadFile, HttpServletResponse response) {

        String fileName = "T_" +  uploadFile.getOriginalFilename();
        String classpath = TEMP_FLODER + fileName;
        try {
            
            String floder = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + TEMP_FLODER ).getAbsolutePath();
            
            Path path = Path.of(floder, fileName);
            Files.deleteIfExists(path);

            path.toFile().createNewFile();
            Files.write(path, uploadFile.getBytes());

            logger.info("上传文件 {} 保存成功，size={}, classpath={}, fspath={}", fileName, uploadFile.getSize(), classpath, path);
            
            String datasetName = fileName.split("[.]")[0];
            logger.info("dataSetRepository.save({}, {})", datasetName, classpath);
            dataSetRepository.save(datasetName, classpath);
            
            Cookie cookie = new Cookie("dataName", datasetName);
            response.addCookie(cookie);
            
        } catch (IOException e) {
            logger.warn("处理上传文件 {} 发生了异常", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("CODE", "SERVER_IO_ERROR", "message", e.toString() ));
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .location(URI.create("/data-exploration"))
            .build();

    }


    @GetMapping(value = "/data/names")
    public List<String> findAllData() {
        return dataSetRepository.findAllName();
    }

    @GetMapping(value = { "/data/desc/{name}", "/data/desc/", "/data/desc" })
    public DataDescription getDataDescription(@PathVariable(value = "name", required=false) String name, 
            @CookieValue(value="dataName", required=false) String dataName) throws Exception {

        DataLocation dataLocation = null;
        System.out.println(name);
        System.out.println(dataName);
        if (name != null) {
            dataLocation = dataSetRepository.findByName(name);
        } else if (dataName != null) {
            dataLocation = dataSetRepository.findByName(dataName);
        } else return null;
        if (dataLocation != null){

            logger.info("datqaLocation = {}", dataLocation);

            return DataUtils.toExplore(dataLocation);
        } else return null;
    }

    @RequestMapping(value = "/model/evalinfo", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelInfo getModelEvalInfo( ModelInfo modelInfo,
            @CookieValue("dataName") String dataName, HttpSession session) throws Exception {
        if (modelInfo == null || modelInfo.isEmpty()){
            System.out.println("modelInfo 为空");
            modelInfo = (ModelInfo) session.getAttribute("modelInfo");
            System.out.println(modelInfo);
        }
        if(modelInfo == null ||  modelInfo.isEmpty()) return null;
        
        DataLocation dataLocation = dataSetRepository.findByName(dataName);       
        return ModelUtils.toEvaluate(modelInfo, dataLocation);
    }

    // 查看已选择数据状态
    @GetMapping(value = "/chosedata")
    public Map<String,Object> choosedDataset(@CookieValue(value="dataName", required=false) String dataName){
        if( dataName != null){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("chose", true);
            map.put("dataName", dataName);
            return map;
        }else{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("chose", false);
            return map;
        }
    }
}