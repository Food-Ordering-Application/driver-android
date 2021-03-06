package com.foa.driver.model.enums;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    @SerializedName("DRAFT")
    DRAFT,
    @SerializedName("ORDERED")
    ORDERED,
    @SerializedName("COMPLETED")
    COMPLETED,
    @SerializedName("CONFIRMED")
    CONFIRMED,
    @SerializedName("CANCELLED")
    CANCELLED
}