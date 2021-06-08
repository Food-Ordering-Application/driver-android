package com.foa.driver.model;

import com.foa.driver.model.enums.TransactionType;
import com.google.gson.annotations.SerializedName;

public class DriverTransaction {
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private TransactionType type;
    @SerializedName("amount")
    private long amount;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("payinTransaction")
    private PayInTransaction payinTransaction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PayInTransaction getPayinTransaction() {
        return payinTransaction;
    }

    public void setPayinTransaction(PayInTransaction payinTransaction) {
        this.payinTransaction = payinTransaction;
    }
}
