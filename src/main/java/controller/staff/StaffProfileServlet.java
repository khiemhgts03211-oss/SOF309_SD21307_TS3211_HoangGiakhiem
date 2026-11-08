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

@WebServlet("/staff/profile")
public class StaffProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    /**
     * Hiển thị thông tin cá nhân của nhân viên.
     */
    @Override
    protected void doGet(
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

        User user =
                userService.findById(loggedInUser.getUserId());

        if (user == null) {
            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?error=user_not_found");
            return;
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/profile.jsp")
                .forward(request, response);
    }

    /**
     * Cập nhật thông tin cá nhân của nhân viên.
     */
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

        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));

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
         * Gán lại dữ liệu để khi validation lỗi,
         * form vẫn hiển thị dữ liệu người dùng vừa nhập.
         */
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);

        String validationError =
                validateProfile(fullName, email, phone);

        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.setAttribute("user", user);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        boolean updated = userService.update(user);

        if (!updated) {
            request.setAttribute(
                    "errorMessage",
                    "Cập nhật thông tin cá nhân thất bại.");

            request.setAttribute("user", user);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/profile.jsp")
                    .forward(request, response);
            return;
        }

        /*
         * Cập nhật lại thông tin trong session.
         * Không nên dùng dữ liệu cũ sau khi cập nhật.
         */
        User updatedUser = userService.findById(user.getUserId());

        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
        } else {
            session.setAttribute("user", user);
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/profile?success=updated");
    }

    /**
     * Kiểm tra dữ liệu hồ sơ.
     */
    private String validateProfile(
            String fullName,
            String email,
            String phone) {

        if (fullName == null || fullName.isBlank()) {
            return "Họ và tên không được để trống.";
        }

        if (fullName.length() > 100) {
            return "Họ và tên không được vượt quá 100 ký tự.";
        }

        if (email == null || email.isBlank()) {
            return "Email không được để trống.";
        }

        String emailRegex =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            return "Email không đúng định dạng.";
        }

        if (email.length() > 100) {
            return "Email không được vượt quá 100 ký tự.";
        }

        if (phone != null && !phone.isBlank()) {

            String phoneRegex = "^[0-9+\\-\\s]{9,15}$";

            if (!phone.matches(phoneRegex)) {
                return "Số điện thoại không đúng định dạng.";
            }
        }

        return null;
    }

    /**
     * Loại bỏ khoảng trắng đầu và cuối.
     */
    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}