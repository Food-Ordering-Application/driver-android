package com.foa.driver.api;


import com.foa.driver.model.Order;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.IResultCallback;
import com.foa.driver.network.RetrofitClient;
import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.OrderData;
import com.foa.driver.network.response.OrderListData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.util.Constants;
import com.foa.driver.util.Helper;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    public static void getOrderById(String orderId, IDataResultCallback<Order> resultCallback) {
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getOrderById(orderId);
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {

                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<OrderData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getOrder());
                    } else {
                        resultCallback.onSuccess(false, null);
                    }
                } else {
                    resultCallback.onSuccess(false, null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
            }
        });
    }


    public static void getAllOrder(String driverId, String status,String startDate, String endDate, IDataResultCallback<List<Order>> resultCallback) {
        Call<ResponseAdapter<OrderListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAllOrder(driverId, status, startDate,endDate);
        responseCall.enqueue(new Callback<ResponseAdapter<OrderListData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderListData>> call, Response<ResponseAdapter<OrderListData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<OrderListData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getOrders());
                    } else {
                        resultCallback.onSuccess(false, null);
                    }
                } else {
                    resultCallback.onSuccess(false, null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderListData>> call, Throwable t) {
                resultCallback.onSuccess(false, null);
            }
        });
    }

    public static void acceptOrder(String orderId, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .acceptOrder(orderId);
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

    public static void pickupOrder(String orderId, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .pickupOrder(orderId);
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

    public static void completeOrder(String orderId, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .completeOrder(orderId);
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

    public static void withdrawMoneyToPayPalAccount(String driverId,String moneyToWithdraw, IResultCallback resultCallback) {
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
}
