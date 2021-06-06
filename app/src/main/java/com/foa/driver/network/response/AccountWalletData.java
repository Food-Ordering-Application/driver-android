package com.foa.driver.network.response;

import com.foa.driver.model.AccountWallet;
import com.google.gson.annotations.SerializedName;

public class AccountWalletData {
    @SerializedName( "accountWallet" )
    private AccountWallet accountWallet;

    public AccountWallet getAccountWallet() {
        return accountWallet;
    }

    public void setAccountWallet(AccountWallet accountWallet) {
        this.accountWallet = accountWallet;
    }
}