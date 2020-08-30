package com.onemsg.shorturl.verticle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.onemsg.shorturl.common.Convertor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.Response;
import io.vertx.redis.client.ResponseType;

/**
 * RedisVerticle
 */
public class RedisVerticle extends AbstractVerticle {

    private static final int MAX_RECONNECT_RETRIES = 16;
    private RedisConnection client;
    private RedisAPI redis;
    Logger logger = LoggerFactory.getLogger(RedisVerticle.class);

    public static final String SHORTURL_CREATE_ADDRESS = "redis.shorturl.create";
    public static final String SHORTURL_GET_ADDRESS = "redis.shorturl.get";
    public static final String SHORTURL_LIST_ADDRESS = "redis.shorturl.list";
    private static final String REDIS_CONNECTION_STRING = "redis://localhost:6379/1";
    private static final String SRC_URL = "srcUrl";

    @Override
    public void start() throws Exception {

        createRedisClient(onCreate -> {
            if (onCreate.succeeded()) {
                logger.info("Redis 连接成功！");
                redis = RedisAPI.api(client);
            } else if (onCreate.failed()) {
                logger.error("Redis 连接失败！");
            }
        });

        initRepository();

    }

    private void createRedisClient(Handler<AsyncResult<RedisConnection>> handler) {

        AtomicInteger retry = new AtomicInteger(1);
        Redis.createClient(vertx, REDIS_CONNECTION_STRING).connect(onConnect -> {
            if (onConnect.succeeded()) {
                client = onConnect.result();
                // make sure the client is reconnected on error
                client.exceptionHandler(e ->  attemptReconnect(retry.getAndIncrement()));
            }
            // allow further processing
            handler.handle(onConnect);
        });
    }

    private void attemptReconnect(int retry) {

        logger.info("第" + retry + "次尝试重连 Redis");

        if (retry > MAX_RECONNECT_RETRIES) {
            // we should stop now, as there's nothing we can do.
        } else {
            // retry with backoff up to 10240 ms
            long backoff = (long) (Math.pow(2, Math.min(retry, 10)) * 10);

            vertx.setTimer(backoff, timer -> createRedisClient(onReconnect -> {
                if (onReconnect.failed()) {
                    attemptReconnect(retry + 1);
                }
            }));
        }
    }

    private void initRepository() {

        EventBus bus = vertx.eventBus();

        MessageConsumer<JsonObject> consumner = bus.consumer(SHORTURL_CREATE_ADDRESS);
        consumner.handler(msg -> {
            JsonObject json = msg.body();

            String srcUrl = json.getString(SRC_URL);
            String shortUrl = Convertor.to62String(srcUrl);

            LocalDateTime date = LocalDateTime.now();

            redis.hmset(List.of(shortUrl, SRC_URL, srcUrl, "date", date.toString()), res -> {
                if (res.succeeded()) {
                    String result = res.result().toString();
                    if (result.equals("OK")) {
                        logger.info("短网址创建完成 " + srcUrl + " -> " + shortUrl);
                        msg.reply(new JsonObject().put("shortUrl", shortUrl).put(SRC_URL, srcUrl).put("date",
                                date.toString()));
                    } else {
                        logger.error("短网址创建失败 " + srcUrl + " -> " + shortUrl);
                        msg.reply(new JsonObject());
                    }
                } else {
                    logger.error("短网址创建失败 " + srcUrl + " -> " + shortUrl);
                    msg.reply(new JsonObject());
                }
            });
        });

        bus.<String>consumer(SHORTURL_GET_ADDRESS, msg -> {

            String shortUrl = msg.body();
            redis.hget(shortUrl, SRC_URL, res -> {
                if (res.succeeded()) {
                    if (res.result().type() == ResponseType.ERROR) {
                        msg.reply(null);
                    } else {
                        String srcUrl = res.result().toString();
                        msg.reply(srcUrl);
                    }
                } else {
                    msg.reply(null);
                }
            });
        });

        bus.<String>consumer(SHORTURL_LIST_ADDRESS, msg -> {

            JsonArray array = new JsonArray();
            redis.keys("*", res -> {
                if (res.succeeded() && res.result().type() == ResponseType.MULTI) {

                    AtomicInteger hasGetCount = new AtomicInteger();
                    final int size = res.result().size();

                    for (Response r : res.result()) {

                        redis.hgetall(r.toString(), res2 -> {
                            Response data = res2.result();
                            JsonObject json = new JsonObject();
                            json.put("shortUrl", r.toString()).put(data.get(0).toString(), data.get(1).toString())
                                    .put(data.get(2).toString(), data.get(3).toString());

                            array.add(json);

                            if (hasGetCount.incrementAndGet() == size) {
                                msg.reply(array);
                                logger.info("list 请求 redis 已回复");
                            }
                        });
                    }

                }
            });

        });
    }

}