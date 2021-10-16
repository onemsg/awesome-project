package com.onemsg.message.device;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.onemsg.message.device.EventQueue.AcceptEvent;
import com.onemsg.message.device.NetWork5G.Packet;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟手机设备
 * 
 * @author onemsg
 * @since 2021-10
 */
@Slf4j
public class MobilePhone {

    private static final NetWork5G NETWORK = new NetWork5G();
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public final String phoneNumber;

    public final Deque<Message> inBoxes = new ConcurrentLinkedDeque<>();

    private MobilePhone(String phoneNumber) {
        Objects.requireNonNull(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void send(String phoneNumber, String message) {
        Packet packet = new Packet(message, this.phoneNumber, phoneNumber);
        NETWORK.transport(packet);
    }

    public Future<Void> sendAsync(String phoneNumber, String message){
       return  executor.submit(() -> send(phoneNumber, message), null);
    }

    void accept(String phoneNumber, String message) {
        Message newMessage = new Message(message, phoneNumber, LocalDateTime.now());
        inBoxes.add(newMessage);
        notice(newMessage);

        AcceptEvent event = new AcceptEvent(this.phoneNumber, newMessage);
        EventQueue.publish(event);
    }

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);

    private synchronized void notice(Message message) {
        log.info("Receive message from: {} {}", message.sender, message.datetime.format(DTF));
        System.out.println("= = = = = = = = = = = = = = = =");
        System.out.printf("PhoneNumber: %s %n", phoneNumber);
        System.out.printf("From: %s - %s %n", message.sender, message.datetime.format(DTF));
        System.out.printf("Message: %s %n", message.content);
        System.out.println("= = = = = = = = = = = = = = = =");
    }

    /**
     * 创建一部新手机，并入网
     * 
     * @param phoneNumber 手机号
     */
    public static MobilePhone create(String phoneNumber) {
        if (NETWORK.phones.containsKey(phoneNumber)) {
            return NETWORK.phones.get(phoneNumber);
        }

        var newPhone = new MobilePhone(phoneNumber);
        NETWORK.register(newPhone);
        return newPhone;
    }

    public static void register(Consumer<AcceptEvent> consumer) {
        EventQueue.register(consumer);
    }

    @ToString
    @AllArgsConstructor
    public static class Message {

        public final String content;

        public final String sender;

        public final LocalDateTime datetime;

    }
}
