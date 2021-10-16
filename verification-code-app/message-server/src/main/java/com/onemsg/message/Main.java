package com.onemsg.message;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main{
    
    public static void main(String[] args) {
        
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(HttpVerticle.class, new DeploymentOptions().setInstances(2))
            .onSuccess( id -> log.info("HttpVerticles 部署成功"))
            .onFailure( t -> log.error("HttpVerticles 部署失败", t) );      
    }
}
