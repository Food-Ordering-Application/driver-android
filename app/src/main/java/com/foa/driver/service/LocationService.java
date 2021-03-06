package com.foa.driver.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TimingLogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.foa.driver.api.UserService;
import com.foa.driver.config.Config;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.body.DriverLocationBody;
import com.foa.driver.session.LoginSession;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

public class LocationService extends Service {

    private LocationManager mLocationManager = null;
    private TimingLogger timings;
    private FusedLocationProviderClient client;
    private final String TAG = "LOCATION_SERVICE";
    private boolean started = false;
    private Handler handler = new Handler();
    private Location lastLocation;
    private PubNub pubNub;

    private final int LOCATION_REFRESH_TIME = 10000;
    private final int LOCATION_REFRESH_DISTANCE = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        lastLocation = location;
                        Log.e("Service init last location", location.getLatitude() + "," + location.getLongitude());

                    }
                });

        timings = new TimingLogger("Update_Location", "Initialization");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("???ng d???ng Driver ??ang thu th???p v??? tr??")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }else{
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, new ServiceLocationListener());
            initPubNub();
            startUpdateLocation();
        }

        return START_STICKY;
    }

    private Runnable runnable = () -> {
        updateLocationToServer();
        if(started) {
            startUpdateLocation();
        }
    };

    public void stopUpdateLocation() {
        pubNub.unsubscribeAll();
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void startUpdateLocation() {
        started = true;
        handler.postDelayed(runnable, 15000);
    }

    private void updateLocationToServer() {
        JsonObject entryUpdate = new JsonObject();
        entryUpdate.addProperty("message", "location-update");
        JsonObject entryDataUpdate = new JsonObject();
        entryDataUpdate.addProperty("driverId",LoginSession.getInstance().getDriver().getId());
        entryDataUpdate.addProperty("latitude",lastLocation.getLatitude());
        entryDataUpdate.addProperty("longitude",lastLocation.getLongitude());
        entryUpdate.add("payload",entryDataUpdate);

        pubNub.publish().channel(Config.PUBNUB_CHANNEL_NAME).message(entryUpdate)
                .async((result, status) -> {
                    if (status.isError()) {
                        status.getErrorData().getThrowable().printStackTrace();
                    }
                    else {
                        Log.e("[PUBLISH: sent]", "timetoken: " + result.getTimetoken());
                    }
                });
//        UserService.updateLocation(lastLocation, success -> {
//            if (success)
//            Log.e("service", "call api:"+ lastLocation.getLatitude()+";"+lastLocation.getLongitude());
//        });
    }

    private void initPubNub(){
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(Config.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSubscribeKey(Config.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setUuid(LoginSession.getInstance().getDriver().getId());
        pubNub = new PubNub(pnConfiguration);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdateLocation();
        Log.e("locaton","service destroy");
    }

    private class ServiceLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            lastLocation = location;
        }


        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }


}
