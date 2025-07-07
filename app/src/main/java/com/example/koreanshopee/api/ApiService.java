package com.example.koreanshopee.api;

import com.example.koreanshopee.model.LoginRequest;
import com.example.koreanshopee.model.LoginResponse;
import com.example.koreanshopee.model.RegisterRequest;
import com.example.koreanshopee.model.RegisterResponse;
import com.example.koreanshopee.model.UpdateProfileRequest;
import com.example.koreanshopee.model.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface ApiService {
    @POST("api/authentication/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    
    @POST("api/authentication/register")
    Call<Void> register(@Body RegisterRequest registerRequest);
    
    @GET("api/user")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String authorization);
    
    @PUT("api/user")
    Call<UserProfileResponse> updateUserProfile(@Header("Authorization") String authorization, @Body UpdateProfileRequest updateRequest);

    @Multipart
    @POST("/api/user/upload-avatar")
    Call<ResponseBody> uploadAvatar(
        @Header("Authorization") String authorization,
        @Part MultipartBody.Part image
    );
} 