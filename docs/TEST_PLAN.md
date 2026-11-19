# Kế hoạch kiểm thử MovieTicketManagement

Hoàng Gia Khiêm · TS03211 · SD21307. Lịch đề xuất cập nhật Tuần 1 và 2 vào 15/11/2026.

| Nhân lực | Thời hạn | Ngày bắt đầu | Ngày bàn giao | Phương thức | Phạm vi và nhiệm vụ |
|---|---|---|---|---|---|
| Hoàng Gia Khiêm | Buổi cập nhật | 15/11/2026 | 15/11/2026 | Đọc mã và tài liệu | Đối chiếu đăng ký, đăng nhập và CRUD danh mục dịch vụ với đặc tả. |
| Hoàng Gia Khiêm | Buổi cập nhật | 15/11/2026 | 15/11/2026 | JUnit 5 và Mockito | Thực thi 51 ca UT_001–UT_051. Biên username 3/4/5 và 19/20/21, mật khẩu đăng ký 7/8/9, điện thoại tùy chọn, giới hạn đăng nhập và quyền. |
| Hoàng Gia Khiêm | Khi có SQL Server thử | 15/11/2026 | Cập nhật sau khi chạy | Eclipse/Tomcat, trình duyệt, HTTP | Chạy 40 ca TC001–TC040. Kiểm tra phản hồi, quyền và bản ghi DB. |
| Hoàng Gia Khiêm | Sau khi kiểm tra | 15/11/2026 | 15/11/2026 | Maven và GitHub | Lưu report Surefire, cập nhật trạng thái thực tế và nộp link commit cá nhân. |

| Điều kiện | Tiêu chí |
|---|---|
| Bắt đầu JUnit | JDK 21, Maven, JUnit 5 và Mockito. Chuẩn bị được dependencies. |
| Bắt đầu ca web | Tomcat tương thích Servlet, SQL Server thử, dữ liệu nền và tài khoản đúng vai trò. Xem SPECIFICATIONS.md. |
| Tạm dừng | Ghi lỗi môi trường hoặc dữ liệu thiếu. Khi xử lý xong, chạy lại ca bị ảnh hưởng. |
| Hoàn thành JUnit | 51 ca, 0 failures, 0 errors, 0 skipped. Giữ báo cáo Maven. |
| Hoàn thành web | Ghi Actual Result cho 40 ca. Với sai khác, ghi ID, dữ liệu, bước tái hiện và bằng chứng. |

JUnit xác nhận phản hồi HTTP, session và lời gọi DAO/service tại Controller. Các ca ngoại lệ ghi hành vi hiện tại để theo dõi. Kiểm tra ghi dữ liệu, JSP, filter và upload thực hiện với Tomcat và SQL Server thử.
