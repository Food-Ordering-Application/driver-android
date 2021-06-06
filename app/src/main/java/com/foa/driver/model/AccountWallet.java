package com.foa.driver.model;

import com.google.gson.annotations.SerializedName;

public class AccountWallet {
    @SerializedName("id")
    private String id;
    @SerializedName("mainBalance")
    private long mainBalance;
    @SerializedName("depositBalance")
    private long depositBalance;

    public AccountWallet(String id, long mainBalance, long depositBalance) {
        this.id = id;
        this.mainBalance = mainBalance;
        this.depositBalance = depositBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(long mainBalance) {
        this.mainBalance = mainBalance;
    }

    public long getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(long depositBalance) {
        this.depositBalance = depositBalance;
    }
}
