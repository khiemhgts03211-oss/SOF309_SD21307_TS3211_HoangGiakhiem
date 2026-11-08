# Đặc tả MovieTicketManagement
Hoàng Gia Khiêm · TS03211 · SD21307
MSSV lấy theo email đã cung cấp. Tên repo hiện có dùng TS3211; cần xác nhận mã chính xác với sinh viên.

## Công nghệ và dữ liệu
Java 17, Servlet 6.0, JSP/JSTL, JDBC, SQL Server, BCrypt. Maven WAR. Tomcat 10.1/11.
Context theo finalName dự kiến /MovieTicketManagement, chạy cổng 8080.
Database MovieTicketManagement cần có sẵn. ZIP thiếu script tạo DB và dữ liệu mẫu; chưa xác minh tài khoản admin và độ dài cột SQL.
DBConfig lấy mật khẩu sa từ MOVIE_DB_PASSWORD, mặc định rỗng. Cấu hình email dùng MOVIE_SMTP_EMAIL/MOVIE_SMTP_PASSWORD.

## Luồng đăng ký
/register nhận fullName, username, email, phone, password, confirmPassword.
fullName, username, email và password bắt buộc, kiểm tra bằng trim khi xác định rỗng. Xác nhận phải khớp. Kiểm tra trùng username/email; điện thoại có nhập thì kiểm tra trùng.
Username 4–20 ký tự thuộc A–Z, a–z, 0–9, dấu gạch dưới.
Email theo regex ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$.
Điện thoại tùy chọn; nếu có thì theo regex ^(0|84)(3|5|7|8|9)[0-9]{8}$.
Mật khẩu tối thiểu 8 ký tự, có chữ hoa, chữ thường và số.
Sau khi mọi kiểm tra hợp lệ mới hash BCrypt và lưu role CUSTOMER, status ACTIVE. Thành công chuyển /login và đặt thông báo trong session.
Đã sửa thứ tự validation trước save. Dữ liệu sai phải không tạo tài khoản.

## Đăng nhập
/login nhận username/password. Username được trim; dữ liệu rỗng bị từ chối. Giới hạn username 100, password 255. Tài khoản cần ACTIVE và role hợp lệ.
CUSTOMER mặc định về /home, ADMIN về /admin/dashboard, STAFF về /staff/dashboard. Một số URL nội bộ lưu trong session được dùng chuyển tiếp cho khách.
Session hết hạn sau 30 phút không hoạt động.

## CRUD danh mục dịch vụ
| Thao tác | URL và dữ liệu | Hành vi |
|---|---|---|
| Xem | /admin/service-categories | Danh sách danh mục |
| Thêm | /admin/service-categories/add; categoryName, status | Chặn tên đã có. status thiếu được mặc định ACTIVE. Form tên required, status ACTIVE/INACTIVE. |
| Sửa | GET /admin/service-categories/edit?id=...; POST categoryId, categoryName, status | ID không tồn tại ở GET quay danh sách. POST cập nhật theo ID. |
| Xóa | GET /admin/service-categories/delete?id=... | Service kiểm tra danh mục đang được dịch vụ sử dụng; nếu có thì từ chối. Nếu chưa dùng thì gọi DAO xóa. |

Tên danh mục bắt buộc ở UI; service hiện chưa kiểm tra chuỗi rỗng. Update chưa kiểm tra trùng tên như create. Các ca này là điểm khảo sát chất lượng.
ID sai kiểu có nguy cơ NumberFormatException; cần ghi lỗi thực tế. Dùng tài khoản ADMIN để demo CRUD.
