package com.foa.driver.api;


import com.foa.driver.model.Order;
import com.foa.driver.network.IDataResultCallback;
import com.foa.driver.network.RetrofitClient;
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


    public static void getAllOrder(String driverId, String status, IDataResultCallback<List<Order>> resultCallback) {
        GregorianCalendar calendar = new GregorianCalendar();
        //String strStartDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        //calendar.add(Calendar.DATE,1);
        //String strEndDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        Call<ResponseAdapter<OrderListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAllOrder(driverId, status);
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
}
