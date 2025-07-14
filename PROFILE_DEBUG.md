# Hướng dẫn Debug Màn hình "Hồ sơ của tôi"

## Các bước kiểm tra:

### 1. Đảm bảo đã đăng nhập
- Chạy app và đăng nhập thành công
- Sau khi đăng nhập, app sẽ chuyển đến màn hình chính với bottom navigation

### 2. Vào màn hình "Hồ sơ của tôi"
- Ở màn hình chính, nhấn vào tab "Account" (biểu tượng người dùng) ở bottom navigation
- Nhấn vào "My Profile" (Hồ sơ của tôi) - mục đầu tiên trong danh sách

### 3. Debug Messages đã được thêm
App đã được thêm debug messages chi tiết:
- Khi vào ProfileCustomer: sẽ hiện log "ProfileCustomer onCreate started"
- Khi tải thông tin: sẽ hiện toast "Đang tải thông tin..."
- Khi tải thành công: sẽ hiện toast "Đã tải thông tin thành công!"
- Khi có lỗi: sẽ hiện thông báo lỗi chi tiết

### 4. Kiểm tra Logcat
Mở Logcat trong Android Studio và filter theo tag "ProfileCustomer" để xem:
- Quá trình khởi tạo views
- API call và response
- Dữ liệu user được nhận
- Lỗi nếu có

### 5. Các thông tin hiển thị:
- **Họ và tên**: Hiển thị fullName hoặc userName nếu fullName trống
- **Số điện thoại**: Hiển thị phoneNumber từ API
- **Email**: Hiển thị email từ API (không thể chỉnh sửa)

### 6. Chức năng cập nhật:
- Có thể chỉnh sửa họ tên và số điện thoại
- Nhấn "Cập nhật và lưu" để lưu thay đổi
- Email không thể thay đổi

### 7. Scroll đã được sửa:
- Layout đã được cập nhật với `android:fillViewport="true"`
- Thêm `android:minHeight="match_parent"` cho LinearLayout
- Thêm space ở cuối để scroll mượt hơn

### 8. Nếu vẫn không hiển thị thông tin:
- Kiểm tra xem có token trong SharedPreferences không
- Kiểm tra API response trong Logcat
- Đảm bảo đã đăng nhập thành công trước đó

### 9. Cấu trúc API:
- GET `/api/user` với Authorization header để lấy thông tin
- PUT `/api/user` với Authorization header để cập nhật thông tin

## Lưu ý:
- Màn hình này chỉ hoạt động khi đã đăng nhập
- Token được lưu trong SharedPreferences
- API calls có timeout và error handling
- Layout responsive với ScrollView 