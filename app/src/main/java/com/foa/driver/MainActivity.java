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
import com.foa.driver.api.UserService;
import com.foa.driver.config.Config;
import com.foa.driver.dialog.NewDeliveryDialog;
import com.foa.driver.dialog.QRDialog;
import com.foa.driver.model.Order;
import com.foa.driver.model.enums.OrderStatusQuery;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.session.DriverModeSession;
import com.foa.driver.session.LoginSession;
import com.foa.driver.session.NotificationOrderIdSession;
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

import java.util.List;

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
        //navController.navigate(R.id.navigation_map);
        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }

        PushNotifications.start(getApplicationContext(), Config.PUSHER_ID);
        PushNotifications.addDeviceInterest(LoginSession.getInstance().getDriver().getId());

        initPusher();
        checkAndShowNotificationOrder();

    }

    private void checkAndShowNotificationOrder(){
        String orderId = NotificationOrderIdSession.getInstance();
        if(orderId!=null){
            NotificationOrderIdSession.clearInstance();
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

        OrderService.getAllOrder(LoginSession.getInstance().getDriver().getId(), OrderStatusQuery.ACTIVE.name(), 1, 25,null,null, (success, data) -> {
            if (success&& data.size()>0){
                OrderSession.setInstance(data.get(0));
                DriverModeSession.setInstance(data.get(0).getDelivery().getStatus());
                //navController.navigate(R.id.navigation_map);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginSession.clearInstance();
        UserService.updateIsActive(false, null, success -> {
            if (success) Log.e("Update Active :","false");
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