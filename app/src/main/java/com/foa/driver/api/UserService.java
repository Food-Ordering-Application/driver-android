package com.foa.driver.api;


import com.foa.driver.model.AccountWallet;
import com.foa.driver.model.Order;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.RetrofitClient;
import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.AccountWalletData;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.OrderData;
import com.foa.driver.network.response.OrderListData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    public static void createDepositMoneyToMainWallet(String driverId,long moneyToDeposit, IDataResultCallback<String> resultCallback) {
        Call<ResponseAdapter<CreateDepositData>> responseCall = RetrofitClient.getInstance().getAppService()
                .createDepositMoneyToMainWallet(driverId,new CreateDepositBody(moneyToDeposit));
        responseCall.enqueue(new Callback<ResponseAdapter<CreateDepositData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<CreateDepositData>> call, Response<ResponseAdapter<CreateDepositData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<CreateDepositData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getPaypalOrderId());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<CreateDepositData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void approveDepositMoneyToMainWallet(String driverId,String paypalOrderId, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .approveDepositMoneyToMainWallet(driverId,new ApproveDepositBody(paypalOrderId));
        responseCall.enqueue(new Callback<ResponseAdapter<String>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<String>> call, Response<ResponseAdapter<String>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<String> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true);
                    } else {
                        resultCallback.onSuccess(false);
                    }
                } else {
                    resultCallback.onSuccess(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<String>> call, Throwable t) {
                resultCallback.onSuccess(false);
            }
        });
    }

    public static void withdrawMoneyToPayPalAccount(String driverId, long moneyToWithdraw, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .withdrawMoneyToPayPalAccount(driverId,new WithdrawMoneyBody(moneyToWithdraw));
        responseCall.enqueue(new Callback<ResponseAdapter<String>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<String>> call, Response<ResponseAdapter<String>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<String> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true);
                    } else {
                        resultCallback.onSuccess(false);
                    }
                } else {
                    resultCallback.onSuccess(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<String>> call, Throwable t) {
                resultCallback.onSuccess(false);
            }
        });
    }

    public static void getAccountWallet(String driverId, IDataResultCallback<AccountWallet> resultCallback) {
        Call<ResponseAdapter<AccountWalletData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAccountWallet(driverId);
        responseCall.enqueue(new Callback<ResponseAdapter<AccountWalletData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<AccountWalletData>> call, Response<ResponseAdapter<AccountWalletData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<AccountWalletData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getAccountWallet());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<AccountWalletData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }


}
