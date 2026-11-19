# Kết quả JUnit Tuần 2 · MovieTicketManagement

Chạy thực tế trên Linux ngày 15/11/2026, Temurin JDK 21, Maven 3.9.9.

Lệnh chạy: `mvn clean test -Dtest=MovieAuthControllerTest,MovieCategoryControllerTest`.

| Lớp test | Số lượt | Failures | Errors | Skipped |
|---|---:|---:|---:|---:|
| controller.admin.servicecategorys.MovieCategoryControllerTest | 11 | 0 | 0 | 0 |
| controller.auth.MovieAuthControllerTest | 40 | 0 | 0 | 0 |
| **Tổng** | **51** | **0** | **0** | **0** |

**BUILD SUCCESS.** JUnit dùng DAO/service giả lập. 40 ca chạy tay với web/SQL chưa được thực thi.

Báo cáo trong ZIP ở minh-chung-junit/linux-2026-11-15. CMD đăng GitHub chạy lại bộ test và đưa báo cáo của máy sinh viên vào minh-chung-junit/windows-latest.
