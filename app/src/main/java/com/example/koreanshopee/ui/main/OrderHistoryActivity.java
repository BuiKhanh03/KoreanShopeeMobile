package com.example.koreanshopee.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.OrderListResponse;
import com.example.koreanshopee.model.OrderHistory;
import com.example.koreanshopee.utils.TokenManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ImageView btnBack;
    private OrderHistoryAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
        tvEmpty = findViewById(R.id.tvOrderHistoryEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnOrderClickListener(paymentRecordId -> {
            android.content.Intent intent = new android.content.Intent(this, OrderDetailActivity.class);
            intent.putExtra("paymentRecordId", paymentRecordId);
            startActivity(intent);
        });
        loadOrderHistory();

        // Kích hoạt layout toàn màn hình và trong suốt status bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void loadOrderHistory() {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAuthorizationHeader();
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getOrderHistory(token).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null && !response.body().getValue().isEmpty()) {
                    List<OrderHistory> orders = response.body().getValue();
                    // Log trạng thái từng đơn hàng
                    for (OrderHistory order : orders) {
                        android.util.Log.d("OrderStatus", "OrderId: " + order.getId() + ", Status: " + order.getStatus());
                    }
                    adapter.setOrders(orders);
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
} 