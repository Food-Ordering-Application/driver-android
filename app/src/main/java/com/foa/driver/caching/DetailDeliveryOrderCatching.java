package com.foa.driver.caching;

import com.foa.driver.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DetailDeliveryOrderCatching {
    private static List<Order> orderList = null;

    public DetailDeliveryOrderCatching() {
    }

    public static void addDeliveryCatching(Order order){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        boolean isContain  = orderList.contains(order);
        if (!isContain) orderList.add(order);
    }

    public static Order getOrderCatching(Order order){
        if (orderList==null) return null;
        for (int i = 0; i < orderList.size(); i++) {
            if(orderList.get(i).getId().equals(order.getId())){
                return orderList.get(i);
            }
        }
        return null;
    }

    public static void clearInstance(){
        orderList = null;
    }
}
