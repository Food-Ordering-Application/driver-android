package com.foa.driver.network.response;

import com.google.gson.annotations.SerializedName;

public class ApproveDepositData {
    @SerializedName( "mainBalance" )
    Long mainBalance;

    public long getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(Long mainBalance) {
        this.mainBalance = mainBalance;
    }
}