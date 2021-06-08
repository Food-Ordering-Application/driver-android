package com.foa.driver.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foa.driver.BuildConfig;
import com.foa.driver.R;
import com.foa.driver.adapter.TransactionListAdapter;
import com.foa.driver.api.OrderService;
import com.foa.driver.api.UserService;
import com.foa.driver.dialog.CreateDepositDialog;
import com.foa.driver.model.AccountWallet;
import com.foa.driver.model.DriverTransaction;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Helper;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
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
import java.util.List;

public class WalletFragment extends Fragment {

    private View view;
    private Button depositButton;
    private Button withdrawButton;
    private TextView mainBalanceTextView;
    private TextView depositBalance;
    private TransactionListAdapter transactionListAdapter;
    private RecyclerView transactionRecyclerView;
    private LinearLayout processLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_wallet, container, false);
        init();
        return view;
    }

    private void init(){
        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);
        processLoading = view.findViewById(R.id.processLoading);

        depositButton =  view.findViewById(R.id.depositButton);
        withdrawButton =  view.findViewById(R.id.withdrawButton);
        mainBalanceTextView = view.findViewById(R.id.mainBalanceTextView);
        depositBalance = view.findViewById(R.id.depositBalance);
        mainBalanceTextView.setText(Helper.formatMoney(0));

        depositButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity(),true);
            dialog.setBalanceChangeListener(balance -> {
                mainBalanceTextView.setText(Helper.formatMoney(balance));
            });
            dialog.show();
        });

        withdrawButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity(),false);
            dialog.show();
        });

        UserService.getAccountWallet(LoginSession.getInstance().getDriver().getId(), (success, data) -> {
            if (success){
                mainBalanceTextView.setText(Helper.formatMoney(data.getMainBalance()));
                depositBalance.setText(Helper.formatMoney(data.getDepositBalance()));
            }
        });
        processLoading.setVisibility(View.VISIBLE);
        UserService.getTransactionHistory(LoginSession.getInstance().getDriver().getId(), (success, data) -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            transactionListAdapter = new TransactionListAdapter(getActivity(),data);
            transactionListAdapter.notifyDataSetChanged();
            transactionRecyclerView.setLayoutManager(layoutManager);
            transactionRecyclerView.setAdapter(transactionListAdapter);
            processLoading.setVisibility(View.GONE);
        });
    }
}