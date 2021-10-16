package com.onemsg.message;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorModel {
    int code;
    String message;
    String detail;

    public static ErrorModel of(int code, String message){
        return new ErrorModel(code, message, null);
    }


    public JsonObject toJson(){
        return new JsonObject()
            .put("code", code)
            .put("message", message)
            .put("detail", detail);
    }

}
