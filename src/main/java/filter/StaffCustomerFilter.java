package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import utils.StaffAuthorization;

@WebFilter("/staff/customers/*")
public class StaffCustomerFilter implements Filter {

    @Override
    public void init(
            FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        /*
         * Chưa đăng nhập.
         */
        if (!StaffAuthorization.isLoggedIn(request)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        /*
         * Không phải STAFF hoặc ADMIN.
         */
        if (!StaffAuthorization
                .canManageCustomers(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này.");

            return;
        }

        chain.doFilter(
                servletRequest,
                servletResponse);
    }

    @Override
    public void destroy() {
    }
}