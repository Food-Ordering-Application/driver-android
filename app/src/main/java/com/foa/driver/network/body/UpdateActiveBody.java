package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class UpdateActiveBody {
    @SerializedName("isActive")
    private boolean isActive;

    public UpdateActiveBody(boolean isActive) {
        this.isActive = isActive;
    }
}
