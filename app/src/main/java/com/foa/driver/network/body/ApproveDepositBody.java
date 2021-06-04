package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class ApproveDepositBody {
    @SerializedName("paypalOrderId")
    private String paypalOrderId;

    public ApproveDepositBody(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }
}
