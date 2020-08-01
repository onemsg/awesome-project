package com.onemsg.infosharing;

import com.onemsg.infosharing.verticle.CacheVertcle;
import com.onemsg.infosharing.verticle.FileVerticle;
import com.onemsg.infosharing.verticle.LogVerticle;
import com.onemsg.infosharing.verticle.RestVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Server {

    public static void main(final String[] args) {

        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(LogVerticle.class, new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(FileVerticle.class, new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(CacheVertcle.class, new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(RestVerticle.class, new DeploymentOptions());
    }
}