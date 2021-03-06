package com.foa.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.driver.R;
import com.foa.driver.api.UserService;
import com.foa.driver.session.LoginSession;
import com.paypal.checkout.paymentbutton.PaymentButton;
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatus;
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatusChanged;

import org.jetbrains.annotations.NotNull;

public class CreateDepositDialog extends Dialog {

    private Context context;
    private EditText money;
    private PaymentButton payPalButton;
    private Button withDrawButton;
    private Button createDepositButton;
    private boolean isDeposit;
    private long numberMoney;
    private TextView titleTransaction;
    private BalanceChangeListener balanceChangeListener;
    private WithdrawPendingListener withdrawPendingListener;
    private ProgressBar processLoading;


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
        createDepositButton = findViewById(R.id.createDepositButton);
        titleTransaction = findViewById(R.id.titleTransaction);
        processLoading = findViewById(R.id.processLoading);
        if (isDeposit){
            initPayPal();
            createDepositButton.setVisibility(View.VISIBLE);
            withDrawButton.setVisibility(View.GONE);
            titleTransaction.setText("N???P TI???N");
        }else{
            initWithdraw();
            createDepositButton.setVisibility(View.GONE);
            withDrawButton.setVisibility(View.VISIBLE);
            withDrawButton.setEnabled(false);
            titleTransaction.setText("R??T TI???N");

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
                String moneyStr =money.getText().toString();
                if (moneyStr.length()>0){
                    numberMoney = Long.parseLong(moneyStr);
                    boolean isEnable = numberMoney%1000==0 && numberMoney >=200000;
                    withDrawButton.setEnabled(isEnable);
                    createDepositButton.setEnabled(isEnable);
                }

            }
        });

        createDepositButton.setOnClickListener(view -> {
            payPalButton.performClick();
            createDepositButton.setEnabled(false);
            processLoading.setVisibility(View.VISIBLE);
        });

    }

    private void initPayPal(){
        payPalButton.setup(
                createOrderActions ->{
                    UserService.createDepositMoneyToMainWallet(numberMoney, (success, paypalId) -> {
                        createOrderActions.set(paypalId);
                    });
                }
                       ,
                approval -> UserService.approveDepositMoneyToMainWallet(approval.getData().getOrderId(),
                        (success,depositData) -> {
                    if (success){
                        if (balanceChangeListener !=null){
                            balanceChangeListener.onChange(depositData.getMainBalance());
                        }
                    }else{
                        if (balanceChangeListener !=null){
                            balanceChangeListener.onChange(-1);
                            createDepositButton.setEnabled(true);
                        }
                    }

                })
        );
    }

    private void initWithdraw(){
        withDrawButton.setOnClickListener(view -> {
            UserService.withdrawMoneyToPayPalAccount(LoginSession.getInstance().getDriver().getId(), numberMoney, success -> {
                if (success){
                   if(withdrawPendingListener!=null) withdrawPendingListener.onPending(numberMoney);
                    dismiss();
                }else{
                    createDepositButton.setEnabled(true);
                    processLoading.setVisibility(View.GONE);
                    Toast.makeText(context, "R??t ti???n th???t b???i, vui l??ng th??? l???i", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    public void setBalanceChangeListener(BalanceChangeListener listener){
        this.balanceChangeListener = listener;
    }

    public interface BalanceChangeListener{
        void onChange(long balance);
    }

    public void setWithdrawPendingListener(WithdrawPendingListener listener){
        this.withdrawPendingListener = listener;
    }

    public interface WithdrawPendingListener{
        void onPending(long pendingMoney);
    }
}
