package com.example.koreanshopee.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.OrderRequest;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.utils.TokenManager;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.Glide;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.VnpayRequest;
import com.example.koreanshopee.model.VnpayResponse;
import com.example.koreanshopee.model.OrderResponse;
import android.content.Intent;

public class ConfirmOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrderItems;
    private EditText edtShippingAddress;
    private Button btnConfirmOrder;
    private List<CartItem> orderItems = new ArrayList<>();
    private OrderItemAdapter adapter;
    private TokenManager tokenManager;
    private TextView tvShippingFee, tvTotalOrder;
    private Map<String, Product> productMap = new HashMap<>();
    private final int shippingFee = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        edtShippingAddress = findViewById(R.id.edtShippingAddress);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        tokenManager = new TokenManager(this);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalOrder = findViewById(R.id.tvTotalOrder);

        // Nhận orderItems và productMap từ Intent
        if (getIntent() != null && getIntent().hasExtra("orderItems")) {
            orderItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("orderItems");
        }
        if (getIntent() != null && getIntent().hasExtra("productMap")) {
            productMap = (HashMap<String, Product>) getIntent().getSerializableExtra("productMap");
        }
        adapter = new OrderItemAdapter(orderItems, productMap);
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrderItems.setAdapter(adapter);
        updateTotalOrder();

        btnConfirmOrder.setOnClickListener(v -> confirmOrder());
    }

    private void confirmOrder() {
        String address = edtShippingAddress.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = tokenManager.getAuthorizationHeader();
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        List<OrderRequest.OrderItemRequest> orderItemRequests = new ArrayList<>();
        for (CartItem item : orderItems) {
            orderItemRequests.add(new OrderRequest.OrderItemRequest(item.getProductId(), item.getQuantity()));
        }
        OrderRequest orderRequest = new OrderRequest(address, orderItemRequests);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        btnConfirmOrder.setEnabled(false);
        api.createOrder(token, orderRequest).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                btnConfirmOrder.setEnabled(true);
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    // Lấy paymentId từ response
                    String paymentId = null;
                    if (response.body().getValue().getPaymentRecord() != null) {
                        paymentId = response.body().getValue().getPaymentRecord().getId();
                    }
                    Log.d("OrderCreated", "Payment ID: " + paymentId);
                    if (paymentId != null && !paymentId.isEmpty()) {
                        // Xóa hết item trong giỏ hàng trước khi gọi VNPAY
                        clearCartItems();
                        // Gọi API VNPAY với paymentId
                        callVnpayApi(token, paymentId);
                    } else {
                        Toast.makeText(ConfirmOrderActivity.this, "Không lấy được paymentId!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ConfirmOrderActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                btnConfirmOrder.setEnabled(true);
                Toast.makeText(ConfirmOrderActivity.this, "Lỗi kết nối!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callVnpayApi(String token, String paymentId) {
        // Lấy thông tin đơn hàng để truyền sang VNPAY
        double total = 0;
        StringBuilder productNames = new StringBuilder();
        for (CartItem item : orderItems) {
            Product product = productMap.get(item.getProductId());
            int price = (product != null) ? product.getPrice() : 0;
            total += price * item.getQuantity();
            if (product != null) {
                if (productNames.length() > 0) productNames.append(", ");
                productNames.append(product.getName());
            }
        }
        total += shippingFee;
        String orderType = productNames.length() > 0 ? productNames.toString() : "ShopeeOrder";
        String orderDescription = "Thanh toán đơn hàng Shopee";
        // Sử dụng orderId thật từ backend thay vì UUID.randomUUID()
        Log.d("Total", "total: " + total);
        Log.d("OrderId", "orderId: " + paymentId);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.createVnpayPayment(token, orderType, total, orderDescription, paymentId).enqueue(new Callback<VnpayResponse>() {
            @Override
            public void onResponse(Call<VnpayResponse> call, Response<VnpayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentUrl = response.body().getPaymentUrl();
                    if (paymentUrl != null && paymentUrl.startsWith("koreanshopee://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(paymentUrl));
                        startActivity(intent);
                        finish();
                    } else if (paymentUrl != null && (paymentUrl.startsWith("http://") || paymentUrl.startsWith("https://"))) {
                        Intent intent = new Intent(ConfirmOrderActivity.this, VnpayWebViewActivity.class);
                        intent.putExtra("paymentUrl", paymentUrl);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ConfirmOrderActivity.this, "Không lấy được link thanh toán VNPAY!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ConfirmOrderActivity.this, "Không lấy được link thanh toán VNPAY!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<VnpayResponse> call, Throwable t) {
                Toast.makeText(ConfirmOrderActivity.this, "Lỗi kết nối VNPAY!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTotalOrder() {
        int total = 0;
        for (CartItem item : orderItems) {
            Product product = productMap.get(item.getProductId());
            int price = (product != null) ? product.getPrice() : 0;
            total += price * item.getQuantity();
        }
        total += shippingFee;
        tvShippingFee.setText("Phí vận chuyển: " + String.format("%,dđ", shippingFee));
        tvTotalOrder.setText("Tổng cộng: " + String.format("%,dđ", total));
    }

    private void clearCartItems() {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAuthorizationHeader();
        if (token == null) return;
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getCart(token).enqueue(new retrofit2.Callback<com.example.koreanshopee.model.CartResponse>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.koreanshopee.model.CartResponse> call, retrofit2.Response<com.example.koreanshopee.model.CartResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getValue() != null) {
                    java.util.List<com.example.koreanshopee.model.CartItem> items = response.body().getValue().getCartItems();
                    for (com.example.koreanshopee.model.CartItem item : items) {
                        api.deleteCartItem(token, item.getId()).enqueue(new retrofit2.Callback<Void>() {
                            @Override
                            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {}
                            @Override
                            public void onFailure(retrofit2.Call<Void> call, Throwable t) {}
                        });
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<com.example.koreanshopee.model.CartResponse> call, Throwable t) {}
        });
    }

    // Adapter hiển thị danh sách sản phẩm đặt hàng
    public static class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
        private List<CartItem> items;
        private Map<String, Product> productMap;
        public OrderItemAdapter(List<CartItem> items, Map<String, Product> productMap) {
            this.items = items;
            this.productMap = productMap;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(parent.getContext(), R.layout.item_order_product, null);
            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CartItem item = items.get(position);
            Product product = productMap.get(item.getProductId());
            if (product != null) {
                holder.tvProductName.setText(product.getName());
                holder.tvProductPrice.setText(String.format("%,dđ", product.getPrice()));
                if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
                    Object imgObj = product.getProductImages().get(0);
                    String imageUrl = (imgObj instanceof String) ? (String) imgObj : imgObj.toString();
                    Glide.with(holder.imgProduct.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.imgProduct);
                } else {
                    holder.imgProduct.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                holder.tvProductName.setText(item.getProductId());
                holder.tvProductPrice.setText("");
                holder.imgProduct.setImageResource(R.drawable.ic_launcher_foreground);
            }
            holder.tvProductQuantity.setText("Số lượng: " + item.getQuantity());
        }
        @Override
        public int getItemCount() { return items.size(); }
        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgProduct;
            TextView tvProductName, tvProductPrice, tvProductQuantity;
            public ViewHolder(View itemView) {
                super(itemView);
                imgProduct = itemView.findViewById(R.id.imgProduct);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
                tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            }
        }
    }
} 