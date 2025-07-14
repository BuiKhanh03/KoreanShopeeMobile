package com.example.koreanshopee.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private RecyclerView recyclerView;
    private SellerProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private FloatingActionButton fabAddProduct;
    
    // Profile views
    private ImageView ivProfileImage;
    private TextView tvUserName, tvUserEmail, tvUserPhone;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        initViews();
        setupRecyclerView();
        setupListeners();
        loadUserProfile();
        loadSellerProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewSellerProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SellerProductAdapter(this, productList, new SellerProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // Mở chi tiết sản phẩm để chỉnh sửa
                Intent intent = new Intent(SellerActivity.this, EditProductActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Product product) {
                // Xóa sản phẩm
                deleteProduct(product);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(SellerActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            LogoutHelper.logout(this);
        });
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
                    
                    // Update UI with user profile
                    tvUserName.setText(userProfile.getFullName());
                    tvUserEmail.setText(userProfile.getEmail());
                    tvUserPhone.setText(userProfile.getPhoneNumber());
                    
                    // TODO: Load profile image if available
                    // if (userProfile.getImageLink() != null && !userProfile.getImageLink().equals("N/A")) {
                    //     // Load image using Glide or Picasso
                    // }
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(SellerActivity.this, "Lỗi tải thông tin profile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSellerProducts();
    }

    private void loadSellerProducts() {
        TokenManager tokenManager = new TokenManager(this);
        String token = "Bearer " + tokenManager.getToken();
        
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getSellerProducts(token).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    productList.clear();
                    productList.addAll(response.body().getValue());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SellerActivity.this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(SellerActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(Product product) {
        // TODO: Implement delete product API
        Toast.makeText(this, "Chức năng xóa sản phẩm sẽ được thêm sau!", Toast.LENGTH_SHORT).show();
    }
} 