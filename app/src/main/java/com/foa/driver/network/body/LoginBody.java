package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("phoneNumber")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
