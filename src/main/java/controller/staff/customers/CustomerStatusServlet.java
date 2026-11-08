package controller.staff.customers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/staff/customers/status")
public class CustomerStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {

        userService = new UserServiceImpl();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        if (!canManageCustomers(loggedInUser)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền thực hiện thao tác này.");

            return;
        }

        Integer customerId =
                parsePositiveInteger(
                        request.getParameter("userId"));

        String requestedStatus =
                normalize(
                        request.getParameter("status"))
                        .toUpperCase();

        String returnPage =
                normalize(
                        request.getParameter("returnPage"));

        String keyword =
                normalize(
                        request.getParameter("keyword"));

        String selectedStatus =
                normalize(
                        request.getParameter("selectedStatus"));

        if (customerId == null) {

            redirectToList(
                    request,
                    response,
                    keyword,
                    selectedStatus,
                    "error=invalid_id");

            return;
        }

        /*
         * Form chỉ được yêu cầu khóa hoặc mở khóa.
         */
        if (!"LOCKED".equals(requestedStatus)
                && !"ACTIVE".equals(requestedStatus)) {

            redirectAfterUpdate(
                    request,
                    response,
                    customerId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    "error=invalid_status");

            return;
        }

        try {

            User customer =
                    userService.findById(customerId);

            if (customer == null) {

                redirectToList(
                        request,
                        response,
                        keyword,
                        selectedStatus,
                        "error=not_found");

                return;
            }

            if (!isCustomer(customer)) {

                redirectToList(
                        request,
                        response,
                        keyword,
                        selectedStatus,
                        "error=not_customer");

                return;
            }

            String currentStatus =
                    normalize(customer.getStatus())
                            .toUpperCase();

            if ("LOCKED".equals(requestedStatus)
                    && "LOCKED".equals(currentStatus)) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=already_locked");

                return;
            }

            if ("ACTIVE".equals(requestedStatus)
                    && "ACTIVE".equals(currentStatus)) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=already_active");

                return;
            }

            if ("LOCKED".equals(requestedStatus)
                    && !"ACTIVE".equals(currentStatus)) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=cannot_lock");

                return;
            }

            if ("ACTIVE".equals(requestedStatus)
                    && !"LOCKED".equals(currentStatus)
                    && !"INACTIVE".equals(currentStatus)) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=cannot_unlock");

                return;
            }

            boolean updated =
                    userService.updateCustomerStatus(
                            customerId,
                            requestedStatus);

            if (!updated) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=update_failed");

                return;
            }

            User updatedCustomer =
                    userService.findById(customerId);

            if (updatedCustomer == null
                    || !requestedStatus.equalsIgnoreCase(
                            updatedCustomer.getStatus())) {

                redirectAfterUpdate(
                        request,
                        response,
                        customerId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        "error=update_failed");

                return;
            }

            String result =
                    "LOCKED".equals(requestedStatus)
                            ? "success=locked"
                            : "success=unlocked";

            redirectAfterUpdate(
                    request,
                    response,
                    customerId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    result);

        } catch (Exception exception) {

            exception.printStackTrace();

            redirectAfterUpdate(
                    request,
                    response,
                    customerId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    "error=server_error");
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/customers");
    }

    private User getLoggedInUser(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value =
                session.getAttribute("user");

        return value instanceof User
                ? (User) value
                : null;
    }

    private boolean canManageCustomers(User user) {

        if (user == null || user.getRole() == null) {
            return false;
        }

        String role =
                String.valueOf(user.getRole()).trim();

        return "STAFF".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }

    private boolean isCustomer(User user) {

        return user != null
                && user.getRole() != null
                && "CUSTOMER".equalsIgnoreCase(
                        String.valueOf(
                                user.getRole()).trim());
    }

    private Integer parsePositiveInteger(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            int id = Integer.parseInt(value.trim());

            return id > 0 ? id : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response,
            String keyword,
            String selectedStatus,
            String result)
            throws IOException {

        StringBuilder url =
                new StringBuilder(
                        request.getContextPath()
                                + "/staff/customers?");

        boolean hasParameter = false;

        if (!keyword.isBlank()) {

            url.append("keyword=")
                    .append(encode(keyword));

            hasParameter = true;
        }

        if (!selectedStatus.isBlank()) {

            if (hasParameter) {
                url.append("&");
            }

            url.append("status=")
                    .append(encode(selectedStatus));

            hasParameter = true;
        }

        if (hasParameter) {
            url.append("&");
        }

        url.append(result);

        response.sendRedirect(url.toString());
    }

    private void redirectAfterUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            int customerId,
            String returnPage,
            String keyword,
            String selectedStatus,
            String result)
            throws IOException {

        if ("detail".equalsIgnoreCase(returnPage)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/customers/detail"
                            + "?id="
                            + customerId
                            + "&"
                            + result);

            return;
        }

        redirectToList(
                request,
                response,
                keyword,
                selectedStatus,
                result);
    }

    private String encode(String value) {

        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8);
    }
}