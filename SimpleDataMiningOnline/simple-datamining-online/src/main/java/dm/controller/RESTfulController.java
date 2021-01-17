package dm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dm.model.DataDescription;
import dm.model.DataLocation;
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

    private DataSetRepository dataSetRepository;

    @Autowired
    public RESTfulController(DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
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
            return DataUtils.toExplore(dataLocation);
        } else return null;
    }

    @RequestMapping(value = "/model/evalinfo", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelInfo getModelEvalInfo( ModelInfo modelInfo,
            @CookieValue("dataName") String dataName, HttpSession session) throws Exception {
        if (modelInfo.isEmpty()){
            System.out.println("modelInfo 为空");
            modelInfo = (ModelInfo) session.getAttribute("modelInfo");
            System.out.println(modelInfo);
        }
        if( modelInfo.isEmpty()) return null;
        
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