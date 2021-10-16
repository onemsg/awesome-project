package com.onemsg.sunday.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSender {
    
    static HttpClient client = HttpClient.newHttpClient();

    static String webhook = "http://localhost:3001/api/sendMsg";
    static URI uri = URI.create(webhook);

    private MessageSender() {}

    public static void sendAsync(String message, String phoneNumber){
        client.sendAsync( buildRequest(message, phoneNumber), BodyHandlers.ofString())
            .whenCompleteAsync( (res, t) -> {
                if(t != null){
                    log.warn("验证码短信 [to {}] 发送出现异常", phoneNumber, t);
                    return;
                }

                if(200 == res.statusCode()){
                    log.info("验证码短信 [to {}] 已发送", phoneNumber);
                }else {
                    log.warn("验证码短信 [to {}] 发送失败 | {} - {}", res.statusCode(), res.body());
                }
            });
            

    }

    private static HttpRequest buildRequest(String message, String phoneNumber){

        String body = toJson(message, phoneNumber);
        return HttpRequest.newBuilder()
            .uri(uri)
            .POST(BodyPublishers.ofString(body))
            .header("Authorization", "Basic faeFaeGEerfteEFmO458")
            .header("Content-Type", "application/json")
            .build();
    }

    private static String toJson(String message, String phoneNumber){
        return String.format("{\"Message\": \"%s\", \"PhoneNumber\": \"%s\"}", 
            message, phoneNumber);
    }

}
