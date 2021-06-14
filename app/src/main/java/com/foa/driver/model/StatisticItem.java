package com.foa.driver.model;

import com.google.gson.annotations.SerializedName;

public class StatisticItem {
    @SerializedName("income")
    private long income;
    @SerializedName("commission")
    private long commission;
    @SerializedName("numOrderFinished")
    private int numOrderFinished;

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getCommission() {
        return commission;
    }

    public void setCommission(long commission) {
        this.commission = commission;
    }

    public int getNumOrderFinished() {
        return numOrderFinished;
    }

    public void setNumOrderFinished(int numOrderFinished) {
        this.numOrderFinished = numOrderFinished;
    }

    public long getTotalShippingFee(){
        return income+ commission;
    }
}
