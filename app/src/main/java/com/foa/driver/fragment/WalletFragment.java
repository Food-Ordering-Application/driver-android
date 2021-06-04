package com.foa.driver.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.foa.driver.BuildConfig;
import com.foa.driver.R;
import com.foa.driver.api.OrderService;
import com.foa.driver.dialog.CreateDepositDialog;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.session.LoginSession;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WalletFragment extends Fragment {

    private View view;
    private Button depositButton;
    private Button withdrawButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_wallet, container, false);
        init();
        return view;
    }

    private void init(){
        depositButton =  view.findViewById(R.id.depositButton);
        withdrawButton =  view.findViewById(R.id.withdrawButton);

        depositButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity());
            dialog.show();
        });

        withdrawButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity());
            dialog.show();
        });
    }
}