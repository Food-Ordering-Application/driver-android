package com.foa.driver.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.driver.R;
import com.foa.driver.adapter.TransactionListAdapter;
import com.foa.driver.api.UserService;
import com.foa.driver.dialog.CreateDepositDialog;
import com.foa.driver.network.response.DepositData;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Helper;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class WalletFragment extends Fragment {

    private View view;
    private Button depositButton;
    private Button withdrawButton;
    private TextView mainBalanceTextView;
    private TextView depositBalance;
    private TransactionListAdapter transactionListAdapter;
    private RecyclerView transactionRecyclerView;
    private LinearLayout processLoading;
    private LinearLayout withdrawPendingLayout;
    private TextView pendingMoneyTextView;

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
        withdrawPendingLayout = view.findViewById(R.id.withdrawPendingText);
        pendingMoneyTextView = view.findViewById(R.id.pendingMoneyTextView);

        depositButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity(),true);
            dialog.setBalanceChangeListener(balance -> {
                if (balance==-1){
                    Toast.makeText(getActivity(), "Nạp tiền thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    mainBalanceTextView.setText(Helper.formatMoney(balance));
                }
                dialog.dismiss();
            });
            dialog.show();
        });

        withdrawButton.setOnClickListener(view -> {
            CreateDepositDialog dialog = new CreateDepositDialog(getActivity(),false);
            dialog.setWithdrawPendingListener(pendingMoney -> {
                pendingMoneyTextView.setText(Helper.formatMoney(pendingMoney));
                withdrawPendingLayout.setVisibility(View.VISIBLE);
            });
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

        initPusher();
    }

    private void initPusher(){
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        Pusher pusher = new Pusher("29ff5ecb5e2501177186", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {

            }

            @Override
            public void onError(String message, String code, Exception e) {
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe(LoginSession.getInstance().getDriver().getId());

        channel.bind("balance-changed", event -> {
            getActivity().runOnUiThread(() -> {
                Gson g = new Gson();
                DepositData depositData = g.fromJson(event.getData(), DepositData.class);
                mainBalanceTextView.setText(Helper.formatMoney(depositData.getMainBalance()));
                withdrawPendingLayout.setVisibility(View.GONE);
            });

        });
    }
}