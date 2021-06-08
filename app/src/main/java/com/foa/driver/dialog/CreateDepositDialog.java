package com.foa.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foa.driver.R;
import com.foa.driver.api.OrderService;
import com.foa.driver.api.UserService;
import com.foa.driver.model.Order;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Helper;
import com.paypal.checkout.paymentbutton.PaymentButton;
import com.stfalcon.swipeablebutton.SwipeableButton;

import kotlin.Unit;

public class CreateDepositDialog extends Dialog {

    private Context context;
    private EditText money;
    private PaymentButton payPalButton;
    private Button withDrawButton;
    private boolean isDeposit;
    private long numberMoney;
    private TextView titleTransaction;
    private BalanceChangeListener listener;

    public CreateDepositDialog(Context context, boolean isDeposit) {
        super(context);
        this.context = context;
        this.isDeposit = isDeposit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_create_deposit);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init(){
        money = findViewById(R.id.moneyInput);
        payPalButton = findViewById(R.id.payPalButton);
        withDrawButton = findViewById(R.id.withdrawButton);
        titleTransaction = findViewById(R.id.titleTransaction);
        if (isDeposit){
            initPayPal();
            payPalButton.setVisibility(View.VISIBLE);
            withDrawButton.setVisibility(View.GONE);
            titleTransaction.setText("NẠP TIỀN");
        }else{
            initWithdraw();
            payPalButton.setVisibility(View.GONE);
            withDrawButton.setVisibility(View.VISIBLE);
            withDrawButton.setEnabled(false);
            titleTransaction.setText("RÚT TIỀN");

        }
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                numberMoney = Long.parseLong(money.getText().toString());
                boolean isEnable = numberMoney%1000==0 && numberMoney >200000;
                payPalButton.setEnabled(isEnable);
                withDrawButton.setEnabled(isEnable);
            }
        });


    }

    private void initPayPal(){
        payPalButton.setup(
                createOrderActions ->{
                    UserService.createDepositMoneyToMainWallet(LoginSession.getInstance().getDriver().getId(), numberMoney, (success, paypalId) -> {
                        createOrderActions.set(paypalId);
                    });
                    dismiss();
                }
                       ,
                approval -> UserService.approveDepositMoneyToMainWallet(LoginSession.getInstance().getDriver().getId(),  approval.getData().getOrderId(), (success,mainBalance) -> {
                    if (listener!=null) listener.onChange(mainBalance);
                })
        );
    }

    private void initWithdraw(){
        withDrawButton.setOnClickListener(view -> {
            UserService.withdrawMoneyToPayPalAccount(LoginSession.getInstance().getDriver().getId(), numberMoney, success -> {
                if (success){
                    dismiss();
                }
            });
        });

    }

    public void setBalanceChangeListener(BalanceChangeListener listener){
        this.listener = listener;
    }

    public interface BalanceChangeListener{
        void onChange(Long balance);
    }
}
