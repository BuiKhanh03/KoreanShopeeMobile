package com.example.koreanshopee.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.model.CartResponse;
import com.example.koreanshopee.model.OrderRequest;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductDetailResponse;
import com.example.koreanshopee.utils.TokenManager;

import java.util.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private TextView tvTotal;
    private TokenManager tokenManager;
    private Button btnCheckout;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private Map<String, Product> productMap = new HashMap<>();
    private TextView tvEmptyCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartItems, productMap, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateTotal();
            }
            @Override
            public void onDeleteItem(int position) {
                cartItems.remove(position);
                adapter.notifyItemRemoved(position);
                updateTotal();
            }
        });
        recyclerViewCart.setAdapter(adapter);

        loadCart();
        btnCheckout.setOnClickListener(v -> gotoConfirmOrder());
    }

    private void loadCart() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        tokenManager = new TokenManager(this);
        String authHeader = tokenManager.getAuthorizationHeader();
        if (authHeader == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        api.getCart(authHeader).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body().getValue().getCartItems());
                    loadProducts();
                }
            }
            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProducts() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        productMap.clear();
        final int[] loaded = {0};
        if (cartItems.isEmpty()) {
            adapter.notifyDataSetChanged();
            updateTotal();
            updateEmptyCartView();
            return;
        }
        for (CartItem item : cartItems) {
            api.getProductDetail(item.getProductId()).enqueue(new Callback<ProductDetailResponse>() {
                @Override
                public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        productMap.put(item.getProductId(), response.body().getValue());
                    }
                    loaded[0]++;
                    if (loaded[0] == cartItems.size()) {
                        adapter.notifyDataSetChanged();
                        updateTotal();
                        updateEmptyCartView();
                    }
                }
                @Override
                public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                    loaded[0]++;
                    if (loaded[0] == cartItems.size()) {
                        adapter.notifyDataSetChanged();
                        updateTotal();
                        updateEmptyCartView();
                    }
                }
            });
        }
    }

    private void updateEmptyCartView() {
        if (cartItems.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            tvTotal.setVisibility(View.GONE);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
            tvTotal.setVisibility(View.VISIBLE);
        }
    }

    private void updateTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPriceAtTime() * item.getQuantity();
        }
        tvTotal.setText("Tổng cộng: " + String.format("%,dđ", total));
    }

    private void gotoConfirmOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ConfirmOrderActivity.class);
        intent.putExtra("orderItems", new ArrayList<>(cartItems));
        intent.putExtra("productMap", new HashMap<>(productMap));
        startActivity(intent);
    }
} 