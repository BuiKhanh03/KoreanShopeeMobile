# Hướng dẫn Flow Chuyển Màn Hình

## Tổng quan
Ứng dụng KoreanShopee có flow chuyển màn hình như sau:

## 🚀 Flow Chuyển Màn Hình

### 1. **Khởi động ứng dụng**
```
App Launch → LoginActivity (nếu chưa đăng nhập)
App Launch → layout_sceen (nếu đã đăng nhập)
```

### 2. **Flow đăng nhập**
```
LoginActivity → API Login → layout_sceen (màn hình chính)
```

### 3. **Flow đăng xuất**
```
Bất kỳ màn hình nào → LogoutHelper.logout() → LoginActivity
```

### 4. **Màn hình chào mừng (MainActivity)**
```
MainActivity → LoginActivity (nếu chưa đăng nhập)
MainActivity → layout_sceen (nếu đã đăng nhập, nhấn "ĐI ĐẾN MÀN HÌNH CHÍNH")
```

## 📱 Các màn hình chính

### 1. **LoginActivity** (`ui.auth.LoginActivity`)
- **Chức năng**: Màn hình đăng nhập
- **Chuyển đến**: 
  - `layout_sceen` (sau khi đăng nhập thành công)
  - `SignupActivity` (nhấn đăng ký)
  - `ForgotPasswordActivity` (nhấn quên mật khẩu)

### 2. **layout_sceen** (Màn hình chính)
- **Chức năng**: Màn hình chính với bottom navigation
- **Các tab**:
  - Home (HomeFragment)
  - Shops (ShopsFragment)
  - Notify (NotifyFragment)
  - Account (AccountFragment)
  - Setting (SettingFragment)

### 3. **MainActivity** (`ui.main.MainActivity`)
- **Chức năng**: Màn hình chào mừng
- **Chuyển đến**:
  - `LoginActivity` (nếu chưa đăng nhập)
  - `layout_sceen` (nếu đã đăng nhập, nhấn "ĐI ĐẾN MÀN HÌNH CHÍNH")

## 🔄 Cách thay đổi flow

### 1. **Thay đổi màn hình sau đăng nhập**
```java
// Trong LoginActivity.java, method performLogin()
if (response.isSuccessful() && response.body() != null) {
    // Save tokens
    tokenManager.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
    
    // Chuyển đến màn hình mong muốn
    Intent intent = new Intent(LoginActivity.this, TargetActivity.class);
    startActivity(intent);
    finish();
}
```

### 2. **Thay đổi màn hình sau logout**
```java
// Trong LogoutHelper.java
public static void logout(Context context) {
    // Clear tokens
    TokenManager tokenManager = new TokenManager(context);
    tokenManager.clearTokens();
    
    // Chuyển đến màn hình mong muốn
    Intent intent = new Intent(context, TargetActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
}
```

### 3. **Thêm màn hình mới vào flow**
```java
// 1. Tạo Activity mới
public class NewActivity extends AppCompatActivity {
    // Implementation
}

// 2. Thêm vào AndroidManifest.xml
<activity android:name=".ui.newpackage.NewActivity" />

// 3. Chuyển đến từ màn hình khác
Intent intent = new Intent(CurrentActivity.this, NewActivity.class);
startActivity(intent);
```

## 🎯 Các trường hợp sử dụng

### 1. **User chưa đăng nhập**
```
LoginActivity → Nhập thông tin → API Login → layout_sceen
```

### 2. **User đã đăng nhập**
```
App Launch → Kiểm tra token → layout_sceen (tự động)
```

### 3. **User muốn đăng xuất**
```
Bất kỳ màn hình nào → Nhấn logout → LoginActivity
```

### 4. **User muốn xem màn hình chào mừng**
```
MainActivity → Nhấn "ĐI ĐẾN MÀN HÌNH CHÍNH" → layout_sceen
```

## 📋 Kiểm tra trạng thái đăng nhập

### 1. **Kiểm tra trong Activity**
```java
TokenManager tokenManager = new TokenManager(this);
if (tokenManager.isLoggedIn()) {
    // User đã đăng nhập
    // Chuyển đến màn hình chính
    Intent intent = new Intent(this, layout_sceen.class);
    startActivity(intent);
} else {
    // User chưa đăng nhập
    // Chuyển đến màn hình đăng nhập
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
}
```

### 2. **Kiểm tra trong Fragment**
```java
TokenManager tokenManager = new TokenManager(requireContext());
if (tokenManager.isLoggedIn()) {
    // User đã đăng nhập
} else {
    // User chưa đăng nhập
    // Có thể logout hoặc chuyển về login
    LogoutHelper.logout(requireContext());
}
```

## 🔧 Cấu hình AndroidManifest.xml

### 1. **Launcher Activity**
```xml
<activity android:name=".ui.auth.LoginActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### 2. **Các Activity khác**
```xml
<activity android:name=".ui.main.MainActivity" android:exported="false" />
<activity android:name=".layout_sceen" android:exported="false" />
<activity android:name=".ui.auth.SignupActivity" android:exported="false" />
<activity android:name=".ui.auth.ForgotPasswordActivity" android:exported="false" />
```

## ⚠️ Lưu ý quan trọng

### 1. **Token Management**
- Luôn kiểm tra token trước khi chuyển màn hình
- Clear token khi logout
- Tự động chuyển về login nếu token hết hạn

### 2. **Activity Flags**
- Sử dụng `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK` khi logout
- Sử dụng `finish()` để đóng Activity hiện tại

### 3. **Error Handling**
- Xử lý lỗi 401 (Unauthorized) bằng cách logout
- Hiển thị thông báo lỗi phù hợp cho user

## 🚀 Best Practices

### 1. **Consistent Navigation**
- Sử dụng cùng pattern cho tất cả màn hình
- Đặt tên Activity rõ ràng và nhất quán

### 2. **State Management**
- Sử dụng TokenManager để quản lý trạng thái đăng nhập
- Kiểm tra trạng thái trong onResume()

### 3. **User Experience**
- Hiển thị loading state khi chuyển màn hình
- Cung cấp feedback cho user (Toast, ProgressBar)
- Tránh chuyển màn hình quá nhanh 