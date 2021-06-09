package com.foa.driver.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.foa.driver.LoginActivity;
import com.foa.driver.MainActivity;
import com.foa.driver.R;
import com.foa.driver.api.UserService;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.session.LoginSession;
import com.foa.driver.session.OrderSession;
import com.foa.driver.util.Debouncer;

import java.util.concurrent.TimeUnit;

public class ProfileFragment extends Fragment {

    private View view;
    private RadioGroup activeRadioGroup;
    private LinearLayout signOut;
    private Debouncer debouncer = new Debouncer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return view;
    }

    private void init(){
        activeRadioGroup = view.findViewById(R.id.activeRadioGroup);
        activeRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            debouncer.debounce(ProfileFragment.class, () ->
                    UserService.updateIsActive(LoginSession.getInstance().getDriver().getId(),
                            radioGroup.getCheckedRadioButtonId() == R.id.onlineRadioButton, success -> {

                            })
                    , 3000, TimeUnit.MILLISECONDS);
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
    }

    private void logout(){
        UserService.updateIsActive(LoginSession.getInstance().getDriver().getId(), false, success -> {
            OrderSession.clearInstance();
            LoginSession.clearInstance();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

    }
}