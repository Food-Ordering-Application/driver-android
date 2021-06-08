package com.foa.driver.network.response;

import com.foa.driver.model.DriverTransaction;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionListData {

    @SerializedName("driverTransactions")
    List<DriverTransaction> driverTransactions;

    public TransactionListData(List<DriverTransaction> driverTransactions) {
        this.driverTransactions = driverTransactions;
    }

    public List<DriverTransaction> getDriverTransactions() {
        return driverTransactions;
    }

    public void setDriverTransactions(List<DriverTransaction> driverTransactions) {
        this.driverTransactions = driverTransactions;
    }
}

