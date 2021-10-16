package com.onemsg.message;

import java.util.List;
import java.util.Objects;/*  */

import com.onemsg.message.device.MobilePhone;
import com.onemsg.message.device.WindowsNoticer;

public class MockDevices {
    
    private static final MobilePhone businessPhone = MobilePhone.create("10682320211010");

    public static final List<String> phoneNumbers = List.of(
        "18620210001",
        "18620210002",
        "15510100001",
        "17520212021",
        "17706187412",
        "13120210000"
    );

    static {
        for (String phoneNumber : phoneNumbers) {
            MobilePhone.create(phoneNumber);
        }
        WindowsNoticer windowsNoticer = new WindowsNoticer();
        
        MobilePhone.register(event -> {
            if(Objects.equals(event.phoneNumber , "18620210001")){
                String title = String.format("From: %s", event.message.sender);
                windowsNoticer.notice(title, event.message.content);
            }
        });
    }

    private MockDevices() {}

    public static MobilePhone businessPhone(){
        return businessPhone;
    }



}
