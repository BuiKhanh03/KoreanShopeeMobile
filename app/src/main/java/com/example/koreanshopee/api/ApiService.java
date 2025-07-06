package com.example.koreanshopee.api;

import com.example.koreanshopee.model.LoginRequest;
import com.example.koreanshopee.model.LoginResponse;
import com.example.koreanshopee.model.RegisterRequest;
import com.example.koreanshopee.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    
    @POST("api/authentication/register")
    Call<Void> register(@Body RegisterRequest registerRequest);
} 