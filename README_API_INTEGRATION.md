# Hướng dẫn tích hợp API Authentication

## Tổng quan
Ứng dụng đã được tích hợp với API authentication để đăng nhập và lưu trữ token. Hệ thống sử dụng Retrofit để gọi API và SharedPreferences để lưu trữ token.

## Các file đã tạo/cập nhật

### 1. Dependencies (app/build.gradle.kts)
- Retrofit: Để gọi API
- OkHttp: Để xử lý HTTP requests
- SharedPreferences: Để lưu trữ token

### 2. API Classes
- `ApiService.java`: Interface định nghĩa các API endpoints
- `ApiClient.java`: Class cấu hình Retrofit và tạo API service
- `LoginRequest.java`: Model cho request đăng nhập
- `LoginResponse.java`: Model cho response đăng nhập

### 3. Authentication Classes
- `TokenManager.java`: Quản lý việc lưu trữ và truy xuất token
- `AuthInterceptor.java`: Tự động thêm token vào header của API calls
- `LogoutHelper.java`: Xử lý việc logout và xóa token

### 4. Updated Activities
- `Login.java`: Tích hợp API đăng nhập
- `MainActivity.java`: Kiểm tra trạng thái đăng nhập và logout

## Cách sử dụng

### 1. Đăng nhập
```java
// Trong Login.java đã được tích hợp sẵn
// Khi user nhập email/password và nhấn đăng nhập:
LoginRequest loginRequest = new LoginRequest(email, password);
ApiClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            // Lưu token
            tokenManager.saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
            // Chuyển đến MainActivity
        }
    }
    
    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        // Xử lý lỗi
    }
});
```

### 2. Gọi API có xác thực
```java
// Sử dụng authenticated API service để tự động thêm token
ApiService apiService = ApiClient.getAuthenticatedApiService(context);

// Ví dụ: gọi API lấy thông tin user
apiService.getUserProfile().enqueue(new Callback<UserProfile>() {
    @Override
    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
        if (response.isSuccessful()) {
            // Xử lý dữ liệu
        } else if (response.code() == 401) {
            // Token hết hạn, logout user
            LogoutHelper.logout(context);
        }
    }
    
    @Override
    public void onFailure(Call<UserProfile> call, Throwable t) {
        // Xử lý lỗi
    }
});
```

### 3. Gọi API không cần xác thực
```java
// Sử dụng public API service
ApiService apiService = ApiClient.getApiService();

// Ví dụ: gọi API đăng ký
apiService.signup(signupRequest).enqueue(new Callback<SignupResponse>() {
    @Override
    public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
        // Xử lý response
    }
    
    @Override
    public void onFailure(Call<SignupResponse> call, Throwable t) {
        // Xử lý lỗi
    }
});
```

### 4. Kiểm tra trạng thái đăng nhập
```java
TokenManager tokenManager = new TokenManager(context);
if (tokenManager.isLoggedIn()) {
    // User đã đăng nhập
} else {
    // User chưa đăng nhập
}
```

### 5. Logout
```java
LogoutHelper.logout(context);
```

## Thêm API endpoints mới

### 1. Thêm vào ApiService.java
```java
@GET("api/user/profile")
Call<UserProfile> getUserProfile();

@POST("api/user/update")
Call<UpdateResponse> updateProfile(@Body UpdateRequest request);
```

### 2. Tạo model classes
```java
public class UserProfile {
    private String name;
    private String email;
    // getters and setters
}

public class UpdateRequest {
    private String name;
    // getters and setters
}
```

### 3. Sử dụng trong Activity/Fragment
```java
ApiClient.getAuthenticatedApiService(context)
    .getUserProfile()
    .enqueue(new Callback<UserProfile>() {
        // Handle response
    });
```

## Cấu hình

### 1. Base URL
Hiện tại đang sử dụng: `http://localhost:32768/`
Để thay đổi, cập nhật trong `ApiClient.java`:
```java
private static final String BASE_URL = "your_new_base_url";
```

### 2. Network Security
Đã thêm `android:usesCleartextTraffic="true"` trong AndroidManifest.xml để cho phép kết nối HTTP.

## Lưu ý quan trọng

1. **Token Storage**: Token được lưu trong SharedPreferences, đảm bảo bảo mật cho production app.

2. **Error Handling**: Hệ thống tự động xử lý lỗi 401 (Unauthorized) bằng cách logout user.

3. **Network Calls**: Tất cả API calls đều được thực hiện bất đồng bộ để không block UI thread.

4. **Logging**: Đã bật HTTP logging để debug, có thể tắt trong production.

## Testing

1. Chạy ứng dụng
2. Nhập email/password hợp lệ
3. Nhấn đăng nhập
4. Kiểm tra token được lưu trong SharedPreferences
5. Test logout functionality

## Troubleshooting

1. **Lỗi kết nối localhost**: Đảm bảo server đang chạy trên port 32768
2. **Token không được lưu**: Kiểm tra SharedPreferences implementation
3. **API calls thất bại**: Kiểm tra network permissions và base URL 