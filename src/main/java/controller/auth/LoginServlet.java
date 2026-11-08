package controller.auth;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

    private static final int MAX_USERNAME_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 255;

    private UserService userService;

    @Override
    public void init() throws ServletException {

        try {

            userService = new UserServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo UserService.",
                    exception);
        }
    }

    /*
     * =========================================================
     * GET /login
     * =========================================================
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        HttpSession session =
                request.getSession(false);

        if (session != null) {

            Object sessionUser =
                    session.getAttribute("user");

            if (sessionUser instanceof User) {

                User user =
                        (User) sessionUser;

                /*
                 * Nếu user trong session vẫn hợp lệ
                 * thì không hiển thị login nữa.
                 */
                if (isActive(user)
                        && isValidRole(user.getRole())) {

                    redirectByRole(
                            request,
                            response,
                            user);

                    return;
                }

                /*
                 * Session chứa user không hợp lệ.
                 */
                session.invalidate();
            }
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/login.jsp")
                .forward(request, response);
    }

    /*
     * =========================================================
     * POST /login
     * =========================================================
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        String username =
                normalize(
                        request.getParameter("username"));

        String password =
                request.getParameter("password");

        if (password == null) {
            password = "";
        }

        /*
         * =====================================================
         * VALIDATE INPUT
         * =====================================================
         */

        if (username.isBlank()
                || password.isBlank()) {

            forwardLoginError(
                    request,
                    response,
                    username,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");

            return;
        }

        if (username.length() > MAX_USERNAME_LENGTH) {

            forwardLoginError(
                    request,
                    response,
                    "",
                    "Tên đăng nhập không hợp lệ.");

            return;
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {

            forwardLoginError(
                    request,
                    response,
                    username,
                    "Mật khẩu không hợp lệ.");

            return;
        }

        if (containsControlCharacter(username)) {

            forwardLoginError(
                    request,
                    response,
                    "",
                    "Tên đăng nhập chứa ký tự không hợp lệ.");

            return;
        }

        try {

            /*
             * UserService.login() chịu trách nhiệm
             * kiểm tra username + password.
             */
            User user =
                    userService.login(
                            username,
                            password);

            if (user == null) {

                forwardLoginError(
                        request,
                        response,
                        username,
                        "Tên đăng nhập hoặc mật khẩu không chính xác.");

                return;
            }

            /*
             * =================================================
             * KIỂM TRA TÀI KHOẢN
             * =================================================
             */

            if (!isActive(user)) {

                forwardLoginError(
                        request,
                        response,
                        username,
                        "Tài khoản đã bị khóa hoặc không còn hoạt động.");

                return;
            }

            if (!isValidRole(user.getRole())) {

                forwardLoginError(
                        request,
                        response,
                        username,
                        "Vai trò tài khoản không hợp lệ.");

                return;
            }

            /*
             * =================================================
             * LẤY URL MÀ AUTHFILTER ĐÃ LƯU
             *
             * Ví dụ:
             *
             * /MovieTicketManagement/booking/create?showtimeId=5
             *
             * Phải lấy trước khi invalidate session.
             * =================================================
             */

            HttpSession oldSession =
                    request.getSession(false);

            String redirectAfterLogin =
                    getRedirectAfterLogin(
                            oldSession);

            /*
             * Chống session fixation.
             */
            if (oldSession != null) {

                try {
                    oldSession.invalidate();
                } catch (IllegalStateException ignored) {
                }
            }

            /*
             * =================================================
             * TẠO SESSION MỚI
             * =================================================
             */

            HttpSession newSession =
                    request.getSession(true);

            newSession.setAttribute(
                    "user",
                    user);

            newSession.setMaxInactiveInterval(
                    SESSION_TIMEOUT_SECONDS);

            /*
             * =================================================
             * CUSTOMER QUAY LẠI TRANG ĐẶT VÉ
             * =================================================
             */

            if (isCustomer(user)
                    && isSafeCustomerRedirect(
                            request,
                            redirectAfterLogin)) {

                /*
                 * Xóa sau khi đã xử lý.
                 * Session mới hiện không chứa redirect nhưng
                 * giữ đoạn này để an toàn nếu về sau có lưu lại.
                 */
                newSession.removeAttribute(
                        "redirectAfterLogin");

                response.sendRedirect(
                        redirectAfterLogin);

                return;
            }

            /*
             * Nếu không có redirect hợp lệ
             * thì chuyển theo role.
             */
            redirectByRole(
                    request,
                    response,
                    user);

        } catch (Exception exception) {

            exception.printStackTrace();

            forwardLoginError(
                    request,
                    response,
                    username,
                    "Đã xảy ra lỗi hệ thống khi đăng nhập.");
        }
    }

    /*
     * =========================================================
     * LẤY redirectAfterLogin
     * =========================================================
     */
    private String getRedirectAfterLogin(
            HttpSession session) {

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute(
                        "redirectAfterLogin");

        if (!(value instanceof String)) {
            return null;
        }

        String redirectUrl =
                ((String) value).trim();

        return redirectUrl.isBlank()
                ? null
                : redirectUrl;
    }

    /*
     * =========================================================
     * CHỈ CHO PHÉP CUSTOMER REDIRECT VÀO URL NỘI BỘ
     * =========================================================
     */
    private boolean isSafeCustomerRedirect(
            HttpServletRequest request,
            String redirectUrl) {

        if (redirectUrl == null
                || redirectUrl.isBlank()) {

            return false;
        }

        /*
         * Chặn header injection.
         */
        if (redirectUrl.contains("\r")
                || redirectUrl.contains("\n")) {

            return false;
        }

        /*
         * Chặn URL kiểu //example.com
         */
        if (redirectUrl.startsWith("//")) {
            return false;
        }

        /*
         * Chặn absolute URL.
         */
        String lowerRedirect =
                redirectUrl.toLowerCase(
                        Locale.ROOT);

        if (lowerRedirect.startsWith("http://")
                || lowerRedirect.startsWith("https://")) {

            return false;
        }

        String contextPath =
                request.getContextPath();

        /*
         * AuthFilter lưu request.getRequestURI(),
         * nên URL thường có contextPath.
         *
         * Ví dụ:
         * /MovieTicketManagement/booking/create?showtimeId=5
         */
        if (redirectUrl.startsWith(
                contextPath + "/booking")) {

            return true;
        }

        if (redirectUrl.startsWith(
                contextPath + "/payment")) {

            return true;
        }

        if (redirectUrl.startsWith(
                contextPath + "/customer/tickets")) {

            return true;
        }

        if (redirectUrl.startsWith(
                contextPath + "/profile")) {

            return true;
        }

        return false;
    }

    /*
     * =========================================================
     * CHUYỂN TRANG THEO ROLE
     * =========================================================
     */
    private void redirectByRole(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        String role =
                normalizeUpper(
                        user == null
                                ? null
                                : user.getRole());

        switch (role) {

            case "ADMIN":

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/dashboard");

                break;

            case "STAFF":

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/dashboard");

                break;

            case "CUSTOMER":

                response.sendRedirect(
                        request.getContextPath()
                                + "/home");

                break;

            default:

                /*
                 * Trường hợp role bất thường.
                 */
                HttpSession session =
                        request.getSession(false);

                if (session != null) {

                    try {
                        session.invalidate();
                    } catch (IllegalStateException ignored) {
                    }
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/login?error=invalid_role");

                break;
        }
    }

    /*
     * =========================================================
     * FORWARD LỖI LOGIN
     * =========================================================
     */
    private void forwardLoginError(
            HttpServletRequest request,
            HttpServletResponse response,
            String username,
            String message)
            throws ServletException, IOException {

        request.setAttribute(
                "errorMessage",
                message);

        request.setAttribute(
                "username",
                username == null
                        ? ""
                        : username);

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/login.jsp")
                .forward(request, response);
    }

    /*
     * =========================================================
     * ROLE
     * =========================================================
     */
    private boolean isCustomer(
            User user) {

        return user != null
                && "CUSTOMER".equals(
                        normalizeUpper(
                                user.getRole()));
    }

    private boolean isValidRole(
            String role) {

        String normalizedRole =
                normalizeUpper(role);

        return "CUSTOMER".equals(normalizedRole)
                || "STAFF".equals(normalizedRole)
                || "ADMIN".equals(normalizedRole);
    }

    /*
     * =========================================================
     * STATUS
     * =========================================================
     */
    private boolean isActive(
            User user) {

        return user != null
                && "ACTIVE".equals(
                        normalizeUpper(
                                user.getStatus()));
    }

    /*
     * =========================================================
     * INPUT
     * =========================================================
     */
    private boolean containsControlCharacter(
            String value) {

        if (value == null) {
            return false;
        }

        for (int index = 0;
                index < value.length();
                index++) {

            if (Character.isISOControl(
                    value.charAt(index))) {

                return true;
            }
        }

        return false;
    }

    private String normalizeUpper(
            String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    /*
     * =========================================================
     * RESPONSE
     * =========================================================
     */
    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding(
                "UTF-8");

        response.setCharacterEncoding(
                "UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate");

        response.setHeader(
                "Pragma",
                "no-cache");

        response.setDateHeader(
                "Expires",
                0);
    }
}