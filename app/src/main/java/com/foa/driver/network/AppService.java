package com.foa.driver.network;

import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.LoginBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.LoginData;
import com.foa.driver.network.response.OrderData;
import com.foa.driver.network.response.OrderListData;
import com.foa.driver.network.response.ResponseAdapter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppService {


    @POST("/user/driver/login")
    Call<ResponseAdapter<LoginData>> login (
            @Body LoginBody login
    );

    @GET("/order/{orderId}")
    Call<ResponseAdapter<OrderData>> getOrderById(
            @Path("orderId") String orderId
    );


    @GET("/order/driver/{driverId}/list-order")
    Call<ResponseAdapter<OrderListData>> getAllOrder(
            @Path("driverId") String orderId,
            @Query("query") String status,
            @Query("start") String startDate,
            @Query("end") String endDate
    );

    @POST("/user/driver/order/{orderId}/accept")
    Call<ResponseAdapter<String>> acceptOrder(
            @Path("orderId") String orderId
    );

    @POST("/user/driver/order/{orderId}/pickup")
    Call<ResponseAdapter<String>> pickupOrder(
            @Path("orderId") String orderId
    );

    @POST("/user/driver/order/{orderId}/complete")
    Call<ResponseAdapter<String>> completeOrder(
            @Path("orderId") String orderId
    );

    @POST("/user/driver/{driverId}/create-deposit-money-to-main-wallet")
    Call<ResponseAdapter<CreateDepositData>> createDepositMoneyToMainWallet(
            @Path("driverId") String driverId,
            @Body CreateDepositBody body
    );

    @PATCH("/user/driver/{driverId}/approve-deposit-money-to-main-wallet")
    Call<ResponseAdapter<String>> approveDepositMoneyToMainWallet(
            @Path("driverId") String driverId,
            @Body ApproveDepositBody body
    );

    @POST("/user/driver/{driverId}/withdraw-money-to-paypal-account")
    Call<ResponseAdapter<String>> withdrawMoneyToPayPalAccount(
            @Path("driverId") String driverId,
            @Body WithdrawMoneyBody body
    );
}
