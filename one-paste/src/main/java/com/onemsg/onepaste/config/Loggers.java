package com.onemsg.onepaste.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Loggers {
    
    private static final Logger httpLogger = LoggerFactory.getLogger("HTTPServer");


    /**
     * 返回 HTTP服务器 日志记录器
     */
    public static Logger getHttpLogger(){
        return httpLogger;
    }
}
