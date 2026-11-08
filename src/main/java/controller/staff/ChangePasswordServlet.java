package controller.staff;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.PasswordUtil;

@WebServlet("/staff/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Không cần mở trang riêng.
         * Chuyển về trang hồ sơ nhân viên.
         */
        response.sendRedirect(
                request.getContextPath() + "/staff/profile");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("user");

        if (loggedInUser == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        String oldPassword =
                request.getParameter("oldPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");

        String validationError = validatePassword(
                oldPassword,
                newPassword,
                confirmPassword);

        if (validationError != null) {
            setProfileData(request, loggedInUser);
            request.setAttribute(
                    "passwordError",
                    validationError);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        User user =
                userService.findById(loggedInUser.getUserId());

        if (user == null) {
            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?error=user_not_found");
            return;
        }

        /*
         * Kiểm tra mật khẩu cũ với password hash
         * đang lưu trong cơ sở dữ liệu.
         */
        boolean oldPasswordCorrect =
                PasswordUtil.verify(
                        oldPassword,
                        user.getPasswordHash());

        if (!oldPasswordCorrect) {
            setProfileData(request, user);

            request.setAttribute(
                    "passwordError",
                    "Mật khẩu hiện tại không chính xác.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        /*
         * Không cho phép đặt lại đúng mật khẩu cũ.
         */
        boolean sameAsOldPassword =
                PasswordUtil.verify(
                        newPassword,
                        user.getPasswordHash());

        if (sameAsOldPassword) {
            setProfileData(request, user);

            request.setAttribute(
                    "passwordError",
                    "Mật khẩu mới phải khác mật khẩu hiện tại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        String newPasswordHash =
                PasswordUtil.hash(newPassword);

        /*
         * Dùng email làm điều kiện cập nhật theo
         * UserService hiện có của dự án.
         */
        boolean updated =
                userService.updatePassword(
                        user.getEmail(),
                        newPasswordHash);

        if (!updated) {
            setProfileData(request, user);

            request.setAttribute(
                    "passwordError",
                    "Đổi mật khẩu thất bại. Vui lòng thử lại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        /*
         * Cập nhật lại dữ liệu session để tránh giữ
         * password hash cũ.
         */
        user.setPasswordHash(newPasswordHash);
        session.setAttribute("user", user);

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/profile?success=password");
    }

    private String validatePassword(
            String oldPassword,
            String newPassword,
            String confirmPassword) {

        if (oldPassword == null || oldPassword.isBlank()) {
            return "Vui lòng nhập mật khẩu hiện tại.";
        }

        if (newPassword == null || newPassword.isBlank()) {
            return "Vui lòng nhập mật khẩu mới.";
        }

        if (confirmPassword == null
                || confirmPassword.isBlank()) {
            return "Vui lòng xác nhận mật khẩu mới.";
        }

        if (newPassword.length() < 6) {
            return "Mật khẩu mới phải có ít nhất 6 ký tự.";
        }

        if (newPassword.length() > 100) {
            return "Mật khẩu mới không được vượt quá 100 ký tự.";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "Xác nhận mật khẩu mới không khớp.";
        }

        return null;
    }

    private void setProfileData(
            HttpServletRequest request,
            User user) {

        request.setAttribute("user", user);
    }
}