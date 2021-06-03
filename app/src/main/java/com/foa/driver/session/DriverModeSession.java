package com.foa.driver.session;


public class DriverModeSession {
    private static boolean isDriving = false;

    public DriverModeSession() {
    }

    public static boolean getInstance(){
        return isDriving;
    }

    public static void setInstance(boolean isDriving){
        DriverModeSession.isDriving = isDriving;
    }
}
