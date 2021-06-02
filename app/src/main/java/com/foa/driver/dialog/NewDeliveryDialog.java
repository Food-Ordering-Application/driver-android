package com.foa.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foa.driver.model.Order;
import com.foa.driver.util.Helper;
import com.stfalcon.swipeablebutton.SwipeableButton;
import com.foa.driver.R;

import kotlin.Unit;

public class NewDeliveryDialog extends Dialog {

    private AcceptedListener listener;
    private SwipeableButton acceptButton;
    private Order order;
    private TextView customerAddress;
    private TextView restaurantAddress;
    private TextView customerName;
    private TextView restaurantName;
    private TextView deliveryDistance;
    private TextView shippingFee;
    private TextView grandTotal;
    private TextView estimateDelivery;
    private TextView countDownAcceptTime;

    public NewDeliveryDialog(Context context, Order order) {
        super(context);
        this.order = order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.new_delivery_dialog);
        init();
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownAcceptTime.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                dismiss();
            }
        }.start();
    }

    private void init(){
        acceptButton = findViewById(R.id.acceptSwipeButton);
        acceptButton.setOnSwipedOnListener(()->{
            if (listener!=null) listener.onAccept(true, order);
            return Unit.INSTANCE;
        });
        countDownAcceptTime  = findViewById(R.id.countDownAcceptTime);

         customerAddress = findViewById(R.id.customerAddress);
         restaurantAddress=findViewById(R.id.restaurantAddress);
         customerName=findViewById(R.id.customerName);
         restaurantName=findViewById(R.id.restaurantName);
         deliveryDistance=findViewById(R.id.deliveryDistance);
         shippingFee=findViewById(R.id.deliveryShippingFee);
         grandTotal=findViewById(R.id.grandTotal);
         estimateDelivery=findViewById(R.id.estimateTimeDelivery);

        customerAddress.setText(order.getDelivery().getCustomerAddress());
        restaurantAddress.setText(order.getDelivery().getRestaurantAddress());
        customerName.setText(order.getDelivery().getCustomerId());
        restaurantName.setText(order.getRestaurantId());
        deliveryDistance.setText(Helper.formatDistance(order.getDelivery().getDistance()));
        shippingFee.setText(Helper.formatMoney(order.getDelivery().getShippingFee()));
        grandTotal.setText(Helper.formatMoney(order.getGrandTotal()));
        estimateDelivery.setText("10:00");
    }

    public void setAcceptedListener(AcceptedListener listener){
        this.listener = listener;
    }

    public interface AcceptedListener{
        public void onAccept(boolean isAccept, Order order);
    }
}
