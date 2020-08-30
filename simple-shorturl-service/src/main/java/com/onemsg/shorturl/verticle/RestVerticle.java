package com.onemsg.shorturl.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RestVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(RestVerticle.class);
    java.util.logging.Logger log = null;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/management").handler(routingContext -> routingContext.reroute("/static/management.html"));
        router.get("/favicon.ico").handler(routingContext -> routingContext.reroute("/static/favicon.ico"));

        router.get("/:shortUrlKey").handler(this::handleRedirect);
        router.get("/api/list").handler(this::handleListShortUrl);
        router.post("/api/create").handler(this::handleCreateShortUrl);

        router.get("/static/*").handler(StaticHandler.create());

        // 方便 nginx 方向代理而添加的域名
        // Router mainRouter = Router.router(vertx).mountSubRouter("/s", router);

        vertx.createHttpServer().requestHandler(router).listen(8081, "localhost", result -> {
            if (result.succeeded()) {
                logger.info("WEB 服务器启动成功！port: " + result.result().actualPort());
            } else {
                logger.error("WEB 服务器启动失败！case: " + result.cause().getMessage());
            }
        });

    }

    private void handleCreateShortUrl(RoutingContext context) {

        JsonObject json = context.getBodyAsJson();
        EventBus bus = vertx.eventBus();
        bus.<JsonObject>request(RedisVerticle.SHORTURL_CREATE_ADDRESS, json, reply -> {
            if( reply.succeeded() ){
                JsonObject data = reply.result().body();
                context.response().putHeader("content-type", "application/json").end(data.toString());
            }
        });
    }

    private void handleRedirect(RoutingContext context){

        String shortUrlKey = context.pathParam("shortUrlKey");
        EventBus bus = vertx.eventBus();
        bus.<String>request(RedisVerticle.SHORTURL_GET_ADDRESS, shortUrlKey, reply -> {
            if( reply.succeeded() ){
                String srcUrl = reply.result().body();
                if(srcUrl != null){
                    context.response().putHeader("Location", srcUrl).setStatusCode(302).end();
                }else{
                    sendError(404, context.response());
                }

            }
        });
    }

    private void handleListShortUrl(RoutingContext context) {

        EventBus bus = vertx.eventBus();
        bus.<JsonArray>request(RedisVerticle.SHORTURL_LIST_ADDRESS, "list", reply -> {
            if( reply.succeeded()){
                JsonArray data = reply.result().body();
                context.response().putHeader("content-type", "application/json").end(data.toString());
            }
        });
    }

    private void sendError(int statusCode, HttpServerResponse response ){
        response.setStatusCode(statusCode).end();
    }
}