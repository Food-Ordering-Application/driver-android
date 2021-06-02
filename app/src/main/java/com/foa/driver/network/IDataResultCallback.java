package com.foa.driver.network;

public interface IDataResultCallback<T> {
    void onSuccess(boolean success,T data);
}
