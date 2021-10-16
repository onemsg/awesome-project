package com.onemsg.message;

import java.util.Objects;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;

public class AppAuthProvider implements AuthenticationProvider{

    static final String TOKEN_1 = "faeFaeGEerfteEFmO458";

    @Override
    public void authenticate(JsonObject credentials, Handler<AsyncResult<User>> resultHandler) {
        
        final String token = credentials.getString("token");
        if(checkToken(token)){
            User user = User.fromToken(token);
            resultHandler.handle(Future.succeededFuture(user));
        }else {
            resultHandler.handle(Future.failedFuture("Token 无效"));
        }
    }
    
    boolean checkToken(String token){
        return Objects.equals(TOKEN_1, token);
    }
    
}
