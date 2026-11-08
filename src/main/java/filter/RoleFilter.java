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
    "/admin/*",
    "/staff/*",
    "/booking/*",
    "/payment/*",
    "/customer/tickets/*"
})
public class RoleFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        Object sessionUser =
                session.getAttribute("user");

        if (!(sessionUser instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        User user =
                (User) sessionUser;

        String role =
                user.getRole() == null
                        ? ""
                        : user.getRole()
                                .trim()
                                .toUpperCase();

        String uri =
                request.getRequestURI()
                        .substring(
                                request.getContextPath()
                                        .length());

        /*
         * ================================================
         * ADMIN
         * ================================================
         */

        if (uri.startsWith("/admin/")) {

            if (!"ADMIN".equals(role)) {

                redirectForbiddenByRole(
                        request,
                        response,
                        role);

                return;
            }

            chain.doFilter(
                    request,
                    response);

            return;
        }

        /*
         * ================================================
         * STAFF
         * ================================================
         */

        if (uri.startsWith("/staff/")) {

            if (!"STAFF".equals(role)
                    && !"ADMIN".equals(role)) {

                redirectForbiddenByRole(
                        request,
                        response,
                        role);

                return;
            }

            chain.doFilter(
                    request,
                    response);

            return;
        }

        /*
         * ================================================
         * CUSTOMER BOOKING
         * ================================================
         */

        if (uri.startsWith("/booking/")
                || uri.equals("/booking")) {

            if (!"CUSTOMER".equals(role)) {

                redirectForbiddenByRole(
                        request,
                        response,
                        role);

                return;
            }

            chain.doFilter(
                    request,
                    response);

            return;
        }

        /*
         * ================================================
         * CUSTOMER PAYMENT
         * ================================================
         */

        if (uri.startsWith("/payment/")
                || uri.equals("/payment")) {

            if (!"CUSTOMER".equals(role)) {

                redirectForbiddenByRole(
                        request,
                        response,
                        role);

                return;
            }

            chain.doFilter(
                    request,
                    response);

            return;
        }

        /*
         * ================================================
         * CUSTOMER TICKETS
         * ================================================
         */

        if (uri.startsWith(
                "/customer/tickets")) {

            if (!"CUSTOMER".equals(role)) {

                redirectForbiddenByRole(
                        request,
                        response,
                        role);

                return;
            }

            chain.doFilter(
                    request,
                    response);

            return;
        }

        chain.doFilter(
                request,
                response);
    }

    private void redirectForbiddenByRole(
            HttpServletRequest request,
            HttpServletResponse response,
            String role)
            throws IOException {

        switch (role) {

            case "ADMIN":

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/dashboard?error=forbidden");

                break;

            case "STAFF":

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/dashboard?error=forbidden");

                break;

            case "CUSTOMER":

                response.sendRedirect(
                        request.getContextPath()
                                + "/home?error=forbidden");

                break;

            default:

                response.sendRedirect(
                        request.getContextPath()
                                + "/login");

                break;
        }
    }
}