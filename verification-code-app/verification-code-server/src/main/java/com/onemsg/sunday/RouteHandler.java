package com.onemsg.sunday;

import java.util.Objects;

import com.onemsg.sunday.service.MessageSender;
import com.onemsg.sunday.service.VerificationCode;
import com.onemsg.sunday.service.VerificationCode.VerifiedState;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.EncodeException;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteHandler {

    public static final long DEFAULT_CODE_DURATION = 10_000;

    void handleCodeAskFor(RoutingContext rtx) {

        String user;
        String service;
        try {
            var body = rtx.getBodyAsJson();
            user = body.getString("user");
            service = body.getString("service");
            Objects.requireNonNull(user);
            Objects.requireNonNull(service);
        } catch (Exception e) {
            rtx.fail(400, new HttpBadParamException(e.getMessage()));
            return;
        }

        var code = VerificationCode.create(user, service, DEFAULT_CODE_DURATION);

        sendCode(code.data, user); // 调用短信发送服务（异步）

        rtx.end();
    }

    void handleCodeValidate(RoutingContext rtx) {
        String user;
        String service;
        int code = 0;
        try {
            var body = rtx.getBodyAsJson();
            user = body.getString("user");
            service = body.getString("service");
            code = body.getInteger("code");
            Objects.requireNonNull(user);
            Objects.requireNonNull(service);
        } catch (Exception e) {
            log.warn("json body 获取出错", e);
            rtx.fail(400, new HttpBadParamException(e.getMessage()));
            return;
        }

        VerifiedState state = VerificationCode.verify(user, service, code);

        if (VerifiedState.SUCCESS == state) {
            rtx.end();
        } else if (VerifiedState.FAIL == state) {
            var errorModel = ErrorModel.of(400, "验证失败", "当前验证码不正确: " + code);
            respondJson(rtx, 400, errorModel);
        } else if (VerifiedState.EXPIRED == state) {
            var errorModel = ErrorModel.of(410, "验证码已过期", null);
            respondJson(rtx, 410, errorModel);
        } else if (VerifiedState.NOT_FOUND == state) {
            var errorModel = ErrorModel.of(404, "无可用验证码", null);
            respondJson(rtx, 404, errorModel);
        }

    }

    void handleException(RoutingContext rtx) {

        Throwable t = rtx.failure();

        log.warn("HTTP {} {} 处理发生异常", rtx.request().method(), rtx.request().uri(), t);

        rtx.fail(500);

        if (t == null) {
            rtx.response().setStatusCode(rtx.statusCode()).end();
        }
        ErrorModel errorModel = null;
        var res = rtx.response();
        if (t instanceof HttpBadParamException) {
            var e = (HttpBadParamException) t;
            errorModel = ErrorModel.of(400, "HTTP 请求参数错误", e.getMessage());
            res.setStatusCode(400);
        } else {
            errorModel = ErrorModel.of(rtx.statusCode(), "HTTP 请求处理发生错误", rtx.failure().getMessage());
            res.setStatusCode(rtx.statusCode());
        }
        res.putHeader(HttpHeaders.CONTENT_TYPE, "application/json") 
            .end(Json.encodeToBuffer(errorModel));
    }

    private void respondJson(RoutingContext rtx, int statusCode, Object json) {
        try {
            rtx.response()
                .setStatusCode(statusCode)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(Json.encodeToBuffer(json));
        } catch (EncodeException e) {
            rtx.fail(e);
        }
    }

    private static final String MESSAGE_FORMAT = "【SUNDAY】你的验证码是 %s，请勿告诉他人";

    private static void sendCode(int code, String user) {
        MessageSender.sendAsync(String.format(MESSAGE_FORMAT, code), user);
    }
}
