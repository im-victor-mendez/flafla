package com.example.flafla.utils;

public interface AuthCallback {
    void onSuccess(String message);

    void onError(Exception exception);
}
