package controller.staff.customers;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

@WebServlet("/staff/customers/edit")
public class CustomerEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9,11}$");

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
        response.setContentType("text/html; charset=UTF-8");

        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        if (!canManageCustomers(loggedInUser)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này.");

            return;
        }

        Integer customerId =
                parsePositiveInteger(
                        request.getParameter("id"));

        if (customerId == null) {

            redirectToList(
                    request,
                    response,
                    "error=invalid_id");

            return;
        }

        User customer =
                userService.findById(customerId);

        if (customer == null) {

            redirectToList(
                    request,
                    response,
                    "error=not_found");

            return;
        }

        if (!isCustomer(customer)) {

            redirectToList(
                    request,
                    response,
                    "error=not_customer");

            return;
        }

        request.setAttribute("customer", customer);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/customers/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

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

        if (customerId == null) {

            redirectToList(
                    request,
                    response,
                    "error=invalid_id");

            return;
        }

        User customer =
                userService.findById(customerId);

        if (customer == null) {

            redirectToList(
                    request,
                    response,
                    "error=not_found");

            return;
        }

        if (!isCustomer(customer)) {

            redirectToList(
                    request,
                    response,
                    "error=not_customer");

            return;
        }

        /*
         * Chỉ nhận các trường được phép sửa.
         * Không nhận username, role, password, status.
         */
        String fullName =
                normalize(
                        request.getParameter("fullName"));

        String email =
                normalize(
                        request.getParameter("email"))
                        .toLowerCase();

        String phone =
                normalize(
                        request.getParameter("phone"));

        String validationError =
                validateCustomerData(
                        fullName,
                        email,
                        phone,
                        customerId);

        if (validationError != null) {

            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);

            request.setAttribute(
                    "customer",
                    customer);

            request.setAttribute(
                    "errorMessage",
                    validationError);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/customers/edit.jsp")
                    .forward(request, response);

            return;
        }

        boolean updated =
                userService.updateCustomer(
                        customerId,
                        fullName,
                        email,
                        phone);

        if (!updated) {

            customer.setFullName(fullName);
            customer.setEmail(email);
            customer.setPhone(phone);

            request.setAttribute(
                    "customer",
                    customer);

            request.setAttribute(
                    "errorMessage",
                    "Cập nhật thông tin khách hàng thất bại.");

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/customers/edit.jsp")
                    .forward(request, response);

            return;
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/customers/detail"
                        + "?id="
                        + customerId
                        + "&success=updated");
    }

    private String validateCustomerData(
            String fullName,
            String email,
            String phone,
            Integer userId) {

        if (fullName.isBlank()) {
            return "Vui lòng nhập họ và tên.";
        }

        if (fullName.length() > 100) {
            return "Họ và tên không được vượt quá 100 ký tự.";
        }

        if (email.isBlank()) {
            return "Vui lòng nhập email.";
        }

        if (email.length() > 150) {
            return "Email không được vượt quá 150 ký tự.";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Địa chỉ email không hợp lệ.";
        }

        if (userService.checkEmailExistsExceptUser(
                email,
                userId)) {

            return "Email đã được sử dụng bởi tài khoản khác.";
        }

        if (!phone.isBlank()
                && !PHONE_PATTERN.matcher(phone).matches()) {

            return "Số điện thoại phải gồm từ 9 đến 11 chữ số.";
        }

        return null;
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
            String parameter)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/customers?"
                        + parameter);
    }
}