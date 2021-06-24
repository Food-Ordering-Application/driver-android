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
import com.foa.driver.network.IResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private LocationManager mLocationManager = null;
    private TimingLogger timings;
    private FusedLocationProviderClient client;
    private final String TAG = "LOCATION_SERVICE";
    private boolean started = false;
    private Handler handler = new Handler();
    private Location lastLocation;

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
                .setContentTitle("Ứng dụng Driver đang thu thập vị trí")
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
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void startUpdateLocation() {
        started = true;
        handler.postDelayed(runnable, 15000);
    }

    private void updateLocationToServer() {

        UserService.updateLocation(lastLocation, success -> {
            if (success)
            Log.e("service", "call api:"+ lastLocation.getLatitude()+";"+lastLocation.getLongitude());
        });
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
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }


}
