package com.onemsg.message.device;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import com.onemsg.message.device.MobilePhone.Message;

import io.vertx.core.impl.ConcurrentHashSet;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventQueue {
    
    private static BlockingQueue<AcceptEvent> queue = new ArrayBlockingQueue<>(1024);

    private static final Thread worker;

    private static Set<Consumer<AcceptEvent>> consumers = new ConcurrentHashSet<>();

    static {
        worker = new Thread(() -> {
            AcceptEvent event = null;
            while (true) {
                if(Thread.currentThread().isInterrupted()){
                    return;
                }

                try {
                    event = queue.take();
                } catch (InterruptedException e) {
                    log.warn("Event Queue is Interrupted", e);
                    return;
                }
                for (Consumer<AcceptEvent> consumer : consumers) {
                    consumer.accept(event);
                }
            }
        }, "EventQueue-Worker-Thread");
        
        worker.setDaemon(true);
        worker.start();
    }

    private EventQueue() {}

    static void publish(AcceptEvent event){
        queue.add(event);
    }

    public static void register(Consumer<AcceptEvent> consumer){
        consumers.add(consumer);
    }

    public static void stop(){
        worker.interrupt();
    }

    @AllArgsConstructor
    @ToString
    public static class AcceptEvent{
        
        public final String phoneNumber;
        public final Message message;
    }

}
