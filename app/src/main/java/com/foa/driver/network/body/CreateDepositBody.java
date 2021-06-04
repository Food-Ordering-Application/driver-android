package com.foa.driver.network.body;

import com.google.gson.annotations.SerializedName;

public class CreateDepositBody {
    @SerializedName("moneyToDeposit")
    private long moneyToDeposit;

    public CreateDepositBody(long moneyToDeposit) {
        this.moneyToDeposit = moneyToDeposit;
    }
}
