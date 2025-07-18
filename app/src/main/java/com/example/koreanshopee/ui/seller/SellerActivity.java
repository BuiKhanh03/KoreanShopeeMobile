package com.example.koreanshopee.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductListResponse;
import com.example.koreanshopee.model.UserProfileResponse;
import com.example.koreanshopee.model.UserProfile;
import com.example.koreanshopee.utils.TokenManager;
import com.example.koreanshopee.auth.LogoutHelper;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerActivity extends AppCompatActivity {
    private ImageView ivProfileImage;
    private TextView tvUserName, tvUserEmail, tvUserPhone;
    private CardView cardToggleProducts, cardToggleBuyers, cardToggleOrders, cardToggleSetting, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        cardToggleBuyers = findViewById(R.id.cardToggleBuyers);
        cardToggleOrders = findViewById(R.id.cardToggleOrders);
        cardToggleProducts = findViewById(R.id.cardToggleProducts);
        cardToggleSetting = findViewById(R.id.cardToggleSetting);

        cardToggleSetting.setOnClickListener(v -> {
            Intent intent = new Intent(SellerActivity.this, SellerProductsActivity.class);
            startActivity(intent);
        });

        cardToggleOrders.setOnClickListener(v -> {
            Intent intent = new Intent(SellerActivity.this, SellerProductsActivity.class);
            startActivity(intent);
        });

        cardToggleProducts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerActivity.this, SellerProductsActivity.class);
            startActivity(intent);
        });

        cardToggleBuyers.setOnClickListener(v -> {
            Intent intent = new Intent(SellerActivity.this, SellerProductsActivity.class);
            startActivity(intent);
        });


        initViews();
        setupListeners();
        loadUserProfile();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> LogoutHelper.logout(this));
    }

    private void loadUserProfile() {
        TokenManager tokenManager = new TokenManager(this);
        String token = "Bearer " + tokenManager.getToken();

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getUserProfile(token).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    UserProfile userProfile = response.body().getValue();
                    tvUserName.setText(userProfile.getFullName());
                    tvUserEmail.setText("Mail: " + userProfile.getEmail());
                    tvUserPhone.setText("SĐT: " + userProfile.getPhoneNumber());
                }
                else {
                    Toast.makeText(SellerActivity.this, "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(SellerActivity.this, "Lỗi kết nối khi tải profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 