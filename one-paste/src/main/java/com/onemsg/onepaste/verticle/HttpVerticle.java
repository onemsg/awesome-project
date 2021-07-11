package com.onemsg.onepaste.verticle;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.onemsg.onepaste.config.Loggers;
import com.onemsg.onepaste.config.MongoDBConfig;
import com.onemsg.onepaste.model.Paster;
import com.onemsg.onepaste.repository.impl.PasterMongoDBRepository;
import com.onemsg.onepaste.service.PasterService;
import com.onemsg.onepaste.service.impl.PasterServiceImpl;

import org.slf4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class HttpVerticle extends AbstractVerticle{
    
    private static final String HOST = "localhost";
    private static final int PORT = 8086;
    
    private static final Logger logger = Loggers.getHttpLogger();

    private final PasterService service = new PasterServiceImpl(
        new PasterMongoDBRepository(MongoDBConfig.getPasterCollection()));

    private static final String AUTH_CODE = "2020";

    @Override
    public void start() throws Exception {
        
        Router router = Router.router(vertx);
        
        router.route().handler(LoggerHandler.create(LoggerFormat.SHORT));
        router.route().handler(BodyHandler.create());
        router.get("/*").handler(StaticHandler.create().setDefaultContentEncoding("utf-8"));

        router.get("/").handler(ctx -> ctx.reroute("/index.html"));
        router.get("/p/*").handler(ctx -> ctx.reroute("/p/show.html"));

        router.get("/api/paster/:id").blockingHandler(this::handleGetPaster);
        router.post("/api/paster").blockingHandler(this::handlePostPasster);
        router.delete("/api/paster/expired").blockingHandler( ctx -> {
            String code = ctx.request().getParam("code");
            if(Objects.equals(code, AUTH_CODE)){
                long count = deleteExpiredTask();
                sendOK(ctx, new JsonObject().put("content", "清理过期 paste 成功").put("count", count));
            }else{
                sendFail(ctx, new JsonObject().put("content", "权限认证失败"));
            }
        });

        startHTTPServer(router);

        vertx.setPeriodic(TimeUnit.HOURS.toMillis(3), id -> deleteExpiredTask()) ;
        logger.info("定时任务 [清理过期paste/3hours] 已启动");
    }

    private void startHTTPServer(Router router){
        vertx.createHttpServer().requestHandler(router).listen(PORT, HOST, result -> {
            if (result.succeeded()) {
                logger.info("WEB 服务器启动成功！port: " + result.result().actualPort());
            } else {
                logger.error("WEB 服务器启动失败！case: " + result.cause().getMessage());
            }
        });
    }

    void handleGetPaster(RoutingContext ctx){
        String id = ctx.pathParam("id");
        Paster paster = service.find(id);
        sendOK(ctx, toJO(paster));
    }

    void handlePostPasster(RoutingContext ctx){

        Paster paster = ctx.getBodyAsJson().mapTo(Paster.class);
        String id = service.insert(paster);
        logger.info("新 Paster 插入成功: {}", paster.getId().toHexString());
        sendOK(ctx, id);
    }

    static JsonObject toJO(Paster paster){
        Objects.requireNonNull(paster);
        return JsonObject.mapFrom(paster).put("id", paster.getId().toString());
    }

    static void sendOK(RoutingContext ctx, Object data){
        JsonObject body = new JsonObject()
            .put("code", 200)
            .put("msg", "请求成功")
            .put("data", ((data instanceof JsonObject) || (data instanceof JsonArray) ) ? 
                data : Objects.toString(data));
        ctx.response().putHeader("content-type", "application/json").end(body.toString());
    }

    static void sendFail(RoutingContext ctx, Object data){
        JsonObject body = new JsonObject()
            .put("code", 400)
            .put("msg", "请求失败")
            .put("data", ((data instanceof JsonObject) || (data instanceof JsonArray) ) ? 
                data : Objects.toString(data));
        ctx.response().putHeader("content-type", "application/json").end(body.toString());
    }

    long deleteExpiredTask(){
        long count = service.deleteExpired();
        logger.info("清除了过期 paster，共 {} 条", count);
        return count;
    }
}
