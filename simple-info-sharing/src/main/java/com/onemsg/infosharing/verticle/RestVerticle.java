package com.onemsg.infosharing.verticle;

import java.util.Set;

import com.onemsg.infosharing.config.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RestVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create(Keys.UPLOAD_FILE_PATH).setHandleFileUploads(true));

        // URL 映射
        router.route("/*").handler(this::logging);
       
        router.post("/api/file/upload").handler(this::handleUploadFile);
        router.get("/api/file/list").handler(this::handleContentList);
        router.get("/api/urlmap/list").handler(this::urlMapList);
        router.delete("/api/urlmap/name/:name").handler(this::handleUrlMapDel);
        router.post("/api/urlmap/put").handler(this::handleUrlMapPut);
        
        router.route("/").handler(this::index);
        router.get("/ct/:name").handler(this::handleContent);
        router.get("/manage").handler(this::managementPage);
        
        router.get("/content/*").handler(StaticHandler.create(Keys.CONTENT_FILE_PATH).setDirectoryListing(true));
        router.get("/static/*").handler(StaticHandler.create("webroot/static"));
        router.get("/root/*").handler(StaticHandler.create());


        // 启动HTTP服务器
        HttpServer server = vertx.createHttpServer();

        server.exceptionHandler(exception -> {
            logger.info("web 服务器错误信息: {}", exception.getMessage());
        });

        server.requestHandler(router).listen(8080, result -> {
            if(result.succeeded()){
                logger.info("WEB 服务器启动成功！port: {}",result.result().actualPort());
            }else{
                logger.info("WEB 服务器启动失败！case: {}", result.cause().getMessage());
            }
        });



    }

    /** 打印每次请求 */
    private void logging(RoutingContext context){
        
        HttpServerRequest req = context.request();
        String[] info = new String[]{req.method().toString(), req.path(), req.host().toString() };
        logger.info("请求: {} {} | from host: {}", info[0], info[1], info[2]);
        // vertx.eventBus().send(Keys.REQUEST_TO_LOG, info);
        context.next();
    }

    /** 主页 */
    private void index(RoutingContext context){
        context.reroute(HttpMethod.GET, "/root/index.html");
    }

    /** 处理上传文件 */
    private void handleUploadFile(RoutingContext context) {

        Set<FileUpload> uploads = context.fileUploads();
        
        for( FileUpload file : uploads){
            logger.info("name: {}\nfileName: {}\nuploadedFileName: {}", file.name(), file.fileName(),
                file.uploadedFileName());
        }
        
        JsonArray array = new JsonArray();
        for( FileUpload file : uploads){
            array.add(new JsonObject()
                        .put("fileName", file.fileName())
                        .put("uploadedFileName", file.uploadedFileName())
                    );
        }
        
        vertx.eventBus().<JsonObject>request(Keys.UPLOAD_FILE_HANDLE, array, reply -> {

            logger.info("收到 回复");

            if(reply.succeeded()){
                JsonObject body = reply.result().body();
                JsonObject info = new JsonObject();
                info.put("code", 200)
                    .put("message", "文件上传成功")
                    .put("data", body);
                
                responseJson(context.response(), info);
                logger.info("文件处理成功: {}", body);
            }else{
                JsonObject info = new JsonObject();
                info.put("code", 500).put("message", "文件上传失败").putNull("data");
                responseJson(context.response(), info);
                logger.error("文件处理失败: {}", reply.cause().getMessage());
            }
        });

    }

    /** 返回所有文件 */
    private void handleContentList(RoutingContext context){

        vertx.eventBus().<JsonArray>request(Keys.GET_FILES_LIST, null, reply -> {
            if(reply.succeeded()){

                JsonArray array = reply.result().body();
                array.forEach( element ->{
                    JsonObject json = (JsonObject) element;
                    json.put("url", "/content/" + json.getString("filename"));
                });

                JsonObject info = new JsonObject();
                info.put("code", 200)
                    .put("message", "文件列表获取成功")
                    .put("data", array);
                responseJson(context.response(), info);
                logger.info("文件列表获取成功");
            }else{
                JsonObject info = new JsonObject();
                info.put("code", 500)
                .put("message", "文件列表获取失败")
                .put("data", reply.result().body());
                responseJson(context.response(), info);
                logger.info("文件列表获取失败");
            }
        });
    }

    /** 返回文件内容 */
    private void handleContent(RoutingContext context){
        String name = context.pathParam("name");

        vertx.eventBus().<String>request(Keys.GET_CACHE_VALUE, name, reply ->{
            if(reply.succeeded()){
                String fileName = reply.result().body();
                context.reroute(HttpMethod.GET, "/content/" + fileName);
            }else{
                context.response().end();
                logger.error("获取缓存出错: {}", reply.cause().getMessage());
            }
        });

    }

    /** 管理界面 */
    private void managementPage(RoutingContext context){
        context.reroute(HttpMethod.GET, "/root/manage.html");
    }

    /** 返回 url map list */
    private void urlMapList(RoutingContext context){

        vertx.eventBus().<JsonObject>request(Keys.GET_CACHE_ALL, null, reply -> {
            if (reply.succeeded()) {
                JsonObject body = reply.result().body();

                JsonArray array = new JsonArray();

                body.forEach( entry -> {
                    array.add( new JsonObject()
                                .put("name", entry.getKey())
                                .put("filename", entry.getValue().toString())
                                .put("url", "/ct/" + entry.getKey())
                            );
                });
                

                JsonObject info = new JsonObject().put("code", 200).put("message", "成功");
                info.put("data", array);
                responseJson(context.response(), info);
                logger.info("get cache all 成功");
            } else {
                JsonObject json = new JsonObject().put("code", 500).put("message", "失败").putNull("data");
                responseJson(context.response(), json);

                logger.error("get cache all 失败 {}", reply.cause().getMessage());
            }
        });
    }


    /** 删除 urlmap cache */
    private void handleUrlMapDel(RoutingContext context) {
        String key = context.pathParam("name");
        vertx.eventBus().send(Keys.DEL_CACHE_VALUE, key);
        JsonObject info = new JsonObject();
        info.put("code", 200)
            .put("message", "成功");
        responseJson(context.response(), info);

    }

    /** 添加 urlmap cache */
    private void handleUrlMapPut(RoutingContext context) {
        JsonObject json = context.getBodyAsJson();

        logger.info("请求体: {}", json);

        if(!json.containsKey("name") || !json.containsKey("filename")){
            responseJson(context.response(), 
                        new JsonObject()
                        .put("code", 500)
                        .put("message", "缺少 name 或 filename 参数"));
        }

        vertx.eventBus().<Boolean>request(Keys.SET_CACHE_VALUE, json, reply -> {
            if(reply.succeeded()){
                Boolean succeeded = reply.result().body();
                if(succeeded){
                    JsonObject info = new JsonObject();
                    info.put("code", 200).put("message", "成功");
                    responseJson(context.response(), info);
                }else{

                    JsonObject info = new JsonObject();
                    info.put("code", 500).put("message", "失败");
                    responseJson(context.response(), info);
                    logger.error("添加 urlmap cache 获取失败");
                }
            }else{
                logger.error("添加 urlmap cache 获取失败 {}", reply.cause().getMessage());
                JsonObject info = new JsonObject();
                info.put("code", 500).put("message", "失败");
                responseJson(context.response(), info);
            }
        });
    }



    private void responseJson(HttpServerResponse res, JsonObject json){
        res.putHeader("content-type", "application/json").end(json.toString());
    }
}