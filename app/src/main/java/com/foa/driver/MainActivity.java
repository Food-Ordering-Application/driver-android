package com.foa.driver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.foa.driver.api.OrderService;
import com.foa.driver.dialog.NewDeliveryDialog;
import com.foa.driver.session.DriverModeSession;
import com.foa.driver.session.OrderSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
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
        PushNotifications.addDeviceInterest("a22f3f78-be7f-11eb-8529-0242ac130003");

        initPusher();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, remoteMessage -> {
            this.runOnUiThread(() -> {
                String orderId = remoteMessage.getData().get("orderId");
                if (orderId!=null){
                    OrderService.getOrderById(orderId, (success, data) -> {
                        if (success){
                            NewDeliveryDialog dialog =  new NewDeliveryDialog(this, data);
                            dialog.setAcceptedListener((isAccept,order) -> {
                                if (isAccept){
                                    OrderSession.setInstance(order);
                                    navController.navigate(R.id.navigation_map);
                                }
                            });
                            dialog.show();
                        }
                    });
                }
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

    private void initPusher(){
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        Pusher pusher = new Pusher("29ff5ecb5e2501177186", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);
    }
}