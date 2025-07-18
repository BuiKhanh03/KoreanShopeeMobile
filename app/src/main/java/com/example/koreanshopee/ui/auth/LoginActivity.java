package com.example.koreanshopee.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.koreanshopee.R;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.utils.TokenManager;
import com.example.koreanshopee.model.LoginRequest;
import com.example.koreanshopee.model.LoginResponse;
import com.example.koreanshopee.LayoutScreen;
import com.example.koreanshopee.ui.seller.SellerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView btnSignup, btnForgot;
    EditText inputEmail, inputPassword;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize TokenManager
        tokenManager = new TokenManager(this);

        // Check if user is already logged in
        if (tokenManager.isLoggedIn()) {
            // User is already logged in, check role and redirect
            checkUserRoleAndRedirect();
            return;
        }

        // Initialize views
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnForgot = findViewById(R.id.btn_forgot_password);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void checkUserRoleAndRedirect() {
        String role = tokenManager.getUserRole();
        if (role != null && role.equalsIgnoreCase("Seller")) {
            startActivity(new Intent(LoginActivity.this, SellerActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, LayoutScreen.class));
        }
        finish();
    }
    private void performLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            inputEmail.setError("Vui lòng nhập email");
            return;
        }

        if (password.isEmpty()) {
            inputPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        // Show loading state
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        // Create login request
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call API
        ApiClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("ĐĂNG NHẬP");

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    String accessToken = loginResponse.getAccessToken();
                    tokenManager.saveToken(accessToken);

// Giải mã JWT để lấy role
                    String[] parts = accessToken.split("\\.");
                    if (parts.length == 3) {
                        try {
                            String payload = new String(
                                    Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING),
                                    "UTF-8"
                            );

                            JSONObject jsonObject = new JSONObject(payload);
                            JSONArray roles = jsonObject.getJSONArray("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");

                            String role = roles.getString(0); // Lấy role đầu tiên: "Seller", "SELLER", etc.
                            tokenManager.saveUserRole(role);  // ✅ lưu lại

                            Toast.makeText(LoginActivity.this, "Đăng nhập với quyền: " + role, Toast.LENGTH_SHORT).show();

                            // Redirect theo role
                            if ("Seller".equalsIgnoreCase(role)) {
                                startActivity(new Intent(LoginActivity.this, SellerActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, LayoutScreen.class));
                            }

                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Không đọc được role từ token", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Handle error response
                    String errorMessage = "Đăng nhập thất bại";
                    if (response.code() == 401) {
                        errorMessage = "Email hoặc mật khẩu không đúng";
                    } else if (response.code() == 400) {
                        errorMessage = "Dữ liệu không hợp lệ";
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("ĐĂNG NHẬP");
                
                // Handle network error
                String errorMessage = "Lỗi kết nối mạng";
                if (t.getMessage() != null && t.getMessage().contains("localhost")) {
                    errorMessage = "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.";
                }
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
} 