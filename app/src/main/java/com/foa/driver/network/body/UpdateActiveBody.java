package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class UpdateActiveBody {
    @SerializedName("activeStatus")
    private boolean isActive;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public UpdateActiveBody(boolean isActive, double latitude, double longitude) {
        this.isActive = isActive;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UpdateActiveBody(boolean isActive) {
        this.isActive = isActive;
    }
}
