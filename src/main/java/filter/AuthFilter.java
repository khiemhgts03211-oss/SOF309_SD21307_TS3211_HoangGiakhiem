package filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;

@WebFilter({
    "/profile",
    "/profile/*",

    "/booking",
    "/booking/*",

    "/payment",
    "/payment/*",

    "/customer/tickets",
    "/customer/tickets/*",

    "/admin/*",
    "/staff/*"
})
public class AuthFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

        Object sessionUser =
                session == null
                        ? null
                        : session.getAttribute("user");

        /*
         * Chưa đăng nhập
         */
        if (!(sessionUser instanceof User)) {

            saveRedirectAfterLogin(request);

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User user =
                (User) sessionUser;

        /*
         * Tài khoản không còn ACTIVE
         */
        if (!isActive(user)) {

            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?error=account_inactive");

            return;
        }

        chain.doFilter(request, response);
    }

    private void saveRedirectAfterLogin(
            HttpServletRequest request) {

        /*
         * Không cần lưu redirect cho admin/staff.
         * Quan trọng nhất là customer booking/payment.
         */

        String requestUri =
                request.getRequestURI();

        String queryString =
                request.getQueryString();

        String redirectUrl =
                requestUri;

        if (queryString != null
                && !queryString.isBlank()) {

            redirectUrl += "?" + queryString;
        }

        request.getSession(true)
                .setAttribute(
                        "redirectAfterLogin",
                        redirectUrl);
    }

    private boolean isActive(
            User user) {

        return user != null
                && user.getStatus() != null
                && "ACTIVE".equalsIgnoreCase(
                        user.getStatus().trim());
    }
}