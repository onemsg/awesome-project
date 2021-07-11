package com.onemsg.onepaste;

import com.onemsg.onepaste.verticle.HttpVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Launcher
 */
public class Launcher {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(HttpVerticle.class, new DeploymentOptions().setInstances(2));
    }
}