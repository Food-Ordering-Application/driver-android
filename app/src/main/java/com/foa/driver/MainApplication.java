package com.foa.driver;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class MainApplication extends Application {
    final String YOUR_CLIENT_ID = "AboCQuy4ljFGWeaPoLStmmmTwA9NGX6UL_uEO9JICNF4KjcB_e20ChAuzX6yEC_kC2G6DJDta0V5DPxL";
    @Override
    public void onCreate() {
        super.onCreate();
        CheckoutConfig config = new CheckoutConfig(
                this,
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);
    }

}
