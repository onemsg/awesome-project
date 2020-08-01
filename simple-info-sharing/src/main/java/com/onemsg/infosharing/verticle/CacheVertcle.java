package com.onemsg.infosharing.verticle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.onemsg.infosharing.config.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class CacheVertcle extends AbstractVerticle {
    
    private final Logger logger = LoggerFactory.getLogger(CacheVertcle.class);
    public final static String CACHE_FILE = Keys.CACHE_FILE;
    private final ConcurrentHashMap<String,String> cache = new ConcurrentHashMap<>();


    @Override
    public void start() throws Exception {
        load();

        EventBus bus = vertx.eventBus();
        
        // 返回 cache 值 
        bus.<String>consumer(Keys.GET_CACHE_VALUE, msg -> {
            String value = cache.getOrDefault(msg.body(), msg.body());
            msg.reply(value);
        });

        // 返回 cache 所有
        bus.<Object>consumer(Keys.GET_CACHE_ALL, msg -> {
            JsonObject json = new JsonObject(Map.copyOf(cache));
            msg.reply(json);
        });

        // 更新 cache
        bus.<JsonObject>consumer(Keys.SET_CACHE_VALUE, msg ->{
            var json = msg.body();
            cache.put(json.getString("name"), json.getString("filename"));
            msg.reply(Boolean.TRUE);
            flush();
        });

        // 删除 cache
        bus.<String>consumer(Keys.DEL_CACHE_VALUE, msg -> {
            String key = msg.body();
            cache.remove(key);
            flush();
        });
        
        logger.info("Cache 服务启动成功");
    }

    // 载入缓存文件
    private void load(){

        vertx.fileSystem().readFile(CACHE_FILE, result -> {
            if (result.succeeded()) {
                JsonObject json = new JsonObject(result.result());
                json.iterator().forEachRemaining(entry -> {
                    cache.put(entry.getKey(), entry.getValue().toString());
                });
                logger.info("缓存载入成功！\n{}", cache);
            }else{
                logger.info("缓存载入失败！");
            }
        });
    }

    /** 刷新至本地，异步执行 */
    private void flush(){

        JsonObject json = new JsonObject(Map.copyOf(cache));        
        vertx.fileSystem().writeFile(CACHE_FILE, json.toBuffer(), result -> {
            if(result.succeeded()){
                logger.info("缓存已刷新至本地: {}", json);
            }else{
                logger.info("缓存刷新至本地失败");
            }
        });
    }


    @Override
    public void stop() throws Exception {
        flush();
        logger.info("Cache 服务已正常关闭");
    }
}