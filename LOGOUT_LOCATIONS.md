# Các vị trí Logout trong ứng dụng KoreanShopee

## 📍 **Vị trí có nút Logout:**

### 1. **MainActivity** (Màn hình chào mừng)
- **Vị trí**: `ui.main.MainActivity`
- **Layout**: `activity_main.xml`
- **ID**: `btn_logout`
- **Hiển thị**: Chỉ hiện khi user đã đăng nhập
- **Chức năng**: 
  ```java
  btnLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          LogoutHelper.logout(MainActivity.this);
          Toast.makeText(MainActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
      }
  });
  ```

### 2. **AccountFragment** (Tab Account trong màn hình chính)
- **Vị trí**: `Fragment/AccountFragment.java`
- **Layout**: `fragment_account.xml`
- **ID**: `btn_logout`
- **Hiển thị**: Luôn hiện trong tab Account
- **Vị trí mới**: Ngay sau "Lịch sử đơn hàng", trước "Lịch sử đánh giá"
- **Chức năng**:
  ```java
  btnLogout.setOnClickListener(v -> {
      LogoutHelper.logout(getActivity());
  });
  ```

## 🎯 **Cách truy cập nút Logout:**

### **Cách 1: Từ màn hình chào mừng**
```
1. Mở app → MainActivity (màn hình chào mừng)
2. Nếu đã đăng nhập → Nút "ĐĂNG XUẤT" sẽ hiện
3. Nhấn "ĐĂNG XUẤT" → Logout và chuyển về LoginActivity
```

### **Cách 2: Từ màn hình chính (Vị trí mới)**
```
1. Đăng nhập → Chuyển đến layout_sceen (màn hình chính)
2. Nhấn tab "Account" (góc dưới bên phải)
3. Thấy thứ tự menu:
   - Hồ sơ của tôi
   - Lịch sử đơn hàng
   - Đăng xuất ← VỊ TRÍ MỚI (ngay đây)
   - Lịch sử đánh giá
   - Thông tin ứng dụng
4. Nhấn "Đăng xuất" → Logout và chuyển về LoginActivity
```

## 🔧 **Cách hoạt động của Logout:**

### **LogoutHelper.logout()**
```java
public static void logout(Context context) {
    // 1. Clear tokens
    TokenManager tokenManager = new TokenManager(context);
    tokenManager.clearTokens();
    
    // 2. Navigate back to login screen
    Intent intent = new Intent(context, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
}
```

### **Quá trình logout:**
1. **Clear tokens**: Xóa access token và refresh token
2. **Clear activity stack**: Xóa tất cả màn hình đã mở
3. **Chuyển về LoginActivity**: User phải đăng nhập lại

## 📱 **Giao diện nút Logout:**

### **MainActivity (Màn hình chào mừng)**
```xml
<Button
    android:id="@+id/btn_logout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    android:backgroundTint="@color/gray500"
    android:text="ĐĂNG XUẤT"
    android:textColor="@color/white"
    android:textSize="20sp"
    android:textStyle="bold"
    android:visibility="gone" />
```

### **AccountFragment (Tab Account) - Vị trí mới**
```xml
<LinearLayout
    android:id="@+id/btn_logout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="20dp"
    android:orientation="horizontal">

    <ImageView
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:layout_marginStart="30dp"
        android:src="@drawable/ic_logout" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="10dp"
        android:layout_marginTop="5dp"
        android:text="@string/log_out"
        android:textColor="@color/pink800"
        android:textSize="20sp"
        android:textStyle="bold" />
</LinearLayout>
```

## 🚀 **Hướng dẫn sử dụng:**

### **Cho User:**
1. **Từ màn hình chào mừng**: Nhấn "ĐĂNG XUẤT" (nếu đã đăng nhập)
2. **Từ màn hình chính**: 
   - Nhấn tab "Account" (góc dưới bên phải)
   - Thấy nút "Đăng xuất" ngay sau "Lịch sử đơn hàng"
   - Nhấn "Đăng xuất"

### **Cho Developer:**
```java
// Thêm logout vào bất kỳ màn hình nào
import com.example.koreanshopee.auth.LogoutHelper;

// Gọi logout
LogoutHelper.logout(context);
```

## ⚠️ **Lưu ý quan trọng:**

### **1. Auto-logout khi token hết hạn**
```java
// Trong API calls, nếu nhận được lỗi 401
if (response.code() == 401) {
    // Token hết hạn, tự động logout
    LogoutHelper.logout(context);
}
```

### **2. Clear activity stack**
- Sử dụng `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`
- Đảm bảo user không thể quay lại màn hình đã đăng nhập

### **3. Token management**
- Clear cả access token và refresh token
- User phải đăng nhập lại sau khi logout

## 📋 **Kiểm tra trạng thái:**

### **Build Status**
- ✅ Tất cả logout functions hoạt động đúng
- ✅ AccountFragment đã được cập nhật để sử dụng LogoutHelper
- ✅ MainActivity có nút logout hoạt động
- ✅ LogoutHelper xử lý logout đúng cách
- ✅ Vị trí logout trong AccountFragment đã được di chuyển lên trên

### **User Experience**
- ✅ User có thể logout từ 2 vị trí khác nhau
- ✅ Nút logout trong AccountFragment dễ tìm hơn (ngay sau lịch sử đơn hàng)
- ✅ Giao diện logout rõ ràng và thuận tiện
- ✅ Quá trình logout mượt mà và an toàn

## 🎉 **Thay đổi mới:**

### **Vị trí logout trong AccountFragment:**
- **Trước**: Ở cuối danh sách (sau "Thông tin ứng dụng")
- **Sau**: Ngay sau "Lịch sử đơn hàng" (vị trí thuận tiện hơn)

### **Thứ tự menu mới:**
1. Hồ sơ của tôi
2. Lịch sử đơn hàng
3. **Đăng xuất** ← Vị trí mới
4. Lịch sử đánh giá
5. Thông tin ứng dụng 