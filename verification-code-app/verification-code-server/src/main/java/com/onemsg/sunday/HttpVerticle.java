package com.onemsg.sunday;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpVerticle extends AbstractVerticle{

    static final int PORT = 3000;
    static final String HOST = "127.0.0.1";

    static final AtomicInteger counter = new AtomicInteger();

    public static final String CODE_PATH = "/api/verification-code";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        var routeHandler = new RouteHandler();

        router.post("/api/verification-code/ask-for")
            .handler( routeHandler::handleCodeAskFor);

        router.post("/api/verification-code/validate")
            .handler(routeHandler::handleCodeValidate);

        router.get("/api/blocking")
            .blockingHandler(ctx -> {

                int blockingTime = ThreadLocalRandom.current().nextInt(500, 1000);
                try {
                    TimeUnit.MILLISECONDS.sleep(blockingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("request blocking time {} ms", blockingTime);
                ctx.json(String.format("blocking time %s ms", blockingTime));
            });

        router.route("/api/*").failureHandler(routeHandler::handleException);
            
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(PORT, HOST)
            .onSuccess(http -> {
                log.info("HttpServer-{} started on http://localhost:{}", counter.incrementAndGet(), http.actualPort());
                startPromise.complete();
            }).onFailure(t -> {
                log.error("HttpServer start failed");
                startPromise.fail(t);
            });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        log.info("HttpVerticle is stopped");
    }
}
