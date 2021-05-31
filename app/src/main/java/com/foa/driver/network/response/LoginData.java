package com.foa.driver.network.response;

import com.foa.driver.model.Driver;
import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName( "user" )
    Driver driver;
    @SerializedName("access_token")
    String accessToken;

    public LoginData() {
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getBearerAccessToken() {
        return "Bearer "+accessToken;
    }
}