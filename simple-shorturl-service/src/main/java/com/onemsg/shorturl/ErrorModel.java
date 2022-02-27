package com.onemsg.shorturl;

import io.vertx.core.json.JsonObject;

public class ErrorModel {
    
    private ErrorModel() {}


    public static JsonObject create(int status, String message) {
        return create(status, message, null);
    }

    public static JsonObject create(int status, String message, String detail){
        return new JsonObject()
            .put("status", status)
            .put("message", message)
            .put("detail", detail);
    }


}
