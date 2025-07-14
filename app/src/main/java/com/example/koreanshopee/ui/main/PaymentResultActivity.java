package com.example.koreanshopee.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.CartResponse;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.utils.TokenManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        ImageView imgStatus = findViewById(R.id.imgStatus);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvAmount = findViewById(R.id.tvAmount);
        TextView tvTransactionId = findViewById(R.id.tvTransactionId);
        TextView tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        String json = getIntent().getStringExtra("paymentResultJson");
        boolean isSuccess = false;
        try {
            JSONObject obj = new JSONObject(json);
            isSuccess = obj.optBoolean("isSuccess");
            String orderId = obj.optString("orderId");
            String paymentId = obj.optString("paymentIdR");
            String paymentMethod = obj.optString("paymentMethod");
            String amount = obj.optString("amount");
            String transactionId = obj.optString("transactionId");
            String vnPayResponseCode = obj.optString("vnPayResponseCode");

            if (isSuccess && "00".equals(vnPayResponseCode)) {
                imgStatus.setImageResource(android.R.drawable.checkbox_on_background);
                tvStatus.setText("Thanh toán thành công!");
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                // Xóa hết item trong giỏ hàng
                clearCartItems();
            } else {
                imgStatus.setImageResource(android.R.drawable.ic_delete);
                tvStatus.setText("Thanh toán thất bại!");
                tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
            tvOrderId.setText("Mã đơn hàng: " + orderId);
            tvAmount.setText("Số tiền: " + amount + "đ");
            tvTransactionId.setText("Mã giao dịch: " + transactionId);
            tvPaymentMethod.setText("Phương thức: " + paymentMethod);
        } catch (Exception e) {
            tvStatus.setText("Không đọc được kết quả thanh toán!");
        }

        btnBackHome.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void clearCartItems() {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAuthorizationHeader();
        if (token == null) return;
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getCart(token).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    List<CartItem> items = response.body().getValue().getCartItems();
                    for (CartItem item : items) {
                        api.deleteCartItem(token, item.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {}
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {}
                        });
                    }
                    Toast.makeText(PaymentResultActivity.this, "Đã xóa giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {}
        });
    }
} 