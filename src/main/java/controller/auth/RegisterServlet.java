package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.PasswordUtil;
import utils.ValidationUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        // Validate dữ liệu
        if (fullName == null || fullName.trim().isEmpty()
                || username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            request.setAttribute("error",
                    "Vui lòng nhập đầy đủ thông tin.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        if (!password.equals(confirmPassword)) {

            request.setAttribute(
                    "error",
                    "Xác nhận mật khẩu không khớp.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        if (userService.existsUsername(username)) {

            request.setAttribute(
                    "error",
                    "Tên đăng nhập đã tồn tại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        if (userService.existsEmail(email)) {

            request.setAttribute(
                    "error",
                    "Email đã được sử dụng.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        if (phone != null
                && !phone.trim().isEmpty()
                && userService.existsPhone(phone)) {

            request.setAttribute(
                    "error",
                    "Số điện thoại đã được sử dụng.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        if (!ValidationUtil.isValidUsername(username)) {

            request.setAttribute(
                    "error",
                    "Tên đăng nhập chỉ gồm chữ, số, dấu gạch dưới và từ 4-20 ký tự.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {

            request.setAttribute(
                    "error",
                    "Email không đúng định dạng.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }
        
        if (!ValidationUtil.isValidPhone(phone)) {

            request.setAttribute(
                    "error",
                    "Số điện thoại không hợp lệ.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }
        
        if (!ValidationUtil.isStrongPassword(password)) {

            request.setAttribute(
                    "error",
                    "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường và số.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }
        
        User user = new User();

        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);

        user.setPasswordHash(
                PasswordUtil.hash(password));

        user.setRole("CUSTOMER");

        user.setStatus("ACTIVE");

        boolean success = userService.save(user);

        if (!success) {

            request.setAttribute(
                    "error",
                    "Không thể tạo tài khoản.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/register.jsp")
                    .forward(request, response);

            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute(
                "success",
                "Đăng ký thành công. Vui lòng đăng nhập.");

        response.sendRedirect(
                request.getContextPath() + "/login");
    }
}