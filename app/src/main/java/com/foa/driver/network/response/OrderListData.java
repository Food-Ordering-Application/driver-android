package com.foa.driver.network.response;

import com.foa.driver.model.Order;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderListData {

    @SerializedName("orders")
    List<Order> orders;

    public OrderListData(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

