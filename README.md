# Fridge Feast

## 1. Giới thiệu dự án
Trong cuộc sống hiện đại, nhiều người gặp khó khăn trong việc quyết định "Hôm
nay ăn gì?". Đặc biệt, khi chỉ có một số nguyên liệu sẵn có trong nhà, việc tìm kiếm
công thức phù hợp không hề đơn giản. Ngoài ra, nếu thiếu một số nguyên liệu cần
thiết, người dùng phải mất thời gian tìm kiếm cửa hàng phù hợp để mua sắm.

Một hệ thống như **Fridge Feast** có khả năng gợi ý món ăn dựa trên nguyên liệu sẵn có, đồng thời
liên kết với các cửa hàng để mua nguyên liệu còn thiếu sẽ giúp người dùng tiết
kiệm thời gian, tối ưu hóa việc nấu nướng và mua sắm thực phẩm. Các cửa hàng có
thể đăng ký để kinh doanh bán thực phẩm trên hệ thống tương tự như trên các sàn
thương mại điện tử.


## 2. Các chức năng chính
- **Sử dụng Role-Based Access Control (RBAC) để phân quyền** - trong đó các vai trò được kế thừa quyền lẫn nhau (role hierarchy),Supllier kế thừa những chức năng của Uer và Admin kết thừa cả chức năng của Supllier và User.

- **Đăng ký, xác minh và đăng nhập người dùng** – Hệ thống đăng ký tài khoản mới với xác minh email qua mã OTP, đăng nhập bảo mật, làm mới token và đăng xuất an toàn với cookie HTTP-only,tích hợp đăng nhập bằng Google.

- **Tìm kiếm và đưa ra gợi ý về món ăn dựa trên những nguyên liệu có sẵn** - Sử dụng Criteria cung cấp khả năng tìm kiếm, phân trang, lọc và sắp xếp danh sách người dùng theo từ khóa một cách linh hoạt và hiệu quả.
  
- **Quản lý thông tin chi tiết của người dùng** – Cho phép xem chi tiết và cập nhật thông tin cá nhân với hỗ trợ upload ảnh đại diện, tạo trải nghiệm người dùng hoàn chỉnh.

- **Quản lý thông tin về các món ăn,cũng như nguyên liệu** - Hệ thống cho phép admin thêm sửa xóa những nguyên liệu,món ăn từ đó tăng trải nghiệm cùng người dùng.

- **Hệ thống phân quyền rõ ràng và bảo mật** – Phân biệt rõ ràng quyền hạn giữa ADMIN (quản lý toàn bộ người dùng, bao gồm xóa vĩnh viễn,thêm sửa xóa món ăn),SUPPLIER(nhà cung cấp nguyên liệu) và USER (chỉ quản lý thông tin cá nhân,và mua các sản phẩm) được bảo vệ bằng @PreAuthorize để đảm bảo an toàn tuyệt đối.

- **Đăng ký từ User lên thành nhà cung cấp** - Thay vì chỉ mua những nguyên liệu,các nhà cung cấp hoàn toàn có thể đăng ký trở thành nhà cung cấp của chính Fridge Feast.

- **Hệ thống thanh toán qua VnPay bảo mật** - Sử dụng tích hợp hệ thống thanh toán Vnpay(do không có mã số thuế nên chỉ đăng ký sử dụng Vnpay trên môi trường Test).

- **Tích hợp ApiChat của Gemeni** - Nếu như trong quá trình trải nghiệm hệ thống,người dùng không tìm được món ăn phù hợp thì hoàn toàn có thể chat với Gemeni thông qua hệ thống,từ đó tìm kiếm món ăn và mua những nguyên liệu trên Fridge Feast.
## 3. Công nghệ

### 3.1 Công nghệ sử dụng

- Java Spring Boot

- MySQL
  
- Redis

- OAuth 2.0

- JWT

- Postman

- Cloudinary

- Docker

- Thanh toán VnPay

### 3.2. Cấu trúc dự án

```java
FridgeFeast (Backend - Spring Boot)
│── src/main/java/com/example/UserManagement
│   ├── Configuration  # Cấu hình Spring Boot (Security, CORS, etc.)
│   ├── Controller     # Xử lý request từ client
│   ├── Dto            # Định nghĩa request/response DTOs
│   ├── Entity         # Các class ánh xạ database
│   ├── Enums          # Các định nghĩa chung của hệ thống
│   ├── Exception      # Xử lý các lỗi của hệ thống
│   ├── Repository     # Tầng truy vấn dữ liệu (JPA Repository)
│   ├── Service        # Xử lý logic nghiệp vụ
│   ├── Utils          # Định nghĩa các logic chung dùng được trong hệ thống
│   ├── Validator     # Chứa các anotation tự tạo
│── src/main/resources/application  # File chứa các thông tin của hệ thống
│── docker-compose.yml #Nơi chưa cấu hình Docker dùng để chạy Redis
```

## 4.Các bước cài đặt môi trường
### 4.1.Khởi tạo cơ sở dữ liệu trong MySql Workbench
- Chạy MySql Workbench tạo 1 Schema tên "FridgeFeastFinal" sau đó chạy code tải về trên intellij,cơ sở dữ liệu sẽ tự động được tạo trong MySql Workbench.
### 4.2.Khởi Tạo redis trên Docker
- Tải Docker về máy và chạy lên,sau đó lấy code về chạy trên Intellij sau đó bấm vào Terminal và gõ lệnh "dockercompose up -d",Docker sẽ tự động tạo một file tên "redisTTCS" để chạy redis trên máy.
