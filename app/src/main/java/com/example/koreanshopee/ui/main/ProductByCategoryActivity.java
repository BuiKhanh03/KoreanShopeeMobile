package com.example.koreanshopee.ui.main;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductListResponse;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.model.CartResponse;
import com.example.koreanshopee.utils.TokenManager;
import com.example.koreanshopee.Item;
import com.example.koreanshopee.ItemAdapter;
import com.example.koreanshopee.ProductDetailActivity;
import com.example.koreanshopee.ui.main.ReviewDialogFragment;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductByCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> productList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);
        
        recyclerView = findViewById(R.id.recyclerViewProductByCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        adapter = new ItemAdapter(this, productList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                // Mở chi tiết sản phẩm
                Intent intent = new Intent(ProductByCategoryActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", item.getId());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Item item) {
                // Thêm vào giỏ hàng
                addToCart(item);
            }

            @Override
            public void onReviewClick(Item item) {
                // Mở dialog đánh giá
                showReviewDialog(item);
            }
        });
        
        recyclerView.setAdapter(adapter);
        
        String categoryId = getIntent().getStringExtra("categoryId");
        if (categoryId != null) {
            loadProductsByCategory(categoryId);
        } else {
            Toast.makeText(this, "Không tìm thấy categoryId", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadProductsByCategory(String categoryId) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getProductsByCategory(categoryId).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    productList.clear();
                    // Chuyển đổi Product thành Item
                    for (Product product : response.body().getValue()) {
                        Item item = new Item();
                        item.setId(product.getId());
                        item.setName(product.getName());
                        item.setPrice(product.getPrice());
                        // Set image from productImages if available
                        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                            item.setImage(product.getProductImages().get(0).toString());
                        }
                        item.setDescription(product.getDescription());
                        item.setCategoryId(product.getCategoryId());
                        productList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ProductByCategoryActivity.this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(ProductByCategoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(Item item) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        TokenManager tokenManager = new TokenManager(this);
        String token = "Bearer " + tokenManager.getToken();
        
        api.addToCart(token, item.getId(), 1).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductByCategoryActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductByCategoryActivity.this, "Lỗi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductByCategoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showReviewDialog(Item item) {
        ReviewDialogFragment dialog = ReviewDialogFragment.newInstance(item.getId());
        dialog.show(getSupportFragmentManager(), "ReviewDialog");
    }
} 