package com.example.koreanshopee.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.koreanshopee.R;
import com.example.koreanshopee.model.OrderHistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<OrderHistory> orders = new ArrayList<>();
    private OnOrderClickListener onOrderClickListener;
    public void setOrders(List<OrderHistory> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }
    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.onOrderClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHistory order = orders.get(position);
        holder.tvOrderId.setText("Mã đơn: #" + order.getId());
        String paymentStatus = order.getPaymentRecord() != null ? order.getPaymentRecord().getPaymentRecordStatus() : "";
        holder.tvOrderStatus.setText("Thanh toán: " + paymentStatus);
        holder.tvOrderTotal.setText("Tổng tiền: " + String.format("%,dđ", (int)order.getTotalAmount()));
        holder.tvOrderAddress.setText("Địa chỉ: " + order.getShippingAddress());
        holder.tvOrderDate.setText("Ngày tạo: " + formatDate(order.getCreateAt()));
        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order.getPaymentRecordId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvOrderTotal, tvOrderDate, tvOrderAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderAddress = itemView.findViewById(R.id.tvOrderAddress);
        }
    }
    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = isoFormat.parse(isoDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return outFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
    public interface OnOrderClickListener {
        void onOrderClick(String paymentRecordId);
    }
} 