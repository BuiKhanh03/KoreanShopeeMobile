package com.example.koreanshopee.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.model.CartResponse;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.model.ProductDetailResponse;
import com.example.koreanshopee.ui.main.CartAdapter;
import com.example.koreanshopee.ui.main.ChatActivity;
import com.example.koreanshopee.ui.main.ConfirmOrderActivity;
import com.example.koreanshopee.utils.TokenManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView recyclerViewCart;
    private TextView tvTotal, tvEmptyCart;
    private Button btnCheckout;
    private ImageView tickAll, btnChat;
    private TokenManager tokenManager;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private Map<String, Product> productMap = new HashMap<>();

    public CartFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);
        btnChat = view.findViewById(R.id.chat);
        tickAll = view.findViewById(R.id.tick_all);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext(), cartItems, productMap, new CartAdapter.OnCartChangeListener() {
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

        updateTotalPrice();
        tickAll.setOnClickListener(v -> toggleSelectAll());

        tokenManager = new TokenManager(requireContext());
        loadCart();

        btnCheckout.setOnClickListener(v -> gotoConfirmOrder());
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadCart() {
        String authHeader = tokenManager.getAuthorizationHeader();
        if (authHeader == null) {
            Toast.makeText(getContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
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
                Toast.makeText(getContext(), "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
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
                    checkLoadComplete(++loaded[0]);
                }

                @Override
                public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                    checkLoadComplete(++loaded[0]);
                }
            });
        }
    }

    private void checkLoadComplete(int loadedCount) {
        if (loadedCount == cartItems.size()) {
            adapter.notifyDataSetChanged();
            updateTotal();
            updateEmptyCartView();
        }
    }

    private void updateTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPriceAtTime() * item.getQuantity();
        }
        tvTotal.setText("Tổng cộng: " + String.format("%,dđ", total));
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

    private void gotoConfirmOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
        intent.putExtra("orderItems", new ArrayList<>(cartItems));
        intent.putExtra("productMap", new HashMap<>(productMap));
        startActivity(intent);
    }
    private void toggleSelectAll() {
        boolean allChecked = true;
        for (CartItem item : cartItems) {
            if (!item.isChecked()) {
                allChecked = false;
                break;
            }
        }

        boolean newCheckState = !allChecked;

        for (CartItem item : cartItems) {
            item.setChecked(newCheckState);
        }

        tickAll.setImageResource(newCheckState ? R.drawable.ic_ticked : R.drawable.ic_tick);
        tickAll.setColorFilter(
                ContextCompat.getColor(requireContext(), newCheckState ? R.color.blue800 : R.color.white),
                PorterDuff.Mode.SRC_IN
        );


        adapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            if (item.isChecked()) {
                total += item.getDiscountedPrice() * item.getQuantity();
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(total).replace(",", ".") + "đ";
        String text = "Tổng tiền: " + formatted;

        SpannableString spannable = new SpannableString(text);

        int start = text.indexOf(formatted);
        int end = text.length();

        spannable.setSpan(
                new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new android.text.style.RelativeSizeSpan(1.2f),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tvTotal.setText(spannable);
    }
}
