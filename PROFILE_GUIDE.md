# Hướng dẫn Chức năng User Profile

## Tổng quan
Ứng dụng đã được tích hợp chức năng hiển thị thông tin user profile từ API. Khi user đăng nhập thành công, thông tin profile sẽ được hiển thị trong tab Account.

## Các thành phần đã được tạo:

### 1. Model Classes
- `UserProfile.java` - Model cho thông tin user
- `UserProfileResponse.java` - Wrapper response từ API
- `UpdateProfileRequest.java` - Model cho cập nhật thông tin user

### 2. API Integration
- `ApiService.java` - Thêm endpoints `GET /api/user` và `PUT /api/user` với Authorization header
- `TokenManager.java` - Quản lý authentication token

### 3. UI Updates
- `AccountFragment.java` - Tự động load và hiển thị thông tin user profile
- `ProfileCustomer.java` - Hiển thị và cập nhật thông tin chi tiết user profile
- `LoginActivity.java` - Xử lý đăng nhập và lưu token
- `LogoutHelper.java` - Xử lý logout và xóa token

## Cách hoạt động:

### 1. Đăng nhập
- User nhập email và password
- App gọi API `POST /api/authentication/login`
- Nếu thành công, token được lưu vào SharedPreferences
- Chuyển đến màn hình chính (layout_sceen)

### 2. Hiển thị Profile
- Khi vào tab Account, `AccountFragment` tự động gọi API `GET /api/user`
- API call bao gồm Authorization header: `Bearer {token}`
- Thông tin user được hiển thị trong TextView `profileName`

### 3. Chi tiết Profile
- Nhấn vào "My Profile" để xem thông tin chi tiết
- `ProfileCustomer` hiển thị thông tin: họ tên, số điện thoại, email
- Có thể cập nhật: họ tên, số điện thoại (email chỉ đọc)

### 4. Logout
- User nhấn nút "ĐĂNG XUẤT" trong tab Account
- Token bị xóa khỏi SharedPreferences
- Chuyển về màn hình login

## API Endpoints:

### Login
```
POST /api/authentication/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Get User Profile
```
GET /api/user
Authorization: Bearer {access_token}
```

Response:
```json
{
  "value": {
    "id": "81807b34-25ca-4792-9a64-be5f0c6bdef6",
    "firstName": "string",
    "lastName": "string", 
    "userName": "khanh123",
    "email": "buikhanh0130@gmail.com",
    "phoneNumber": "string"
  },
  "paging": null,
  "errors": null
}
```

### Update User Profile
```
PUT /api/user
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "firstName": "Nguyễn",
  "lastName": "Văn A",
  "phoneNumber": "0123456789"
}
```

## Cách test:

1. **Đăng ký tài khoản mới**:
   - Vào màn hình Signup
   - Điền thông tin và chọn role
   - Nhấn "ĐĂNG KÝ"

2. **Đăng nhập**:
   - Vào màn hình Login
   - Nhập email và password
   - Nhấn "ĐĂNG NHẬP"

3. **Xem Profile**:
   - Sau khi đăng nhập, vào tab Account
   - Thông tin user sẽ được hiển thị tự động

4. **Xem chi tiết Profile**:
   - Trong tab Account, nhấn "My Profile"
   - Xem thông tin chi tiết: họ tên, số điện thoại, email
   - Có thể chỉnh sửa: họ tên, số điện thoại (email chỉ đọc)

5. **Logout**:
   - Trong tab Account, nhấn "ĐĂNG XUẤT"
   - App sẽ chuyển về màn hình login

## Lưu ý:
- Token được lưu tự động sau khi đăng nhập thành công
- Nếu token hết hạn, user cần đăng nhập lại
- Thông tin profile được cập nhật mỗi khi vào tab Account
- Nếu API trả về lỗi, sẽ hiển thị toast message thông báo 