package com.onemsg.shorturl;

import com.onemsg.shorturl.verticle.RedisVerticle;
import com.onemsg.shorturl.verticle.RestVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;


public class Server {

    public static void main(String[] args) {
        
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(RestVerticle.class, new DeploymentOptions().setInstances(2));
        vertx.deployVerticle(RedisVerticle.class, new DeploymentOptions().setWorker(true).setInstances(4));
    }
}