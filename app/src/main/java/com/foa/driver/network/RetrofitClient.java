package com.foa.driver.network;

import com.foa.driver.util.EnumRetrofitConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  Retrofit retrofit;
    private  static com.foa.driver.network.RetrofitClient instance;
    private static final String BASE_URL = "https://apigway.herokuapp.com/";

    private  RetrofitClient() {

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private  RetrofitClient(String accessToken) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", accessToken).build();
            return chain.proceed(request);
        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(new EnumRetrofitConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    public static com.foa.driver.network.RetrofitClient getInstance() {
        if(instance == null) {
            instance = new com.foa.driver.network.RetrofitClient();
        }
        return instance;
    }
    public AppService getAppService() {
        return retrofit.create(AppService.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setAuthorizationHeader(String accessToken){
        instance  = new com.foa.driver.network.RetrofitClient(accessToken);
    }

}
