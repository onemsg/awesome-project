package thevideo;

import io.vertx.core.Vertx;
// import thevideo.common.DataInit;
// import thevideo.common.DataInit;
import thevideo.web.DataService;
import thevideo.web.RESTServer;

/**
 * Application
 */
public class Application {

    public static void main(String[] args) throws Exception {
        
        // DataInit.start();   //数据初始化到redis
        DataService dataService = new DataService();    //创建service对象
        RESTServer restServer = new RESTServer(dataService);    //创建rest web服务对象
        Vertx vertx = Vertx.vertx();    //创建vertx对象
        vertx.deployVerticle(restServer);   //启动

    }
}