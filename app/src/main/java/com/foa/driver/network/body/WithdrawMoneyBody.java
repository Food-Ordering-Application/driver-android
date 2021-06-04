package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class WithdrawMoneyBody {
    @SerializedName("moneyToWithdraw")
    private String moneyToWithdraw;

    public WithdrawMoneyBody(String moneyToWithdraw) {
        this.moneyToWithdraw = moneyToWithdraw;
    }
}
