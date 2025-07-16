package com.example.koreanshopee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.BookViewHolder> {

    private Context context;
    private List<Item> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
        void onAddToCartClick(Item item);
        void onReviewClick(Item item);
    }

    public ItemAdapter(Context context, List<Item> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.tvTitle.setText(item.getName());
        holder.tvPrice.setText(item.getPrice() + "đ");

        // Set click listeners if listener is available
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
            if (holder.btnAddToCart != null) {
                holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(item));
            }
            if (holder.btnReview != null) {
                holder.btnReview.setOnClickListener(v -> listener.onReviewClick(item));
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice;
        ImageView imgBook;
        ImageView btnAddToCart, btnReview;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgBook = itemView.findViewById(R.id.imgBook);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnReview = itemView.findViewById(R.id.btnReview);
        }
    }
}
