package com.example.koreanshopee.ui.main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.OrderListResponse;
import com.example.koreanshopee.model.OrderHistory;
import com.example.koreanshopee.utils.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvOrderId, tvOrderStatus, tvOrderTotal, tvOrderShippingFee, tvOrderAddress, tvOrderDate, tvOrderPaymentStatus;
    private LinearLayout layoutOrderItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvOrderShippingFee = findViewById(R.id.tvOrderShippingFee);
        tvOrderAddress = findViewById(R.id.tvOrderAddress);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderPaymentStatus = findViewById(R.id.tvOrderPaymentStatus);
        layoutOrderItems = findViewById(R.id.layoutOrderItems);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        String paymentRecordId = getIntent().getStringExtra("paymentRecordId");
        if (paymentRecordId != null) {
            loadOrderDetail(paymentRecordId);
        } else {
            tvOrderId.setText("Không tìm thấy thông tin đơn hàng.");
        }
    }
    private void loadOrderDetail(String paymentRecordId) {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAuthorizationHeader();
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getOrderByPaymentRecordId(token, paymentRecordId).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null && !response.body().getValue().isEmpty()) {
                    OrderHistory order = response.body().getValue().get(0);
                    tvOrderId.setText("Mã đơn: #" + order.getId());
                    tvOrderStatus.setText("Trạng thái: " + order.getStatus());
                    tvOrderTotal.setText("Tổng tiền: " + String.format("%,.0fđ", order.getTotalAmount()));
                    tvOrderShippingFee.setText("Phí vận chuyển: " + String.format("%,.0fđ", order.getShippingFee()));
                    tvOrderAddress.setText("Địa chỉ: " + order.getShippingAddress());
                    tvOrderDate.setText("Ngày tạo: " + order.getCreateAt());
                    tvOrderPaymentStatus.setText("Trạng thái thanh toán: " + (order.getPaymentRecord() != null ? order.getPaymentRecord().getPaymentRecordStatus() : ""));
                    layoutOrderItems.removeAllViews();
                    if (order.getOrderItems() != null) {
                        for (OrderHistory.OrderItem item : order.getOrderItems()) {
                            TextView tv = new TextView(OrderDetailActivity.this);
                            tv.setText("- " + (item.getProductName() != null ? item.getProductName() : item.getProductId()) +
                                    " x" + item.getQuantity() + " (" + String.format("%,.0fđ", item.getPriceAtTime()) + ")");
                            tv.setTextSize(15f);
                            tv.setTextColor(getResources().getColor(android.R.color.black));
                            layoutOrderItems.addView(tv);
                        }
                    }
                } else {
                    tvOrderId.setText("Không tìm thấy thông tin đơn hàng.");
                }
            }
            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                tvOrderId.setText("Lỗi khi tải thông tin đơn hàng.");
            }
        });
    }
} 