package com.example.koreanshopee.OrderHistory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.koreanshopee.Fragment.DeliveredFragment;
import com.example.koreanshopee.Fragment.PendingDeliveryFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {

    public OrderPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PendingDeliveryFragment();
            case 1:
                return new DeliveredFragment();
            default:
                return new DeliveredFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

