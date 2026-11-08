# Test cases MovieTicketManagement
Hoàng Gia Khiêm · TS03211 · SD21307

Mỗi ca đăng ký dùng username và email mới, các ô còn lại hợp lệ, xác nhận mật khẩu trùng. Kiểm tra thực tế trong DB sau mỗi ca. Ca CRUD dùng ADMIN và danh mục thử riêng.

| ID | Luồng | Tầng | Kỹ thuật | Bước | Dữ liệu | Mong đợi | Trạng thái |
|---|---|---|---|---|---|---|---|
| TC001 | Đăng ký | UI | Hợp lệ | Mở /register, điền và gửi | QA Khiêm; qa03211_01; qa03211@example.com; 0912345678; Abcd1234; xác nhận trùng | Chuyển login, tài khoản CUSTOMER/ACTIVE, hash BCrypt | Chưa chạy |
| TC002 | Đăng ký | Controller | Bắt buộc | POST để trống fullName; ô khác hợp lệ | fullName rỗng | Báo thiếu thông tin, không lưu | Chưa chạy |
| TC003 | Đăng ký | Controller | Bắt buộc | POST để trống username; ô khác hợp lệ | username rỗng | Báo thiếu thông tin, không lưu | Chưa chạy |
| TC004 | Đăng ký | Controller | Bắt buộc | POST để trống email; ô khác hợp lệ | email rỗng | Báo thiếu thông tin, không lưu | Chưa chạy |
| TC005 | Đăng ký | Controller | Bắt buộc | POST để trống password; ô khác hợp lệ | password rỗng | Báo thiếu thông tin, không lưu | Chưa chạy |
| TC006 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC007 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC008 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaaaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC009 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaaaaaaaaaaaaaaaaaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC010 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaaaaaaaaaaaaaaaaaaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC011 | Đăng ký | Controller | Biên username | Gửi form với username mới dài n | aaaaaaaaaaaaaaaaaaaaa | Lưu nếu dài 4–20; 3/21 báo sai username và không lưu | Chưa chạy |
| TC012 | Đăng ký | Controller | Biên mật khẩu | Gửi form username/email mới, xác nhận trùng | Ab1aaaa | 7 bị từ chối và không lưu; 8/9 được tạo | Chưa chạy |
| TC013 | Đăng ký | Controller | Biên mật khẩu | Gửi form username/email mới, xác nhận trùng | Ab1aaaaa | 7 bị từ chối và không lưu; 8/9 được tạo | Chưa chạy |
| TC014 | Đăng ký | Controller | Biên mật khẩu | Gửi form username/email mới, xác nhận trùng | Ab1aaaaaa | 7 bị từ chối và không lưu; 8/9 được tạo | Chưa chạy |
| TC015 | Đăng ký | Controller | Hồi quy | Gửi trường sai, kiểm tra DB sau phản hồi | username=qa test | Hiện lỗi định dạng/quy tắc, không tạo tài khoản | Chưa chạy |
| TC016 | Đăng ký | Controller | Hồi quy | Gửi trường sai, kiểm tra DB sau phản hồi | email=abc | Hiện lỗi định dạng/quy tắc, không tạo tài khoản | Chưa chạy |
| TC017 | Đăng ký | Controller | Hồi quy | Gửi trường sai, kiểm tra DB sau phản hồi | phone=12345 | Hiện lỗi định dạng/quy tắc, không tạo tài khoản | Chưa chạy |
| TC018 | Đăng ký | Controller | Hồi quy | Gửi trường sai, kiểm tra DB sau phản hồi | password=abcdefgh | Hiện lỗi định dạng/quy tắc, không tạo tài khoản | Chưa chạy |
| TC019 | Đăng ký | UI | Xác nhận | Gửi xác nhận khác | password=Abcd1234, confirmPassword=Abcd1235 | Báo xác nhận không khớp | Chưa chạy |
| TC020 | Đăng ký | Controller/UI | Trùng | Gửi username đã có, ô khác hợp lệ | username từ dữ liệu thử | Báo đã sử dụng/tồn tại, không lưu thêm | Chưa chạy |
| TC021 | Đăng ký | Controller/UI | Trùng | Gửi email đã có, ô khác hợp lệ | email từ dữ liệu thử | Báo đã sử dụng/tồn tại, không lưu thêm | Chưa chạy |
| TC022 | Đăng ký | Controller/UI | Trùng | Gửi phone đã có, ô khác hợp lệ | phone từ dữ liệu thử | Báo đã sử dụng/tồn tại, không lưu thêm | Chưa chạy |
| TC023 | Đăng ký | Controller | Phân vùng phone | Gửi phone với username/email mới | (rỗng) | Rỗng/10 số đầu 09/11 số đầu 849 qua kiểm tra; 9 hoặc 11 số đầu 09 bị từ chối | Chưa chạy |
| TC024 | Đăng ký | Controller | Phân vùng phone | Gửi phone với username/email mới | 0912345678 | Rỗng/10 số đầu 09/11 số đầu 849 qua kiểm tra; 9 hoặc 11 số đầu 09 bị từ chối | Chưa chạy |
| TC025 | Đăng ký | Controller | Phân vùng phone | Gửi phone với username/email mới | 84912345678 | Rỗng/10 số đầu 09/11 số đầu 849 qua kiểm tra; 9 hoặc 11 số đầu 09 bị từ chối | Chưa chạy |
| TC026 | Đăng ký | Controller | Phân vùng phone | Gửi phone với username/email mới | 091234567 | Rỗng/10 số đầu 09/11 số đầu 849 qua kiểm tra; 9 hoặc 11 số đầu 09 bị từ chối | Chưa chạy |
| TC027 | Đăng ký | Controller | Phân vùng phone | Gửi phone với username/email mới | 09123456789 | Rỗng/10 số đầu 09/11 số đầu 849 qua kiểm tra; 9 hoặc 11 số đầu 09 bị từ chối | Chưa chạy |
| TC028 | Login | UI | Vai trò | Đăng nhập đúng tài khoản ACTIVE CUSTOMER | Username/password hợp lệ; không có redirect chờ | Chuyển /home | Chưa chạy |
| TC029 | Login | UI | Vai trò | Đăng nhập đúng tài khoản ACTIVE ADMIN | Username/password hợp lệ; không có redirect chờ | Chuyển /admin/dashboard | Chưa chạy |
| TC030 | Login | UI | Vai trò | Đăng nhập đúng tài khoản ACTIVE STAFF | Username/password hợp lệ; không có redirect chờ | Chuyển /staff/dashboard | Chưa chạy |
| TC031 | Login | UI | Sai | Nhập mật khẩu sai | Tài khoản đang tồn tại | Báo lỗi, không tạo session đăng nhập | Chưa chạy |
| TC032 | Login | UI | Khóa | Đăng nhập tài khoản bị khóa | Thông tin đúng, trạng thái không ACTIVE | Từ chối đăng nhập | Chưa chạy |
| TC033 | CRUD | UI | Thêm | ADMIN mở add, nhập và lưu | QA-TS03211; ACTIVE | Danh mục xuất hiện | Chưa chạy |
| TC034 | CRUD | UI | Đọc | Tải lại danh sách | QA-TS03211 | Hiển thị đúng tên/trạng thái | Chưa chạy |
| TC035 | CRUD | UI | Trùng | Thêm lại cùng tên | QA-TS03211 | Báo Category already exists, không thêm | Chưa chạy |
| TC036 | CRUD | UI | Sửa | Mở sửa dòng vừa tạo | QA-TS03211-B; INACTIVE | Cùng ID cập nhật | Chưa chạy |
| TC037 | CRUD | UI | Bắt buộc | Để trống tên và lưu | categoryName rỗng | Trình duyệt yêu cầu nhập | Chưa chạy |
| TC038 | CRUD | UI | Đang dùng | Xóa danh mục có dịch vụ liên kết | Danh mục thử có liên kết | Báo cannot delete; danh mục vẫn còn | Chưa chạy |
| TC039 | CRUD | UI | Xóa | Xóa dòng QA chưa liên kết rồi tải lại | QA-TS03211-B | Danh mục mất khỏi danh sách và DB | Chưa chạy |
| TC040 | CRUD | Controller | Sai kiểu | Gọi edit với ID sai | id=abc | Ghi nhận lỗi xử lý đầu vào; có nguy cơ NumberFormatException | Chưa chạy |
