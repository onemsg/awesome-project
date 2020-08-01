package com.onemsg.infosharing.verticle;

import com.onemsg.infosharing.config.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class LogVerticle extends AbstractVerticle {

    private Logger logger = LoggerFactory.getLogger(LogVerticle.class);

    @Override
    public void start() throws Exception {

        EventBus bus = vertx.eventBus();

        // 处理 request 日志
        bus.<Object[]>consumer(Keys.REQUEST_TO_LOG, msg -> {
            var req = msg.body();
            logger.info("请求: {} {} | from host: {}", req[0], req[1], req[2]);
            // logger.info("请求: {}", req);
        });

        logger.info("Log 服务启动成功！");
    }
}