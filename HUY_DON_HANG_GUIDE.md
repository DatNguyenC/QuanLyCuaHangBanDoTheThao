# Hướng dẫn sử dụng chức năng hủy đơn hàng

## Tổng quan
Chức năng hủy đơn hàng đã được thêm vào giao diện người dùng (User Interface) cho phép khách hàng hủy các đơn hàng của mình trong một số trường hợp nhất định.

## Cách sử dụng

### 1. Truy cập vào quản lý hóa đơn
- Đăng nhập vào hệ thống với tài khoản người dùng
- Click vào biểu tượng giỏ hàng (shopping cart) trên thanh công cụ
- Trong form giỏ hàng, click vào nút "Xem Hóa Đơn"

### 2. Chọn đơn hàng cần hủy
- Trong giao diện quản lý hóa đơn, bạn sẽ thấy các tab trạng thái:
  - **Chưa Thanh Toán**: Đơn hàng chưa thanh toán
  - **Đã Thanh Toán**: Đơn hàng đã thanh toán
  - **Đang Giao**: Đơn hàng đang được giao
  - **Đã Giao**: Đơn hàng đã giao thành công
  - **Hủy**: Đơn hàng đã bị hủy

### 3. Hủy đơn hàng
- Chọn tab chứa đơn hàng bạn muốn hủy (Chưa Thanh Toán, Đã Thanh Toán, hoặc Đang Giao)
- Click chọn đơn hàng trong danh sách
- Click nút **"Hủy Đơn Hàng"** (màu đỏ)
- Xác nhận hủy đơn hàng trong hộp thoại hiện ra

## Điều kiện hủy đơn hàng

### Có thể hủy:
- ✅ Đơn hàng có trạng thái "Chưa Thanh Toán"
- ✅ Đơn hàng có trạng thái "Đã Thanh Toán" 
- ✅ Đơn hàng có trạng thái "Đang Giao"

### Không thể hủy:
- ❌ Đơn hàng có trạng thái "Đã Giao" (đã hoàn thành)
- ❌ Đơn hàng đã bị hủy trước đó
- ❌ Đơn hàng không thuộc về tài khoản đang đăng nhập

## Quy trình hủy đơn hàng

Khi hủy đơn hàng, hệ thống sẽ thực hiện các bước sau:

1. **Xác nhận quyền**: Kiểm tra đơn hàng có thuộc về người dùng hiện tại không
2. **Hoàn lại kho**: Tự động hoàn lại số lượng sản phẩm vào kho
3. **Cập nhật trạng thái**: Chuyển trạng thái đơn hàng thành "Hủy"
4. **Thông báo**: Hiển thị thông báo thành công và chuyển sang tab "Hủy"

## Lưu ý quan trọng

- ⚠️ **Hành động không thể hoàn tác**: Khi đã hủy đơn hàng, không thể khôi phục lại
- ⚠️ **Hoàn lại kho tự động**: Số lượng sản phẩm sẽ được hoàn lại vào kho ngay lập tức
- ⚠️ **Chỉ hủy được đơn hàng của mình**: Không thể hủy đơn hàng của người khác

## Thông báo lỗi thường gặp

### "Vui lòng chọn một hóa đơn để hủy!"
- **Nguyên nhân**: Chưa chọn đơn hàng nào trong danh sách
- **Giải pháp**: Click chọn đơn hàng trước khi nhấn nút hủy

### "Bạn không có quyền hủy hóa đơn này!"
- **Nguyên nhân**: Đơn hàng không thuộc về tài khoản đang đăng nhập
- **Giải pháp**: Chỉ có thể hủy đơn hàng của chính mình

### "Hủy đơn hàng thất bại!"
- **Nguyên nhân**: Có lỗi xảy ra trong quá trình xử lý
- **Giải pháp**: Thử lại hoặc liên hệ hỗ trợ

## Tính năng bổ sung

### Xem chi tiết đơn hàng
- Click nút "Xem Chi Tiết" để xem thông tin chi tiết về sản phẩm trong đơn hàng
- Hiển thị hình ảnh, tên sản phẩm, kích cỡ, màu sắc, số lượng và giá tiền

### Làm mới danh sách
- Click nút "Làm mới" để cập nhật lại danh sách đơn hàng
- Hữu ích khi có thay đổi từ các nguồn khác

## Hỗ trợ kỹ thuật

Nếu gặp vấn đề khi sử dụng chức năng hủy đơn hàng, vui lòng:
1. Kiểm tra lại các điều kiện hủy đơn hàng
2. Thử làm mới trang và thực hiện lại
3. Liên hệ bộ phận hỗ trợ khách hàng

---
*Chức năng hủy đơn hàng được phát triển để cải thiện trải nghiệm người dùng và quản lý đơn hàng hiệu quả hơn.* 