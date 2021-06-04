package com.foa.driver.network.response;

import com.foa.driver.model.Driver;
import com.google.gson.annotations.SerializedName;

public class CreateDepositData {
    @SerializedName( "paypalOrderId" )
    String paypalOrderId;

    public CreateDepositData() {
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }
}