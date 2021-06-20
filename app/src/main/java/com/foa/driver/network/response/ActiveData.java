package com.foa.driver.network.response;

import com.google.gson.annotations.SerializedName;

public class ActiveData {
    @SerializedName( "activeStatus" )
    boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}