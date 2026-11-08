package controller.staff.customers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/staff/customers")
public class CustomerListServlet extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setCharacterEncoding("UTF-8");

        /*
         * ==============================
         * KIỂM TRA ĐĂNG NHẬP
         * ==============================
         */

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        /*
         * Project hiện tại lưu người dùng đăng nhập
         * bằng session attribute "user".
         */
        User loggedInUser =
                (User) session.getAttribute("user");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login");

            return;
        }

        /*
         * ==============================
         * NHẬN THAM SỐ TÌM KIẾM/LỌC
         * ==============================
         */

        String keyword =
                normalize(
                        request.getParameter(
                                "keyword"));

        String status =
                normalize(
                        request.getParameter(
                                "status"));

        /*
         * Chỉ chấp nhận trạng thái hợp lệ.
         * Nếu status không hợp lệ thì xem như không lọc.
         */
        if (!isValidStatus(status)) {

            status = "";
        }

        try {

            List<User> customers;

            /*
             * Tìm kiếm kết hợp trạng thái.
             */
            if (!keyword.isBlank()
                    && !status.isBlank()) {

                customers =
                        userService.searchCustomers(
                                keyword,
                                status);

            /*
             * Chỉ tìm theo từ khóa.
             */
            } else if (!keyword.isBlank()) {

                customers =
                        userService.searchCustomers(
                                keyword);

            /*
             * Chỉ lọc theo trạng thái.
             */
            } else if (!status.isBlank()) {

                customers =
                        userService
                                .getCustomersByStatus(
                                        status);

            /*
             * Không có điều kiện:
             * lấy toàn bộ khách hàng.
             */
            } else {

                customers =
                        userService.getCustomers();
            }

            if (customers == null) {

                customers =
                        Collections.emptyList();
            }

            /*
             * ==============================
             * GỬI DỮ LIỆU SANG JSP
             * ==============================
             */

            request.setAttribute(
                    "customers",
                    customers);

            request.setAttribute(
                    "totalCustomers",
                    customers.size());

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "selectedStatus",
                    status);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/"
                    + "customers/list.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách khách hàng.");

            request.setAttribute(
                    "customers",
                    Collections.emptyList());

            request.setAttribute(
                    "totalCustomers",
                    0);

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "selectedStatus",
                    status);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/"
                    + "customers/list.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Chuẩn hóa tham số nhận từ request.
     *
     * Không trả về null để có thể gọi isBlank()
     * mà không phát sinh NullPointerException.
     */
    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    /**
     * Kiểm tra trạng thái lọc hợp lệ.
     */
    private boolean isValidStatus(String status) {

        if (status == null
                || status.isBlank()) {

            return true;
        }

        return "ACTIVE".equalsIgnoreCase(status)
                || "INACTIVE".equalsIgnoreCase(status)
                || "BLOCKED".equalsIgnoreCase(status);
    }
}