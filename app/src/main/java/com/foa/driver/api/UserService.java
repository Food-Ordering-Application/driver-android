package com.foa.driver.api;


import com.foa.driver.model.AccountWallet;
import com.foa.driver.model.DriverTransaction;
import com.foa.driver.model.Order;
import com.foa.driver.model.enums.TransactionStatus;
import com.foa.driver.model.enums.TransactionType;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.RetrofitClient;
import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.UpdateActiveBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.AccountWalletData;
import com.foa.driver.network.response.ApproveDepositData;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.OrderData;
import com.foa.driver.network.response.OrderListData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.network.response.TransactionListData;
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

    public static void approveDepositMoneyToMainWallet(String driverId,String paypalOrderId, IDataResultCallback<Long> resultCallback) {
        Call<ResponseAdapter<ApproveDepositData>> responseCall = RetrofitClient.getInstance().getAppService()
                .approveDepositMoneyToMainWallet(driverId,new ApproveDepositBody(paypalOrderId));
        responseCall.enqueue(new Callback<ResponseAdapter<ApproveDepositData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<ApproveDepositData>> call, Response<ResponseAdapter<ApproveDepositData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<ApproveDepositData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getMainBalance());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<ApproveDepositData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
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

    public static void getTransactionHistory(String driverId, IDataResultCallback<List<DriverTransaction>> resultCallback) {
        Call<ResponseAdapter<TransactionListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getTransactionHistory(driverId, TransactionType.ALL.name(),1,5,TransactionStatus.ALL.name());
        responseCall.enqueue(new Callback<ResponseAdapter<TransactionListData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<TransactionListData>> call, Response<ResponseAdapter<TransactionListData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<TransactionListData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getDriverTransactions());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<TransactionListData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void updateIsActive(String driverId,boolean isActive, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateIsActive(driverId,new UpdateActiveBody(isActive));
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


}
