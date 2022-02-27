package com.onemsg.shorturl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.onemsg.shorturl.common.Convertor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import io.vertx.redis.client.ResponseType;
import io.vertx.redis.client.impl.types.BulkType;

/**
 * 短域名存储服务
 */
public class StoreVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(StoreVerticle.class);

    public static final String DEFAULT_REDIS_URL = "redis://localhost:6379/0";

    private static final String SRCURL = "srcUrl";
    private static final String CREATED = "created";

    static final Cache<String, String> CACHE = Caffeine.newBuilder()
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    private RedisAPI redis;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        String redisUrl = config().getJsonObject("redis", new JsonObject()).getString("url", DEFAULT_REDIS_URL);

        try {
            redis = RedisAPI.api(Redis.createClient(vertx, redisUrl));
        } catch (Exception e) {
            startPromise.fail(e);
            return;
        }

        EventBus bus = vertx.eventBus();

        bus.<String>consumer(Constants.SHORTURL_CREATE_ADDRESS).handler(this::handleCreate);
        bus.<String>consumer(Constants.SHORTURL_GET_ADDRESS).handler(this::handleGet);
        bus.<JsonObject>consumer(Constants.SHORTURL_LIST_ADDRESS).handler(this::handleList);

        redis.ping(Collections.emptyList())
            .onFailure(startPromise::fail)
            .onSuccess(res -> startPromise.complete());
    }

    private void handleCreate(Message<String> message) {
        String srcUrl = message.body();
        String key = Convertor.to62String(srcUrl);
        LocalDateTime now = LocalDateTime.now();

        List<String> command = List.of(key, SRCURL, srcUrl, CREATED, now.toString());
        redis.hmset(command)
                .map(Response::toString)
                .onFailure(t -> {
                    logger.warn("Redis hmset {} failed,", command, t);
                    message.fail(500, t.getMessage());
                }).onSuccess(status -> {
                    if (Objects.equals(status, "OK")) {
                        message.reply(
                                new JsonObject()
                                        .put("shortUrl", key)
                                        .put(SRCURL, srcUrl)
                                        .put(CREATED, now.toString()));
                        CACHE.put(key, srcUrl);
                        logger.info("Create shorurl succees, {} -> {}", srcUrl, key);
                    } else {
                        logger.warn("Create shorurl failed({}), {} -> {}", status, srcUrl, key);
                        message.fail(400, status);
                    }
                });
    }

    private void handleGet(Message<String> message) {
        String shortUrl = message.body();
        String srcUrl = CACHE.getIfPresent(shortUrl);
        if (srcUrl != null) {
            message.reply(srcUrl);
        } else {
            redis.hget(shortUrl, SRCURL)
                    .onFailure(t -> {
                        logger.warn("Redis hget {} failed", shortUrl, t);
                        message.fail(500, t.getMessage());
                    })
                    .onSuccess(res -> {
                        if (res.type() == ResponseType.ERROR) {
                            message.reply(null);
                        } else {
                            message.reply(res.toString());
                        }
                    });
        }
    }

    private void handleList(Message<JsonObject> message) {
        redis.keys("*")
            .onFailure(t -> {
                logger.warn("Redis keys * failed", t);
                message.fail(500, t.getMessage());
            }).onSuccess(res -> {
                if(res.type() == ResponseType.MULTI){
                    final int size = res.size();
                    AtomicInteger hasGetCount = new AtomicInteger();
                    JsonArray array = new JsonArray();
                    for(Response row : res ){
                        redis.hgetall(row.toString())
                            .onSuccess(data -> {
                                JsonObject json = new JsonObject()
                                    .put("shortUrl", row.toString())
                                    .put("srcUrl", data.get("srcUrl").toString() )
                                    .put("created", data.get("created").toString() );
                                
                                array.add(json);
                            }).onComplete(ar -> {
                                if (hasGetCount.incrementAndGet() >= size) {
                                    message.reply(array);
                                }
                            });
                    }
                }else{
                    logger.warn("Redis keys * failed, response is {}", res);
                    message.fail(500, res.toString());
                }
            });
    }
}