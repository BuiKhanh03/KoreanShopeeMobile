# Hướng dẫn Debug Nút Logout

## Các bước kiểm tra:

### 1. Đảm bảo đã đăng nhập
- Chạy app và đăng nhập thành công
- Sau khi đăng nhập, app sẽ chuyển đến màn hình chính với bottom navigation

### 2. Vào tab Account
- Ở màn hình chính, nhấn vào tab "Account" (biểu tượng người dùng) ở bottom navigation
- Tab Account là tab cuối cùng bên phải

### 3. Kiểm tra vị trí nút Logout
Nút logout nằm ở vị trí thứ 3 trong danh sách:
1. **My Profile** (Hồ sơ của tôi)
2. **Order History** (Lịch sử đơn hàng)  
3. **ĐĂNG XUẤT** ← Nút logout ở đây
4. Review History (Đánh giá sản phẩm)
5. About App (Về ứng dụng)

### 4. Debug Messages
App đã được thêm debug messages:
- Khi vào AccountFragment: sẽ hiện toast "Tìm thấy nút logout!" hoặc "KHÔNG tìm thấy nút logout!"
- Khi nhấn nút logout: sẽ hiện toast "Đang logout..."

### 5. Nếu vẫn không thấy nút logout:
- Kiểm tra xem có đang ở đúng tab Account không
- Thử scroll xuống nếu layout dài
- Kiểm tra xem có lỗi gì trong logcat không

### 6. Cấu trúc layout hiện tại:
```
fragment_account.xml:
├── Profile Picture
├── Profile Name  
├── Logo
└── Menu Items:
    ├── My Profile
    ├── Order History
    ├── ĐĂNG XUẤT ← Nút logout
    ├── Review History
    └── About App
```

## Lưu ý:
- Nút logout chỉ hiển thị khi đã đăng nhập thành công
- Nếu chưa đăng nhập, app sẽ chuyển về màn hình login
- Nút logout có icon logout và text "ĐĂNG XUẤT" màu hồng 