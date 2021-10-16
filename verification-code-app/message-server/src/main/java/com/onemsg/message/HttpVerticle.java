package com.onemsg.message;

import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpVerticle extends AbstractVerticle{

    static final int PORT = 3001;
    static final String HOST = "127.0.0.1";

    static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        AppRouteHandler routeHandler = new AppRouteHandler();

        AppAuthProvider authProvider = new AppAuthProvider();
        AuthenticationHandler authHandler = new AppAuthenticationHandler(authProvider, "app-token");
        router.route("/api/*")
            .order(-10)
            .handler(authHandler)
            .failureHandler(routeHandler::handleError);

        router.post("/api/sendMsg")
            .consumes("application/json")
            .handler(routeHandler::handleMessagePost);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(PORT, HOST)
            .onSuccess(http -> {
                log.info("HttpServer-{} is runnig on localhost:{}", counter.incrementAndGet(), http.actualPort());
                startPromise.complete();
            }).onFailure(t -> {
                log.error("HttpServer running failed");
                startPromise.fail(t);
            });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        log.info("HttpVerticle is stopped");
    }
}
