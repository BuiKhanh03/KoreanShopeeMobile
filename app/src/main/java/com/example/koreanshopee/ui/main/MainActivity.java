package com.example.koreanshopee.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.koreanshopee.R;
import com.example.koreanshopee.auth.LogoutHelper;
import com.example.koreanshopee.auth.TokenManager;
import com.example.koreanshopee.layout_sceen;
import com.example.koreanshopee.ui.auth.LoginActivity;
import com.example.koreanshopee.ui.auth.SignupActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSignup, btnLogin, btnLogout, btnGoToMain;
    TextView tvWelcome;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize TokenManager
        tokenManager = new TokenManager(this);
        
        // Initialize views
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnLogout = findViewById(R.id.btn_logout);
        btnGoToMain = findViewById(R.id.btn_go_to_main);
        tvWelcome = findViewById(R.id.tv_welcome);

        // Check login status
        if (tokenManager.isLoggedIn()) {
            // User is logged in
            showLoggedInState();
        } else {
            // User is not logged in
            showLoggedOutState();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutHelper.logout(MainActivity.this);
                Toast.makeText(MainActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnGoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, layout_sceen.class);
                startActivity(intent);
            }
        });
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void showLoggedInState() {
        btnLogin.setVisibility(View.GONE);
        btnSignup.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
        btnGoToMain.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);
        tvWelcome.setText("Chào mừng bạn đã đăng nhập!");
    }
    
    private void showLoggedOutState() {
        btnLogin.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
        btnGoToMain.setVisibility(View.GONE);
        tvWelcome.setVisibility(View.GONE);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Check login status again when returning to this activity
        if (tokenManager.isLoggedIn()) {
            showLoggedInState();
        } else {
            showLoggedOutState();
        }
    }
} 