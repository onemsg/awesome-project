package com.onemsg.shorturl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Web 服务
 * 
 * @author onemsg
 * @since 2021
 */
public class WebVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebVerticle.class);

    
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/management").handler(routingContext -> routingContext.reroute("/static/management.html"));
        router.get("/favicon.ico").handler(routingContext -> routingContext.reroute("/static/favicon.ico"));

        router.get("/s/:shortUrlKey").handler(this::handleRedirect);
        router.get("/api/shorturls").handler(this::handleListShortUrl);
        router.post("/api/shorturls").handler(this::handleCreateShortUrl);
        router.get("/static/*").handler(StaticHandler.create());

        router.route("/api/*").failureHandler(ctx -> {
            JsonObject error = ErrorModel.create(
                ctx.statusCode(), 
                ctx.failure() == null ? "" : ctx.failure().getMessage() );
            ctx.response().setStatusCode(ctx.statusCode());
            ctx.json(error);
        });

        JsonObject config = config().getJsonObject("server", new JsonObject());
        String host = config.getString("host", "localhost");
        int port = config.getInteger("port", 8080);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port, host)
            .onComplete(ar -> {
                if(ar.succeeded()){
                    logger.info("HttpServer running on {}:{}", host, port);
                    startPromise.complete();
                }else{
                    logger.warn("HttpServer start failed, {}", ar.cause().getMessage());
                    startPromise.fail(ar.cause());
                }
            });

    }


    /**
     * 处理重定向
     * 
     * @param context 必须包含路径变量 <code>shortUrlKey</code>
     */
    private void handleRedirect(RoutingContext context){
        String shortUrlKey = context.pathParam("shortUrlKey");
        vertx.eventBus()
            .<String>request(Constants.SHORTURL_GET_ADDRESS, shortUrlKey)
            .onComplete( ar -> {
                if(ar.succeeded() || ar.result().body() != null){
                    context.redirect(ar.result().body());
                }else {
                    context.response().setStatusCode(404).end();
                }
            });
    }

    /**
     * 创建新 shorturl
     * @param context
     */
    private void handleCreateShortUrl(RoutingContext context) {
        JsonObject data = context.getBodyAsJson();
        if(data == null || !data.containsKey("srcUrl") ){
            context.fail(400, new HttpException(400, "请求json缺少 [srcUrl] 字段"));
            return;
        }

        vertx.eventBus()
            .<JsonObject>request(Constants.SHORTURL_CREATE_ADDRESS, data.getString("srcUrl"))
            .onComplete(ar -> {
                if (ar.succeeded()){
                    context.json(ar.result().body());
                } else {
                    context.fail(500, ar.cause());
                }
            });
    }

    /**
     * 查询 shorturl
     * @param context
     */
    @SuppressWarnings("unchecked")
    private void handleListShortUrl(RoutingContext context) {
        vertx.eventBus()
            .<JsonArray>request(Constants.SHORTURL_LIST_ADDRESS, "list")
            .onFailure(t -> context.fail(500, t))
            .onSuccess(msg -> {
                JsonArray data = msg.body();
                data.getList().sort( (Object o1, Object o2) -> {
                    if( o1 instanceof JsonObject data1 && o2 instanceof JsonObject data2){
                        return data1.getString("created", "")
                            .compareTo(data2.getString("created", ""));
                    }else{
                        return 0;
                    }
                } );

                context.json(data);
            });
    }

}