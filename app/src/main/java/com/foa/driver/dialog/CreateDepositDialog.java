package com.foa.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.foa.driver.R;
import com.foa.driver.api.OrderService;
import com.foa.driver.model.Order;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Helper;
import com.paypal.checkout.paymentbutton.PaymentButton;
import com.stfalcon.swipeablebutton.SwipeableButton;

import kotlin.Unit;

public class CreateDepositDialog extends Dialog {

    private EditText money;
    private PaymentButton payPalButton;

    public CreateDepositDialog(Context context) {
        super(context);
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
        payPalButton.setEnabled(false);
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                payPalButton.setEnabled(money.getText().toString().length()>0);
            }
        });
        initPayPal();
    }

    private void initPayPal(){
        payPalButton.setup(
                createOrderActions ->
                        OrderService.createDepositMoneyToMainWallet(LoginSession.getInstance().getDriver().getId(), 30000, (success, paypalId) -> {
                            createOrderActions.set(paypalId);
                        }),
                approval -> OrderService.approveDepositMoneyToMainWallet(LoginSession.getInstance().getDriver().getId(),  approval.getData().getOrderId(), success -> {

                })
        );
    }
}
