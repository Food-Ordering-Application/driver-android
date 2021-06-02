package com.foa.driver.session;


public class DriverMode {
    private static boolean isDriving = false;

    public DriverMode() {
    }

    public static boolean getInstance(){
        return isDriving;
    }

    public static void setInstance(boolean isDriving){
        DriverMode.isDriving = isDriving;
    }
}
