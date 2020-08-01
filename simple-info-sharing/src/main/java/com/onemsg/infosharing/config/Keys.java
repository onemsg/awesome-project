package com.onemsg.infosharing.config;

import java.util.Properties;

/** 配置关键字 */
public class Keys {

    // Vert.X 主题路径地址参数

    public static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    public final static String REQUEST_TO_LOG = "request_to_log";

    public final static String FILE_TO_UNZIP = "file_to_unzip";

    public final static String GET_FILES_LIST = "get_files_list";

    public final static String GET_CACHE_VALUE = "get_cache_value";

    public final static String GET_CACHE_ALL = "get_cache_all";

    public final static String SET_CACHE_VALUE = "set_cache_value";

    public final static String DEL_CACHE_VALUE = "delete_cache_value";

    public final static String UPLOAD_FILE_HANDLE = "upload_file_handle";

    // 文件路径配置参数
    public final static String CONTENT_FILE_PATH;

    public final static String UPLOAD_FILE_PATH;

    public final static String CACHE_FILE;


    static {
        Properties prop = ConfigReader.getProperties(DEFAULT_PROPERTIES_FILE);
        CONTENT_FILE_PATH = prop.getProperty("content.file.path");
        UPLOAD_FILE_PATH = prop.getProperty("upload.file.path");
        CACHE_FILE = prop.getProperty("cache.file.path");        
    }

    private Keys(){

    }
}