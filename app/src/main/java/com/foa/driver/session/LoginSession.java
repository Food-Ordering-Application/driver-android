package com.foa.driver.session;

import com.foa.driver.network.response.LoginData;

public class LoginSession {
    private static LoginData loginData = null;

    public LoginSession() {
    }

    public static LoginData getInstance(){
        if (loginData == null){
            loginData  = new LoginData();
        }
        return loginData;
    }

    public static void setInstance(LoginData loginData){
        LoginSession.loginData = loginData;
    }

    public static void clearInstance(){
        LoginSession.loginData = null;
    }
}
