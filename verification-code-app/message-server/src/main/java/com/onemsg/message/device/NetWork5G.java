package com.onemsg.message.device;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟 5G 网络 
 * 
 * @author onemsg
 * @since 2021-10
 */
@Slf4j
class NetWork5G {

    Map<String, MobilePhone> phones = new ConcurrentHashMap<>();

    public void register(MobilePhone phone) {
        phones.put(phone.phoneNumber, phone);
        log.info("phone {} 已入网", phone.phoneNumber);
    }

    void transport(Packet packet) {
        var toPhone = phones.get(packet.to);
        if(toPhone != null){
            toPhone.accept(packet.from, packet.content);
        }
    }



    @ToString
    @AllArgsConstructor
    static class Packet {
        final String content;
        final String from;
        final String to;
    }

}