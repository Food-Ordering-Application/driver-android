package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class LocationBody {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public LocationBody(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
