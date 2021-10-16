package com.onemsg.message.device;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WindowsNoticer{
    
    // windows 托盘图标，比如QQ的小企鹅
    private static final String ICON_FILE = "icon.png"; 

    private TrayIcon trayIcon;
    private String tooltip;

    public WindowsNoticer(){
        this("短信模拟收件箱");
    }

    public WindowsNoticer(String tooltip){

        this.tooltip = tooltip;

        URL url = WindowsNoticer.class.getClassLoader().getResource(ICON_FILE);
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException("WindowsNoticer 创建失败, icon 文件没有找到", e);
        }
        trayIcon = new TrayIcon(image, tooltip);
        trayIcon.setImageAutoSize(true);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("WindowsNoticer 创建失败, desktop system tray is missing", e);
        }

        log.debug("WindowsNoticer - {} 创建成功", tooltip);
    }

    public void notice(String title, String message){
        trayIcon.displayMessage(title, message, MessageType.INFO);
    }

    public void stop() {
        SystemTray.getSystemTray().remove(trayIcon);
        log.debug("WindowsNoticer - {} 已暂停", tooltip);
    }

    public static void main(String[] args) throws InterruptedException{

        WindowsNoticer noticer = new WindowsNoticer();

        noticer.notice("【掘金】系统消息", "Hello, 别忘了为你喜欢的文章点赞👍");
        TimeUnit.SECONDS.sleep(5);
        noticer.stop();
    }

}
