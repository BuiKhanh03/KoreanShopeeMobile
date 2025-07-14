package com.example.koreanshopee.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%,dđ", product.getPrice()));
        holder.tvBrand.setText(product.getBrand());
        // Hiển thị ảnh nếu có
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            // TODO: Load ảnh bằng Glide nếu có link
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvBrand;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvBrand = itemView.findViewById(R.id.tvProductBrand);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
} 