package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class DriverLocationBody {
    @SerializedName("driverId")
    private String driverId;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public DriverLocationBody(String driverId, double latitude, double longitude) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
