package com.example.koreanshopee.api;

import android.content.Context;

import com.example.koreanshopee.auth.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Sử dụng IP máy tính để mobile có thể kết nối được
    // 10.0.2.2 = Android Emulator
    // 192.168.1.4 = IP máy tính thật (từ ipconfig)
    private static final String BASE_URL = "http://192.168.1.4:32768/";
    private static Retrofit retrofit = null;
    private static Retrofit authenticatedRetrofit = null;
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Create OkHttpClient with logging interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
    
    public static Retrofit getAuthenticatedClient(Context context) {
        if (authenticatedRetrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Create OkHttpClient with auth interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .addInterceptor(loggingInterceptor)
                    .build();
            
            authenticatedRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return authenticatedRetrofit;
    }
    
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
    
    public static ApiService getAuthenticatedApiService(Context context) {
        return getAuthenticatedClient(context).create(ApiService.class);
    }
} 