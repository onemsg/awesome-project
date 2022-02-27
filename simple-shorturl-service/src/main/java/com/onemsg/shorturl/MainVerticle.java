package com.onemsg.shorturl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;


public class MainVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

    static {
        System.setProperty("vertx-config-path", "config.json");
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(ar -> {
            if(ar.failed()){
                log.warn("Get config failed", ar.cause());
                startPromise.fail(ar.cause());
            }else{
                JsonObject config = ar.result();
                if(config == null){
                    startPromise.fail("Get config is empty");
                    return;
                }

                Future<String> f1 = vertx.deployVerticle(
                    StoreVerticle.class, 
                    new DeploymentOptions()
                        .setWorker(true)
                        .setInstances(3)
                        .setWorkerPoolName("vertx-store-thread")
                        .setConfig(config)
                );
                Future<String> f2 = vertx.deployVerticle(
                    WebVerticle.class,
                    new DeploymentOptions()
                            .setInstances(3)
                            .setWorkerPoolName("vertx-web-thread")
                            .setConfig(config));

                CompositeFuture.all(f1, f2)
                    .onFailure( t -> {
                        startPromise.fail(t);
                        vertx.close();
                    })
                    .onSuccess( f -> {
                        startPromise.complete();
                    } );
            }
        });
    }
}