package com.foa.driver.network.response;

import com.foa.driver.model.Order;
import com.google.gson.annotations.SerializedName;

public class OrderData {

    @SerializedName("order")
    Order order;

    public OrderData(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

