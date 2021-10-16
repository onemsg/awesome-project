package com.onemsg.message;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.ext.web.handler.impl.HTTPAuthorizationHandler;

public class AppAuthenticationHandler extends HTTPAuthorizationHandler<AuthenticationProvider> {
    
    static final Type TOKEN_TYPE = Type.BASIC;

    public AppAuthenticationHandler(AuthenticationProvider authProvider, String realm) {
        super(authProvider, TOKEN_TYPE, realm);
    }

    @Override
    public void authenticate(RoutingContext context, Handler<AsyncResult<User>> handler) {
        parseAuthorization(context, parseAuthorization -> {

            if (parseAuthorization.failed()) {
                handler.handle(Future.failedFuture(parseAuthorization.cause()));
                return;
            }

            var credentials = new JsonObject().put("token", parseAuthorization.result());
            authProvider.authenticate(credentials, authn -> {
                if (authn.failed()) {
                    handler.handle(Future.failedFuture(new HttpException(401, authn.cause())));
                } else {
                    handler.handle(authn);
                }
            });
        });    
        
    }
    
}
