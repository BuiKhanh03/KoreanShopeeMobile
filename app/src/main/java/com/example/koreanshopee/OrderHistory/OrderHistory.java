package com.example.koreanshopee.OrderHistory;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.koreanshopee.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderHistory extends AppCompatActivity {

    ImageView btnBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrderPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new OrderPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chờ giao hàng");
                    break;
                case 1:
                    tab.setText("Đã giao");
                    break;
            }
        }).attach();
    }
}
