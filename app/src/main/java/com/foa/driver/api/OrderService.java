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

    public static void getAllOrder(String driverId, String status,int page, int size, IDataResultCallback<List<Order>> resultCallback) {
        GregorianCalendar calendar = new GregorianCalendar();
        String startDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE,1);
        String endDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        getAllOrder(driverId, status, page, size,startDate,endDate, resultCallback);
    }


    public static void getAllOrder(String driverId, String status,int page, int size,String startDate, String endDate, IDataResultCallback<List<Order>> resultCallback) {

        Call<ResponseAdapter<OrderListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAllOrder(driverId, status, page, size,startDate,endDate);
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
}
