package com.example.koreanshopee;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.koreanshopee.Fragment.SettingFragment;
import com.example.koreanshopee.Fragment.CartFragment;
import com.example.koreanshopee.Fragment.HomeFragment;
import com.example.koreanshopee.Fragment.NotifyFragment;
import com.example.koreanshopee.Fragment.ShopsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LayoutScreen extends AppCompatActivity {

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
                } else if (itemId == R.id.nav_cart) {
                    selectedFragment = new CartFragment();
                } else if (itemId == R.id.nav_settings) {
                    selectedFragment = new SettingFragment();
                } else if (itemId == R.id.nav_notify) {
                    selectedFragment = new NotifyFragment();
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
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        String destination = getIntent().getStringExtra("navigateTo");

        if ("cart".equals(destination)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CartFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_cart);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
}
