package com.example.koreanshopee.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.CartItem;
import com.example.koreanshopee.model.Product;
import com.example.koreanshopee.utils.TokenManager;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface OnCartChangeListener {
        void onQuantityChanged();
        void onDeleteItem(int position);
    }

    private List<CartItem> cartItems;
    private Map<String, Product> productMap;
    private Context context;
    private OnCartChangeListener listener;
    private TokenManager tokenManager;
    private ApiService apiService;

    public CartAdapter(Context context, List<CartItem> cartItems, Map<String, Product> productMap, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.productMap = productMap;
        this.listener = listener;
        this.tokenManager = new TokenManager(context);
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Product product = productMap.get(item.getProductId());
        if (product == null) return;

        holder.tvProductName.setText(product.getName());
        Log.d("Name", "Name " + product.getName());
        holder.tvProductType.setText(product.getCategoryname());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        String imageUrl = null;
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            Object imgObj = product.getProductImages().get(0);
            if (imgObj instanceof String) {
                imageUrl = (String) imgObj;
            } else if (imgObj != null) {
                imageUrl = imgObj.toString(); // fallback nếu là object chứa url
            }
        }
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgProduct);

        holder.btnMinus.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                CartItem currentItem = cartItems.get(currentPosition);
                if (currentItem.getQuantity() > 1) {
                    int newQuantity = currentItem.getQuantity() - 1;
                    updateCartItemQuantity(currentItem, newQuantity, currentPosition, holder);
                    listener.onQuantityChanged();
                }
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                CartItem currentItem = cartItems.get(currentPosition);
                int newQuantity = currentItem.getQuantity() + 1;
                updateCartItemQuantity(currentItem, newQuantity, currentPosition, holder);
                listener.onQuantityChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                CartItem currentItem = cartItems.get(currentPosition);
                String token = tokenManager.getAuthorizationHeader();
                if (token == null) {
                    Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                apiService.deleteCartItem(token, currentItem.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            cartItems.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            listener.onDeleteItem(currentPosition);
                            Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductType, btnMinus, btnPlus, tvQuantity;
        ImageView btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imageProduct);
            tvProductName = itemView.findViewById(R.id.textProductName);
            tvProductType = itemView.findViewById(R.id.textProductPrice);
            tvQuantity = itemView.findViewById(R.id.textQuantity);
            btnMinus = itemView.findViewById(R.id.decrease);
            btnPlus = itemView.findViewById(R.id.increase);
            btnRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price);
    }

    private void updateCartItemQuantity(CartItem item, int newQuantity, int position, CartViewHolder holder) {
        String token = tokenManager.getAuthorizationHeader();
        if (token == null) {
            Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        apiService.updateCartItemQuantity(token, item.getId(), newQuantity).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    item.setQuantity(newQuantity);
                    notifyItemChanged(position);
                    listener.onQuantityChanged();
                } else {
                    Toast.makeText(context, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 