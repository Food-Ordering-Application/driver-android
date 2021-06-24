package com.foa.driver.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.foa.driver.LoginActivity;
import com.foa.driver.R;
import com.foa.driver.api.UserService;
import com.foa.driver.service.LocationService;
import com.foa.driver.session.LoginSession;
import com.foa.driver.session.OrderSession;
import com.foa.driver.util.Debouncer;
import com.foa.driver.util.Helper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {

    private View view;
    private RadioGroup activeRadioGroup;
    private RadioButton activeRadioButton;
    private RadioButton deActiveRadioButton;
    private LinearLayout signOut;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Debouncer debouncer = new Debouncer();
    private boolean currentActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        init();
        return view;
    }

    private void init() {
        activeRadioGroup = view.findViewById(R.id.activeRadioGroup);
        activeRadioButton = view.findViewById(R.id.onlineRadioButton);
        deActiveRadioButton = view.findViewById(R.id.offlineRadioButton);
        activeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            debouncer.debounce(ProfileFragment.class, () ->
                            updateDriverStatusAndLocation(radioGroup)
                    , 2000, TimeUnit.MILLISECONDS);
        });

        signOut = view.findViewById(R.id.signOut);
        signOut.setOnClickListener(view -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất khỏi ứng dụng")
                    .setPositiveButton("Có", (dialog, i) -> {
                        logout();
                    })
                    .setNegativeButton("Không", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        UserService.getActive((success, data) -> {
            if (success){
                currentActive = data.isActive();
                Log.e("curent-location",String.valueOf(currentActive));
                if (data.isActive()){
                    activeRadioButton.setChecked(true);
                    deActiveRadioButton.setChecked(false);

                }else{
                    activeRadioButton.setChecked(false);
                    deActiveRadioButton.setChecked(true);
                }

            }
        });
    }

    private void logout() {
        UserService.updateIsActive(false, lastLocation, success -> {
            currentActive=false;
            Log.e("curent-location",String.valueOf(currentActive));
            OrderSession.clearInstance();
            LoginSession.clearInstance();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();;
        });
    }

    private void updateDriverStatusAndLocation(RadioGroup radioGroup) {
        boolean isActive = radioGroup.getCheckedRadioButtonId() == R.id.onlineRadioButton;
        Log.e("curent-location",String.valueOf(currentActive));
        if(currentActive==isActive) return;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Vui lòng bật quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        updateService(radioGroup);
                        Log.e("Client Last location",location.getLatitude()+ ","+location.getLongitude());

                        UserService.updateIsActive(isActive,
                                location,success -> {
                                    if (success) {
                                        currentActive = isActive;
                                        Log.e("curent-location",String.valueOf(currentActive));
                                    }else{
                                        Toast.makeText(getActivity(), "Cập nhật nhận đơn thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "Không nhận được vị trí!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateService(RadioGroup radioGroup){
        Intent intent = new Intent(getActivity(),LocationService.class );
        if (radioGroup.getCheckedRadioButtonId() ==  R.id.onlineRadioButton){
            if (!Helper.isServiceRunning(LocationService.class,getActivity())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().startForegroundService(intent);
                } else {
                    getActivity().startService(intent);
                }
            }
        }else{
            getActivity().stopService(intent);
        }
    }


}