package com.foa.driver.session;

import com.foa.driver.model.Order;

public class NotificationOrderIdSession {
    private static String notificationOrderId = null;

    public NotificationOrderIdSession() {
    }

    public static String getInstance(){
        return notificationOrderId;
    }

    public static void setInstance(String currentOrder){
        NotificationOrderIdSession.notificationOrderId = currentOrder;
    }

    public static void clearInstance(){
        NotificationOrderIdSession.notificationOrderId = null;
    }
}
