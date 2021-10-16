package com.onemsg.sunday;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * Main
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        VertxOptions options = new VertxOptions()
            .setWorkerPoolSize(5);
        Vertx vertx = Vertx.vertx(options);

        vertx.deployVerticle(HttpVerticle.class, new DeploymentOptions().setInstances(2))
            .onSuccess(id -> log.info("HttpVerticles 部署成功"))
            .onFailure(t -> log.error("HttpVerticles 部署失败", t));
    }
}