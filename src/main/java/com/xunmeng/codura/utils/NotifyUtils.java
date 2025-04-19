package com.xunmeng.codura.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class NotifyUtils {
    public static void info(String msg){
        Notifications.Bus.notify(new Notification("codura",msg, NotificationType.INFORMATION));
    }
    public static void error(String msg){
        Notifications.Bus.notify(new Notification("codura",msg, NotificationType.ERROR));
    }

    public static void warning(String msg){
        Notifications.Bus.notify(new Notification("codura",msg, NotificationType.WARNING));
    }

    public static void update(String msg){
        Notifications.Bus.notify(new Notification("codura",msg, NotificationType.IDE_UPDATE));
    }
}
