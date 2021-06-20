package com.foa.driver.api;


import android.location.Location;
import android.util.Log;

import com.foa.driver.model.AccountWallet;
import com.foa.driver.model.DriverTransaction;
import com.foa.driver.model.StatisticItem;
import com.foa.driver.model.enums.TransactionStatus;
import com.foa.driver.model.enums.TransactionType;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.RetrofitClient;
import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.LocationBody;
import com.foa.driver.network.body.UpdateActiveBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.AccountWalletData;
import com.foa.driver.network.response.ActiveData;
import com.foa.driver.network.response.DepositData;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.LoginData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.network.response.StatisticListData;
import com.foa.driver.network.response.TransactionListData;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    public static void createDepositMoneyToMainWallet(long moneyToDeposit, IDataResultCallback<String> resultCallback) {
        String driverId = LoginSession.getInstance().getDriver().getId();
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

    public static void approveDepositMoneyToMainWallet(String paypalOrderId, IDataResultCallback<DepositData> resultCallback) {
        String driverId = LoginSession.getInstance().getDriver().getId();
        Call<ResponseAdapter<DepositData>> responseCall = RetrofitClient.getInstance().getAppService()
                .approveDepositMoneyToMainWallet(driverId,new ApproveDepositBody(paypalOrderId));
        responseCall.enqueue(new Callback<ResponseAdapter<DepositData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<DepositData>> call, Response<ResponseAdapter<DepositData>> response) {
                Log.e("PAYPAL_DEPOSIT","api status"+ response.code());
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<DepositData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<DepositData>> call, Throwable t) {
                Log.e("PAYPAL_DEPOSIT","api fail");
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

    public static void getStatisticMonthly(IDataResultCallback<List<StatisticItem>> resultCallback) {
        String driverId = LoginSession.getInstance().getDriver().getId();
        Call<ResponseAdapter<StatisticListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getStatisticMonthly(driverId);
        responseCall.enqueue(new Callback<ResponseAdapter<StatisticListData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<StatisticListData>> call, Response<ResponseAdapter<StatisticListData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<StatisticListData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getStatisticItemList());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<StatisticListData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void getStatisticWeekly(IDataResultCallback<List<StatisticItem>> resultCallback) {
        String driverId = LoginSession.getInstance().getDriver().getId();
        Call<ResponseAdapter<StatisticListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getStatisticWeekly(driverId);
        responseCall.enqueue(new Callback<ResponseAdapter<StatisticListData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<StatisticListData>> call, Response<ResponseAdapter<StatisticListData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<StatisticListData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData().getStatisticItemList());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<StatisticListData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void getActive( IDataResultCallback<ActiveData> resultCallback) {
        Call<ResponseAdapter<ActiveData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getActive();
        responseCall.enqueue(new Callback<ResponseAdapter<ActiveData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<ActiveData>> call, Response<ResponseAdapter<ActiveData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<ActiveData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<ActiveData>> call, Throwable t) {
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void updateIsActive(boolean isActive, Location location, IResultCallback resultCallback) {
        UpdateActiveBody body;
        if (isActive && location!=null){
           body = new UpdateActiveBody(true,location.getLatitude(),location.getLongitude());
        }else{
            body = new UpdateActiveBody(false);
        }
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateIsActive(body);
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

    public static void updateLocation(Location location, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateLocation(new LocationBody(location.getLatitude(),location.getLongitude()));
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
