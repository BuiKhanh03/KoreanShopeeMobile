# Cấu trúc thư mục dự án KoreanShopee

## Tổng quan
Dự án đã được tổ chức lại theo mô hình Clean Architecture với các package được phân chia theo chức năng để dễ quản lý và bảo trì.

## Cấu trúc thư mục

```
app/src/main/java/com/example/koreanshopee/
├── api/                          # API Layer
│   ├── ApiService.java          # Interface định nghĩa API endpoints
│   └── ApiClient.java           # Cấu hình Retrofit và tạo API service
│
├── auth/                         # Authentication Layer
│   ├── TokenManager.java        # Quản lý lưu trữ và truy xuất token
│   ├── AuthInterceptor.java     # Tự động thêm token vào header
│   └── LogoutHelper.java        # Xử lý logout và xóa token
│
├── model/                        # Data Models
│   ├── LoginRequest.java        # Model cho request đăng nhập
│   └── LoginResponse.java       # Model cho response đăng nhập
│
├── ui/                          # UI Layer
│   ├── auth/                    # Authentication UI
│   │   ├── LoginActivity.java   # Màn hình đăng nhập
│   │   ├── SignupActivity.java  # Màn hình đăng ký
│   │   └── ForgotPasswordActivity.java # Màn hình quên mật khẩu
│   └── main/                    # Main UI
│       └── MainActivity.java    # Màn hình chính
│
├── utils/                       # Utility Classes
│   └── ApiExample.java         # Ví dụ sử dụng API
│
├── Fragment/                    # Existing Fragments
├── Chat/                        # Existing Chat components
├── Notification/                # Existing Notification components
├── OrderHistory/                # Existing Order History components
└── [Other existing files]       # Các file hiện có khác
```

## Mô tả chi tiết các package

### 1. `api` Package
**Chức năng**: Xử lý tất cả các tương tác với API server
- **ApiService**: Interface định nghĩa các API endpoints
- **ApiClient**: Cấu hình Retrofit, tạo API service instances

### 2. `auth` Package
**Chức năng**: Quản lý authentication và authorization
- **TokenManager**: Lưu trữ, truy xuất và quản lý access/refresh tokens
- **AuthInterceptor**: Tự động thêm token vào header của API calls
- **LogoutHelper**: Xử lý logout và xóa token

### 3. `model` Package
**Chức năng**: Chứa các data models/entities
- **LoginRequest**: Model cho request đăng nhập
- **LoginResponse**: Model cho response đăng nhập
- Có thể thêm các model khác như UserProfile, Product, Order, etc.

### 4. `ui` Package
**Chức năng**: Chứa tất cả các UI components (Activities, Fragments)

#### 4.1 `ui.auth` Sub-package
- **LoginActivity**: Màn hình đăng nhập với tích hợp API
- **SignupActivity**: Màn hình đăng ký
- **ForgotPasswordActivity**: Màn hình quên mật khẩu

#### 4.2 `ui.main` Sub-package
- **MainActivity**: Màn hình chính sau khi đăng nhập

### 5. `utils` Package
**Chức năng**: Chứa các utility classes và helper functions
- **ApiExample**: Ví dụ và hướng dẫn sử dụng API

## Lợi ích của cấu trúc mới

### 1. **Separation of Concerns**
- Mỗi package có trách nhiệm riêng biệt
- Dễ dàng tìm và sửa đổi code
- Giảm coupling giữa các components

### 2. **Scalability**
- Dễ dàng thêm tính năng mới
- Có thể mở rộng từng package độc lập
- Dễ dàng thêm các package mới (ví dụ: `ui.product`, `ui.order`)

### 3. **Maintainability**
- Code được tổ chức rõ ràng
- Dễ dàng debug và test
- Giảm conflict khi làm việc nhóm

### 4. **Reusability**
- Các components có thể tái sử dụng
- API layer có thể được sử dụng bởi nhiều UI components
- Auth layer có thể được sử dụng xuyên suốt app

## Quy tắc đặt tên

### 1. **Package Names**
- Sử dụng lowercase
- Tên ngắn gọn và mô tả chức năng
- Ví dụ: `api`, `auth`, `model`, `ui`

### 2. **Class Names**
- Sử dụng PascalCase
- Tên mô tả rõ chức năng
- Ví dụ: `LoginActivity`, `TokenManager`, `ApiClient`

### 3. **File Names**
- Tương ứng với tên class
- Sử dụng PascalCase
- Ví dụ: `LoginActivity.java`, `TokenManager.java`

## Hướng dẫn thêm tính năng mới

### 1. **Thêm API endpoint mới**
```java
// 1. Thêm vào ApiService.java
@GET("api/user/profile")
Call<UserProfile> getUserProfile();

// 2. Tạo model trong package model
public class UserProfile { ... }

// 3. Sử dụng trong UI
ApiClient.getAuthenticatedApiService(context)
    .getUserProfile()
    .enqueue(new Callback<UserProfile>() { ... });
```

### 2. **Thêm màn hình mới**
```java
// 1. Tạo package mới nếu cần
// ui.product/ProductListActivity.java

// 2. Thêm vào AndroidManifest.xml
<activity android:name=".ui.product.ProductListActivity" />

// 3. Tạo layout file
// res/layout/activity_product_list.xml
```

### 3. **Thêm utility mới**
```java
// Tạo trong package utils
public class ValidationUtils {
    public static boolean isValidEmail(String email) { ... }
}
```

## Migration Notes

### 1. **Import Statements**
Tất cả import statements đã được cập nhật để phản ánh cấu trúc mới:
```java
// Cũ
import com.example.koreanshopee.Login;
import com.example.koreanshopee.TokenManager;

// Mới
import com.example.koreanshopee.ui.auth.LoginActivity;
import com.example.koreanshopee.auth.TokenManager;
```

### 2. **AndroidManifest.xml**
Đã cập nhật tất cả activity names:
```xml
<!-- Cũ -->
<activity android:name=".Login" />
<activity android:name=".MainActivity" />

<!-- Mới -->
<activity android:name=".ui.auth.LoginActivity" />
<activity android:name=".ui.main.MainActivity" />
```

### 3. **Existing Files**
Các file hiện có trong `Fragment/`, `Chat/`, `Notification/`, `OrderHistory/` vẫn giữ nguyên vị trí để tránh breaking changes.

## Best Practices

### 1. **Package Organization**
- Mỗi package chỉ chứa các class liên quan
- Không tạo package quá sâu (>3 levels)
- Sử dụng tên package mô tả chức năng

### 2. **Dependency Management**
- UI layer chỉ phụ thuộc vào API và Auth layers
- Model layer không phụ thuộc vào bất kỳ layer nào khác
- Utils có thể được sử dụng bởi tất cả layers

### 3. **Code Organization**
- Mỗi class chỉ có một trách nhiệm
- Sử dụng meaningful names
- Thêm comments cho complex logic

## Future Improvements

### 1. **Repository Pattern**
Có thể thêm package `repository` để quản lý data sources:
```
repository/
├── UserRepository.java
├── ProductRepository.java
└── OrderRepository.java
```

### 2. **ViewModel Pattern**
Có thể thêm package `viewmodel` cho MVVM architecture:
```
viewmodel/
├── LoginViewModel.java
├── MainViewModel.java
└── ProductViewModel.java
```

### 3. **Database Layer**
Có thể thêm package `database` cho local storage:
```
database/
├── AppDatabase.java
├── UserDao.java
└── ProductDao.java
``` 