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

public class RestVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/:shortUrlKey").handler(this::handleRedirect);
        router.get("/api/list").handler(this::handleListShortUrl);
        router.post("/api/create").handler(this::handleCreateShortUrl);

        vertx.createHttpServer().requestHandler(router).listen(8080);
        logger.info("REST web 服务器启动成功！监听端口 8080");
    }

    private void handleCreateShortUrl(RoutingContext context) {

        JsonObject json = context.getBodyAsJson();
        EventBus bus = vertx.eventBus();
        bus.<JsonObject>request(RedisVerticle.SHORTURL_CREATE_ADDRESS, json, reply -> {
            if( reply.succeeded() ){
                JsonObject data = reply.result().body();
                data.put("shortUrl", context.request().host() + "/" + data.getString("shortUrl") );
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
        response.setStatusCode(404).end();
    }
}