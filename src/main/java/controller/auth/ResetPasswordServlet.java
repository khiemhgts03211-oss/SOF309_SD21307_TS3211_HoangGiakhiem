package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.PasswordUtil;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("otpVerified") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/forgot-password");

            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/reset-password.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("otpVerified") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/forgot-password");

            return;
        }

        String password =
                request.getParameter("password");

        String confirm =
                request.getParameter("confirmPassword");

        if (!password.equals(confirm)) {

            request.setAttribute(
                    "error",
                    "Xác nhận mật khẩu không khớp.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/reset-password.jsp")
                    .forward(request, response);

            return;
        }

        String email =
                (String) session.getAttribute("resetEmail");

        String hash =
                PasswordUtil.hash(password);

        boolean success =
                userService.updatePassword(email, hash);

        if (!success) {

            request.setAttribute(
                    "error",
                    "Không thể cập nhật mật khẩu.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/reset-password.jsp")
                    .forward(request, response);

            return;
        }

        session.removeAttribute("otp");
        session.removeAttribute("otpTime");
        session.removeAttribute("resetEmail");
        session.removeAttribute("otpVerified");

        request.getSession().setAttribute(
                "success",
                "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");

        response.sendRedirect(
                request.getContextPath()
                + "/login");

    }

}