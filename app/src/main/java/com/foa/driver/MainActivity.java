package com.foa.driver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.foa.driver.api.OrderService;
import com.foa.driver.dialog.NewDeliveryDialog;
import com.foa.driver.session.DriverModeSession;
import com.foa.driver.session.OrderSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.pusher.pushnotifications.PushNotifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity  {

    private final int LOCATION_REQUEST_CODE = 1999;
    private final String CHANNEL_ID = "123";
    private final int notificationId = 93;
    private NavController navController;
    private TextView countDownAcceptTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));



        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }

        PushNotifications.start(getApplicationContext(), "77650b88-b6b2-4178-9fc2-95c36493470d");
        PushNotifications.addDeviceInterest("debug-hello");
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, remoteMessage -> {
            this.runOnUiThread(() -> {
                OrderService.getOrderById("af4a03a3-c85f-4773-a62b-11c71774a50e", (success, data) -> {
                    if (success){
                        NewDeliveryDialog dialog =  new NewDeliveryDialog(this, data);
                        dialog.setAcceptedListener((isAccept,order) -> {
                            if (isAccept){
                                OrderSession.setInstance(order);
                                DriverModeSession.setInstance(true);
                                navController.navigate(R.id.navigation_map);
                            }
                            dialog.dismiss();
                        });
                        dialog.show();
                    }
                });

            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

            }
            else {
                finish();
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "This is description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}