package com.onemsg.infosharing.config;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    public static Properties getProperties(String filePath) {

        Properties prop = new Properties();
        try {

            var inStream = ConfigReader.class.getClassLoader().getResourceAsStream(filePath);

            if (inStream != null) {
                prop.load(inStream);
            } else {
                throw new FileNotFoundException("配置文件 " + filePath + " 没找到");
            }
            inStream.close();
        } catch (Exception e) {
            logger.error("加载配置文件出现错误", e);
        }
        return prop;
    }

    private ConfigReader(){
        
    }
}