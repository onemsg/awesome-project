package thevideo.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
// import io.vertx.core.json.JsonObject;

/**
 * RESTServer
 * 基于vertx框架的REST web服务类，设计了所需的路由和处理器
 */
public class RESTServer extends AbstractVerticle {

    private DataService dataService;    //依赖 DataService对象

    public RESTServer(){ }

    public RESTServer(DataService dataService){
        this.dataService = dataService;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        //获得用户看过的商品的路由和处理器设置
        router.get("/items").handler(this::handleGetItemsOfUser); 
        //获得基于用户的推荐系统的推荐信息的路由和处理器设置
        router.get("/recommendation/items").handler(this::handleGetRecommendedItems);
        //获得基于商品的推荐系统的推荐信息的路由和处理器设置
        router.get("/recommendation2/items").handler(this::handleGetRecommended2Items);
        // router.get("/products").handler(this::handleGetProducts);
        
        router.errorHandler(500, rc -> {        //错误500时的处理方法，打印出错信息（参考自github issue)
            System.err.println("=== Handling failure ===\n");
            Throwable failure = rc.failure();
            if (failure != null) {
              failure.printStackTrace();
            }
        });
        //创建http服务器，设置指定路由和监听端口
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    //得到用户看过的商品
    private void handleGetItemsOfUser(RoutingContext routingContext){
        String userID = routingContext.request().getParam("userID");    //得到请求参数
        HttpServerResponse response = routingContext.response();
        if (userID == null){    //url路径出错，返回出错信息
            sendError(400, response);   
        }else{
            String items = dataService.getItemsOfUser(userID);  //获取信息
            if( items == null){ //获取数据出错，返回出错信息
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(items);  //写入结果到response
            }
        }
    }

    //得到基于用户的推荐
    private void handleGetRecommendedItems(RoutingContext routingContext){
        String userID = routingContext.request().getParam("userID");
        String howMany = routingContext.request().getParam("howMany");
        howMany = howMany != null ? howMany : "10"; //如果howMany参数缺失，默认为10
        HttpServerResponse response = routingContext.response();
        if (userID == null){
            sendError(400, response);
        }else{
            String items = dataService.getItemsRecommenderBasedUser(Integer.parseInt(userID), 
                            Integer.parseInt(howMany));
            if( items == null){
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(items);
            }
        }
    }

    //得到基于商品的推荐
    private void handleGetRecommended2Items(RoutingContext routingContext){
        String userID = routingContext.request().getParam("userID");    //得到请求参数
        String itemID = routingContext.request().getParam("itemID");
        String howMany = routingContext.request().getParam("howMany");
        howMany = howMany != null ? howMany : "10"; //如果howMany参数缺失，默认为10
        HttpServerResponse response = routingContext.response();
        if(userID == null){
            sendError(400, response);
        } else{
            if (itemID == null){
                String items = dataService.getItemsRecommenderBasedItem( Integer.parseInt(userID), 
                                Integer.parseInt(howMany));
                if (items == null){
                    sendError(404, response);
                } else{
                    response.putHeader("content-type", "application/json").end(items);
                }
            } else if (itemID != null){
                String items = dataService.getItemsRecommenderBasedUserAndItem(Integer.parseInt(userID), 
                                Integer.parseInt(itemID), Integer.parseInt(howMany));
                if (items == null){
                    sendError(404, response);
                } else{
                    response.putHeader("content-type", "application/json").end(items);
                }
            }
        }
    }

    // private void handleGetProducts(RoutingContext routingContext) {
    //     String id = routingContext.request().getParam("id");
    //     HttpServerResponse response = routingContext.response();
    //     if (id == null) {
    //         sendError(400, response);
    //     } else {
    //         JsonObject product = new JsonObject().put("id", id).put("name", "iPhoneXR").put("price", 4599);
    //         if (product == null) {
    //             sendError(404, response);
    //         } else {
    //             response.putHeader("content-type", "application/json").end(product.encodePrettily());
    //         }
    //     }
    // }

    //出错时，发送错误码和信息给客户端
    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end("Sorry, the resource was not found. Check to see if the request path is correct");
    }
}