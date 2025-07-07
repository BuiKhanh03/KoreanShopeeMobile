package com.example.koreanshopee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.koreanshopee.api.ApiClient;
import com.example.koreanshopee.api.ApiService;
import com.example.koreanshopee.model.UpdateProfileRequest;
import com.example.koreanshopee.model.UserProfile;
import com.example.koreanshopee.model.UserProfileResponse;
import com.example.koreanshopee.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

public class ProfileCustomer extends AppCompatActivity {

    private static final String TAG = "ProfileCustomer";
    private ImageView btnBack;
    private Button btnUpdate;
    private EditText etFirstName, etLastName, etPhone, etEmail;
    private ProgressBar progressBar;
    private ImageView ivAvatar;
    
    private TokenManager tokenManager;
    private ApiService apiService;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_customer);
        
        // Initialize TokenManager and ApiService
        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);
        
        // Initialize views
        etFirstName = findViewById(R.id.ufirstname);
        etLastName = findViewById(R.id.ulastname);
        etPhone = findViewById(R.id.uphone);
        etEmail = findViewById(R.id.uemail);
        progressBar = findViewById(R.id.uProgress);
        ivAvatar = findViewById(R.id.uavatar);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Set click listener for avatar to upload image
        ivAvatar.setOnClickListener(v -> selectImageFromGallery());

        // Set click listener for update info button
        btnUpdate.setOnClickListener(v -> updateUserInfo());

        // Load user profile
        loadUserProfile();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUserProfile() {
        
        String authHeader = tokenManager.getAuthorizationHeader();
        
        if (authHeader == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Show loading
        progressBar.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Đang tải thông tin...", Toast.LENGTH_SHORT).show();

        Call<UserProfileResponse> call = apiService.getUserProfile(authHeader);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UserProfile userProfile = response.body().getValue();

                    
                    if (userProfile != null) {
                        Log.d(TAG, "User data - FullName: " + userProfile.getFullName() + 
                                   ", UserName: " + userProfile.getUserName() + 
                                   ", Email: " + userProfile.getEmail() + 
                                   ", Phone: " + userProfile.getPhoneNumber());
                        displayUserProfile(userProfile);
                        Toast.makeText(ProfileCustomer.this, "Đã tải thông tin thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileCustomer.this, "Không thể lấy thông tin user", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "UserProfile is null");
                    }
                } else {
                    String errorMsg = "Lỗi: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                    Toast.makeText(ProfileCustomer.this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileCustomer.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserProfile(UserProfile userProfile) {
        Log.d(TAG, "displayUserProfile started");
        // Display first name
        String firstName = userProfile.getFirstName();
        if (firstName != null && !firstName.trim().isEmpty() && !firstName.equals("string")) {
            etFirstName.setText(firstName);
        } else {
            etFirstName.setText("");
        }
        // Display last name
        String lastName = userProfile.getLastName();
        if (lastName != null && !lastName.trim().isEmpty() && !lastName.equals("string")) {
            etLastName.setText(lastName);
        } else {
            etLastName.setText("");
        }
        // Display phone number
        String phoneNumber = userProfile.getPhoneNumber();
        if (phoneNumber != null && !phoneNumber.equals("string")) {
            Log.d(TAG, "Setting phone number: " + phoneNumber);
            etPhone.setText(phoneNumber);
        } else {
            Log.d(TAG, "Phone number is null or 'string', setting empty");
            etPhone.setText("");
        }
        // Display email
        String email = userProfile.getEmail();
        if (email != null) {
            Log.d(TAG, "Setting email: " + email);
            etEmail.setText(email);
        } else {
            Log.d(TAG, "Email is null, setting empty");
            etEmail.setText("");
        }
        // Hiển thị avatar
        String imageLink = userProfile.getImageLink();
        if (imageLink != null && !imageLink.equals("N/A") && !imageLink.isEmpty()) {
            Glide.with(this)
                .load(imageLink)
                .placeholder(R.drawable.ic_userr)
                .error(R.drawable.ic_userr)
                .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_userr);
        }
        Log.d(TAG, "displayUserProfile completed");
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivAvatar.setImageURI(imageUri);
            uploadAvatar(imageUri);
        }
    }

    private void uploadAvatar(Uri uri) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            // Convert URI to File
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(inputStream);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), bytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "avatar.jpg", requestFile);
            String authHeader = tokenManager.getAuthorizationHeader();
            Call<ResponseBody> call = apiService.uploadAvatar(authHeader, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseString = response.body().string();
                            // Giả sử response là {"imageUrl":"https://..."}
                            JSONObject jsonObject = new JSONObject(responseString);
                            String imageUrl = jsonObject.getString("imageUrl");
                            Glide.with(ProfileCustomer.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_userr)
                                .error(R.drawable.ic_userr)
                                .into(ivAvatar);
                            Toast.makeText(ProfileCustomer.this, "Tải ảnh lên thành công!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(ProfileCustomer.this, "Lỗi đọc link ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileCustomer.this, "Lỗi tải ảnh: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileCustomer.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Lỗi chọn ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // Add updateUserInfo method to call API and update info
    private void updateUserInfo() {
        String authHeader = tokenManager.getAuthorizationHeader();
        if (authHeader == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        UpdateProfileRequest request = new UpdateProfileRequest(firstName, lastName, phone);
        Call<UserProfileResponse> call = apiService.updateUserProfile(authHeader, request);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProfileCustomer.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadUserProfile();
                } else {
                    Toast.makeText(ProfileCustomer.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileCustomer.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}