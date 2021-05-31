package com.foa.driver.network;

import com.foa.driver.network.body.LoginBody;
import com.foa.driver.network.response.LoginData;
import com.foa.driver.network.response.ResponseAdapter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppService {


    @POST("/user/driver/login")
    Call<ResponseAdapter<LoginData>> login (
            @Body LoginBody login
    );

}
