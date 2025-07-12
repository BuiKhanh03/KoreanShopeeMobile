package com.example.koreanshopee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.koreanshopee.Fragment.AccountFragment;
import com.example.koreanshopee.Fragment.HomeFragment;
import com.example.koreanshopee.Fragment.NotifyFragment;
import com.example.koreanshopee.Fragment.SettingFragment;
import com.example.koreanshopee.Fragment.ShopsFragment;
import com.example.koreanshopee.ui.main.CartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class layout_sceen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_sceen);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_shops) {
                    selectedFragment = new ShopsFragment();
                } else if (itemId == R.id.nav_notify) {
                    selectedFragment = new NotifyFragment();
                } else if (itemId == R.id.nav_account) {
                    selectedFragment = new AccountFragment();
                } else if (itemId == R.id.nav_setting) {
                    Intent intent = new Intent(layout_sceen.this, CartActivity.class);
                    startActivity(intent);
                    return true;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }

                return false;
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //auto vào home khi mở app
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
