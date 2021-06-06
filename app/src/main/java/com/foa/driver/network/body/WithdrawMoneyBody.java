package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class WithdrawMoneyBody {
    @SerializedName("moneyToWithdraw")
    private long moneyToWithdraw;

    public WithdrawMoneyBody(long moneyToWithdraw) {
        this.moneyToWithdraw = moneyToWithdraw;
    }
}
