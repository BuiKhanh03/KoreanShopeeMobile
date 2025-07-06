package com.example.koreanshopee.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.auth.LogoutHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Ví dụ về cách sử dụng authenticated API calls
 * Bạn có thể thêm các API endpoints khác vào ApiService và sử dụng tương tự
 */
public class ApiExample {
    
    /**
     * Ví dụ về cách gọi API có xác thực
     * Sử dụng ApiClient.getAuthenticatedApiService(context) để tự động thêm token
     */
    public static void callAuthenticatedApi(Context context) {
        // Sử dụng authenticated API service
        ApiService apiService = ApiClient.getAuthenticatedApiService(context);
        
        // Ví dụ: gọi API lấy thông tin user (bạn cần thêm endpoint này vào ApiService)
        // apiService.getUserProfile().enqueue(new Callback<UserProfile>() {
        //     @Override
        //     public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
        //         if (response.isSuccessful() && response.body() != null) {
        //             UserProfile userProfile = response.body();
        //             // Xử lý dữ liệu
        //             Toast.makeText(context, "Lấy thông tin thành công", Toast.LENGTH_SHORT).show();
        //         } else {
        //             // Xử lý lỗi
        //             if (response.code() == 401) {
        //                 // Token hết hạn, logout user
        //                 LogoutHelper.logout(context);
        //             } else {
        //                 Toast.makeText(context, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
        //             }
        //         }
        //     }
        //
        //     @Override
        //     public void onFailure(Call<UserProfile> call, Throwable t) {
        //         Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        //     }
        // });
    }
    
    /**
     * Ví dụ về cách gọi API không cần xác thực
     * Sử dụng ApiClient.getApiService() cho các API public
     */
    public static void callPublicApi(Context context) {
        // Sử dụng public API service
        ApiService apiService = ApiClient.getApiService();
        
        // Ví dụ: gọi API đăng ký (bạn cần thêm endpoint này vào ApiService)
        // SignupRequest signupRequest = new SignupRequest("email@example.com", "password", "name");
        // apiService.signup(signupRequest).enqueue(new Callback<SignupResponse>() {
        //     @Override
        //     public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
        //         if (response.isSuccessful()) {
        //             Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        //         } else {
        //             Toast.makeText(context, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        //         }
        //     }
        //
        //     @Override
        //     public void onFailure(Call<SignupResponse> call, Throwable t) {
        //         Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
        //     }
        // });
    }
} 