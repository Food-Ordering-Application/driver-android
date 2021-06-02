package com.foa.driver.session;

import com.foa.driver.model.Order;

public class OrderSession {
    private static Order currentOrder = null;

    public OrderSession() {
    }

    public static Order getInstance(){
        if (currentOrder == null){
            currentOrder  = new Order();
        }
        return currentOrder;
    }

    public static void setInstance(Order currentOrder){
        OrderSession.currentOrder = currentOrder;
    }

    public static void clearInstance(){
        OrderSession.currentOrder = null;
    }
}
