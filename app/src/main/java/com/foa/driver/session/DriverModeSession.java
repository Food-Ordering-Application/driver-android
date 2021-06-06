package com.foa.driver.session;


import com.foa.driver.model.enums.DeliveryStatus;

public class DriverModeSession {
    private static DeliveryStatus  deliveryStatus = DeliveryStatus.DRAFT;

    public DriverModeSession() {
    }

    public static DeliveryStatus getInstance(){
        return deliveryStatus;
    }

    public static void setInstance(DeliveryStatus isDriving){
        DriverModeSession.deliveryStatus = isDriving;
    }

    public static void clearInstance(){
        DriverModeSession.deliveryStatus = DeliveryStatus.DRAFT;
    }
}
