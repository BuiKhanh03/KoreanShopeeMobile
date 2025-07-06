package com.example.koreanshopee.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.koreanshopee.InfoApp;
import com.example.koreanshopee.ui.auth.LoginActivity;
import com.example.koreanshopee.auth.LogoutHelper;
import com.example.koreanshopee.OrderHistory.OrderHistory;
import com.example.koreanshopee.ProfileCustomer;
import com.example.koreanshopee.R;
import com.example.koreanshopee.ReviewOrder;

public class AccountFragment extends Fragment {

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán sự kiện click cho từng nút
        LinearLayout btnAccountInfo = view.findViewById(R.id.btn_account_info);
        LinearLayout btnOrderHistory = view.findViewById(R.id.btn_order_history);
        LinearLayout btnPaymentHistory = view.findViewById(R.id.btn_order_review);
        LinearLayout btnInfoApp = view.findViewById(R.id.btn_app_info);
        LinearLayout btnLogout = view.findViewById(R.id.btn_logout);

        // Debug: Kiểm tra xem có tìm thấy nút logout không
        if (btnLogout != null) {
            Toast.makeText(getActivity(), "Tìm thấy nút logout!", Toast.LENGTH_SHORT).show();
            // Thêm background màu để dễ nhìn thấy
            btnLogout.setBackgroundColor(0xFFFFE0E0); // Màu hồng nhạt
        } else {
            Toast.makeText(getActivity(), "KHÔNG tìm thấy nút logout!", Toast.LENGTH_LONG).show();
        }

        btnAccountInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileCustomer.class);
            startActivity(intent);
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderHistory.class);
            startActivity(intent);
        });

        btnPaymentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReviewOrder.class);
            startActivity(intent);
        });

        btnInfoApp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InfoApp.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Đang logout...", Toast.LENGTH_SHORT).show();
            LogoutHelper.logout(getActivity());
        });
    }
}
