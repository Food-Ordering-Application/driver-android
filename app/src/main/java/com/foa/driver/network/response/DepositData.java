package com.foa.driver.network.response;

import com.google.gson.annotations.SerializedName;

public class DepositData {
    @SerializedName( "mainBalance" )
    long mainBalance;

    public long getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(long mainBalance) {
        this.mainBalance = mainBalance;
    }
}