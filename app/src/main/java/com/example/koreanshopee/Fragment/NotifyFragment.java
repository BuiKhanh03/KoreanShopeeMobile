package com.example.koreanshopee.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.ui.main.NotificationAdapter;
import com.example.koreanshopee.ui.main.ItemNotification;
import com.example.koreanshopee.R;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<ItemNotification> notificationList;

    public NotifyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fake dữ liệu demo như ảnh
        notificationList = new ArrayList<>();
        notificationList.add(new ItemNotification(
                "‼️ Yen ơi, deal \"xịn\" từ VPBank không đùa được đâu ! 😱",
                "😍 VPBank Giảm Ngay 20% Tối Đa 50.000đ - Nhập Mã: VPBDAYFHS\n🔥 Số lượng có hạn, áp dụng duy nhất hôm nay 15.06\n👉 Áp dụng cho khách hàng thanh toán bằng thẻ VPBank qua Zalopay.",
                "14/06/2025"
        ));
        notificationList.add(new ItemNotification(
                "🎁 15/06 - SĂN MÃ GIẢM 94.000Đ",
                "🌞 Từ 0H ngày 15/06, mã giảm 94K cho đơn từ 100K\n🕐 Khung giờ: 0H - 7H - 9H - 12H - 15H - 17H - 18H - 19H - 21H\n📌 Hẹn lịch săn nha Yen ơiiiii!",
                "14/06/2025"
        ));
        notificationList.add(new ItemNotification(
                "🔥 15.6 - SIÊU SALE GIỮA THÁNG - Sắp On Air!",
                "🥳 Tiệc sale bung xõa, hốt quà thả ga!\n🎁 Giảm 94K cho đơn 100K\n🎁 Giảm 25K cho đơn 200K\n📚 Sách đồng giá 6K, 66K\n🔥 Deal cực hời!",
                "14/06/2025"
        ));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
