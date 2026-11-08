# Nhật ký hỗ trợ AI
Hoàng Gia Khiêm · TS03211 · SD21307

Yêu cầu nhận được: đóng gói MovieTicketManagement và tài liệu tuần 1 cho repo cá nhân/email đã cung cấp.
Đã đọc RegisterServlet, LoginServlet, ValidationUtil, PasswordUtil, ServiceCategoryServiceImpl, các servlet CRUD, form thêm, DBConfig và EmailConfig.

Prompt tái sử dụng: Bạn là QA Lead. Dựa trên SPECIFICATIONS.md, dùng phân vùng tương đương và giá trị biên thiết kế Test Plan và test cases Controller/UI cho đăng ký, đăng nhập và CRUD danh mục dịch vụ. Có bước, dữ liệu, mong đợi và Chưa chạy. Thêm ca hồi quy đảm bảo tài khoản sai định dạng không được lưu.

Kết quả: tạo ba tài liệu và bảng ca. Sửa RegisterServlet đưa validation lên trước userService.save. Gộp JUnit/Selenium, cấu hình WAR/Surefire. Chuyển mật khẩu DB/SMTP sang biến môi trường và bỏ log mật khẩu SQL.
Giới hạn: thiếu script SQL và dữ liệu admin, cần tác giả cung cấp/dùng DB hiện có. Sinh viên cần chạy và ghi kết quả thật. MSSV tạm dùng TS03211 theo email, repo đang ghi TS3211.
