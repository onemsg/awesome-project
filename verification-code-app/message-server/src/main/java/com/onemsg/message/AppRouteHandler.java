package com.onemsg.message;

import com.onemsg.message.device.MobilePhone;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppRouteHandler {
    
    private final MobilePhone businessPhone = MockDevices.businessPhone();

    /**
     * 处理消息发送
     * @param rtx
     */
    public void handleMessagePost(RoutingContext rtx){

        if(log.isDebugEnabled()){
            log.debug("{} {} From {}", rtx.request().method(), rtx.request().path(), rtx.request().remoteAddress());
        }

        JsonObject body = rtx.getBodyAsJson();
        String phoneNumber = body.getString("PhoneNumber");
        String message = body.getString("Message");
        businessPhone.sendAsync(phoneNumber, message);
        rtx.end();
    }


    public void handleError(RoutingContext rtx){
        
        ErrorModel errorModel = null;

        Throwable t = rtx.failure();

        log.warn("HTTP {} {} 处理异常", rtx.request().method(), rtx.request().path(), t);

        if( t instanceof HttpException ) {
            errorModel = ErrorModel.of( ((HttpException) t).getStatusCode(), 
                ((HttpException) t).getPayload());
        }

        errorModel = ErrorModel.of(rtx.statusCode(), rtx.failure().getMessage());
        
        rtx.response().setStatusCode(errorModel.code)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(errorModel.toJson().toBuffer());

    }
}
