# Kế hoạch kiểm thử tuần 1
Hoàng Gia Khiêm · TS03211 · SD21307

Phạm vi: đăng ký, đăng nhập và CRUD danh mục dịch vụ. Kiểm tra controller/service và giao diện. Thanh toán, OTP, đặt ghế, thống kê chuyên sâu ngoài phạm vi.

Môi trường: JDK 17, Eclipse Enterprise, Tomcat 10.1/11, SQL Server với MovieTicketManagement đầy đủ. Chuẩn bị một ADMIN, STAFF, CUSTOMER, tài khoản khóa; lấy từ dữ liệu của tác giả. Dùng tên đăng nhập/email mới và danh mục QA-TS03211 chưa được sử dụng.

Phương pháp: phân vùng dữ liệu rỗng/trùng/sai định dạng/hợp lệ; giá trị biên username 3/4/5/19/20/21 và mật khẩu 7/8/9. So sánh trước/sau DB để bắt hồi quy lỗi đăng ký lưu trước validation. Với điện thoại dùng số nội địa và tiền tố 84 theo regex thực tế.

Thực thi: ca UI thao tác trên web; ca controller gửi request có session và dữ liệu tương ứng hoặc triển khai test tự động tuần 2. Ghi ảnh/log khi lỗi và kết quả thực tế. Tất cả ca ban đầu Chưa chạy.
Y1 đạt khi web khởi động và ba luồng chính thành công. Bộ tài liệu Y2 cần được sinh viên rà soát theo database thật.
Giới hạn kiểm chứng: ZIP không kèm SQL và môi trường này chưa build/chạy Tomcat hoặc SQL Server.
