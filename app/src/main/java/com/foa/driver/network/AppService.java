package com.foa.driver.network;

import com.foa.driver.network.body.ApproveDepositBody;
import com.foa.driver.network.body.CreateDepositBody;
import com.foa.driver.network.body.LocationBody;
import com.foa.driver.network.body.LoginBody;
import com.foa.driver.network.body.UpdateActiveBody;
import com.foa.driver.network.body.WithdrawMoneyBody;
import com.foa.driver.network.response.AccountWalletData;
import com.foa.driver.network.response.ActiveData;
import com.foa.driver.network.response.DepositData;
import com.foa.driver.network.response.CreateDepositData;
import com.foa.driver.network.response.LoginData;
import com.foa.driver.network.response.OrderData;
import com.foa.driver.network.response.OrderListData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.network.response.StatisticListData;
import com.foa.driver.network.response.TransactionListData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
            @Query("page") int page,
            @Query("size") int size,
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
    Call<ResponseAdapter<DepositData>> approveDepositMoneyToMainWallet(
            @Path("driverId") String driverId,
            @Body ApproveDepositBody body
    );

    @POST("/user/driver/{driverId}/withdraw-money-to-paypal-account")
    Call<ResponseAdapter<String>> withdrawMoneyToPayPalAccount(
            @Path("driverId") String driverId,
            @Body WithdrawMoneyBody body
    );

    @GET("/user/driver/{driverId}/account-wallet")
    Call<ResponseAdapter<AccountWalletData>> getAccountWallet(
            @Path("driverId") String driverId
    );

    @GET("/user/driver/{driverId}/transaction-histories")
    Call<ResponseAdapter<TransactionListData>> getTransactionHistory(
            @Path("driverId") String driverId,
            @Query("query") String type,
            @Query("page") int page,
            @Query("size") int size,
            @Query("transactionStatus") String transactionStatus
    );

    @GET("/user/driver/active")
    Call<ResponseAdapter<ActiveData>> getActive();

    @PUT("/user/driver/active")
    Call<ResponseAdapter<String>> updateIsActive(
            @Body UpdateActiveBody body
    );

    @PUT("user/driver/location")
    Call<ResponseAdapter<String>> updateLocation(
            @Body LocationBody body
    );

    @GET("/user/driver/{driverId}/weekly-statistic")
    Call<ResponseAdapter<StatisticListData>> getStatisticWeekly(
            @Path("driverId") String driverId
    );

    @GET("/user/driver/{driverId}/monthly-statistic")
    Call<ResponseAdapter<StatisticListData>> getStatisticMonthly(
            @Path("driverId") String driverId
    );
}
