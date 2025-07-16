package com.example.koreanshopee.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.koreanshopee.InfoApp;
import com.example.koreanshopee.auth.LogoutHelper;
import com.example.koreanshopee.ProfileCustomer;
import com.example.koreanshopee.R;
import com.example.koreanshopee.ReviewOrder;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.UserProfile;
import com.example.koreanshopee.model.UserProfileResponse;
import com.example.koreanshopee.utils.TokenManager;
import com.example.koreanshopee.ui.main.OrderHistoryActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {

    private TokenManager tokenManager;
    private ApiService apiService;
    private TextView tvProfileName;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize TokenManager and ApiService
        tokenManager = new TokenManager(requireContext());
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize UI elements
        tvProfileName = view.findViewById(R.id.profileName);
        // Note: We'll add email display later if needed

        // Load user profile
        loadUserProfile();

        // Gán sự kiện click cho từng nút
        LinearLayout btnAccountInfo = view.findViewById(R.id.btn_account_info);
        LinearLayout btnOrderHistory = view.findViewById(R.id.btn_order_history);
        LinearLayout btnPaymentHistory = view.findViewById(R.id.btn_order_review);
        LinearLayout btnInfoApp = view.findViewById(R.id.btn_app_info);
        LinearLayout btnLogout = view.findViewById(R.id.btn_logout);

        btnAccountInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileCustomer.class);
            startActivity(intent);
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        btnPaymentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReviewOrder.class);
            startActivity(intent);
        });

        btnInfoApp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InfoApp.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Đang logout...", Toast.LENGTH_SHORT).show();
            LogoutHelper.logout(getActivity());
        });
    }

    private void loadUserProfile() {
        String authHeader = tokenManager.getAuthorizationHeader();
        if (authHeader == null) {
            Toast.makeText(requireContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<UserProfileResponse> call = apiService.getUserProfile(authHeader);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile userProfile = response.body().getValue();
                    if (userProfile != null) {
                        updateProfileUI(userProfile);
                    } else {
                        Toast.makeText(requireContext(), "Không thể lấy thông tin user", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileUI(UserProfile userProfile) {
        if (tvProfileName != null) {
            String fullName = userProfile.getFullName();
            if (fullName.trim().isEmpty() || fullName.equals("string string")) {
                tvProfileName.setText(userProfile.getUserName());
            } else {
                tvProfileName.setText(fullName);
            }
        }
    }
}
