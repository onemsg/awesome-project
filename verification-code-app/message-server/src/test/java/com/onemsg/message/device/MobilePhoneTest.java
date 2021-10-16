package com.onemsg.message.device;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MobilePhoneTest {
    
    private MobilePhone iPhone13;

    private MobilePhone iPhone13ProMax;
    
    private WindowsNoticer windowsNoticer;

    @BeforeEach
    public void setup(){
        iPhone13 = MobilePhone.create("18620210001");
        iPhone13ProMax = MobilePhone.create("15520210001");

        windowsNoticer = new WindowsNoticer();

        EventQueue.register(System.out::println);
        EventQueue.register(event -> {
            

            String title = String.format("From: %s", event.message.sender);
            windowsNoticer.notice(title, event.message.content);
        });
    }

    @Test
    public void testMessageSend(){
        iPhone13.send("15520210001", "你好，约吗?");
        assertNotNull(iPhone13ProMax.inBoxes.stream()
            .filter(msg -> msg.sender == "18620210001"));
    }
}
