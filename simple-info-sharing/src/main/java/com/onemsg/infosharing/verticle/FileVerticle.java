package com.onemsg.infosharing.verticle;

import java.io.File;

import com.onemsg.infosharing.config.Keys;
import com.onemsg.infosharing.util.FileManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class FileVerticle extends AbstractVerticle {
    
    private Logger logger = LoggerFactory.getLogger(FileVerticle.class);
    private FileManagement fm = null;

    @Override
    public void start() throws Exception {

        fm = FileManagement.create(Keys.CONTENT_FILE_PATH, true);

        EventBus bus = vertx.eventBus();

        // 处理上传文件
        bus.<JsonArray>consumer(Keys.UPLOAD_FILE_HANDLE, msg ->{

        
            logger.info(" 开始处理文件");
            JsonArray uploads = msg.body();
            JsonObject json = new JsonObject();

            for(int i=0,end=uploads.size(); i < end; i++){
                JsonObject file = uploads.getJsonObject(i);
                String fileName = file.getString("fileName");
                String uploadedFileName = file.getString("uploadedFileName");
                // 如果是 zip 文件则解压
                if (fileName.endsWith(".zip")){
                    fm.unzip(uploadedFileName);
                }else{
                    fm.save(uploadedFileName, fileName);
                }
                json.put(fileName, "OK");
            }
            msg.reply(json);
        });

        // 返回所有文件名称
        bus.consumer(Keys.GET_FILES_LIST, msg -> {

            JsonArray jsonArray = new  JsonArray();
            fm.list().forEach( path ->{
                File file = path.toFile();
                jsonArray.add( new JsonObject()
                                .put("filename", file.getName())
                                .put("size", file.getTotalSpace()) );
            });

            msg.reply(jsonArray);
        });

        logger.info("File 服务启动成功！");

    }
}