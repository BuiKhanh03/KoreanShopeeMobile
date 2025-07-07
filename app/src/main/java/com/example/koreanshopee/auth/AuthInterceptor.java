package com.example.koreanshopee.auth;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Get the access token
        String accessToken = tokenManager.getAccessToken();
        
        // If we have a token, add it to the request header
        if (accessToken != null) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization",  accessToken)
                    .build();
            return chain.proceed(newRequest);
        }
        
        // If no token, proceed with original request
        return chain.proceed(originalRequest);
    }
} 