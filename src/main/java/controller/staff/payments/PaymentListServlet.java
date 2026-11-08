package controller.staff.payments;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;
import utils.StaffAuthorization;

@WebServlet("/staff/payments")
public class PaymentListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_KEYWORD_LENGTH = 150;

    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {
        paymentService = new PaymentServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        if (!authorize(request, response)) {
            return;
        }

        String keyword =
                normalize(request.getParameter("keyword"));

        String selectedStatus =
                normalizeUpper(
                        request.getParameter("status"));

        String selectedMethod =
                normalizeUpper(
                        request.getParameter("method"));

        if (keyword.length() > MAX_KEYWORD_LENGTH) {

            keyword = keyword.substring(
                    0,
                    MAX_KEYWORD_LENGTH);

            request.setAttribute(
                    "warningMessage",
                    "Từ khóa đã được rút gọn còn 150 ký tự.");
        }

        if (!isValidStatus(selectedStatus)) {

            selectedStatus = "";

            request.setAttribute(
                    "warningMessage",
                    "Trạng thái lọc không hợp lệ.");
        }

        if (!isValidMethod(selectedMethod)) {

            selectedMethod = "";

            request.setAttribute(
                    "warningMessage",
                    "Phương thức thanh toán không hợp lệ.");
        }

        try {

            List<Payment> payments =
                    paymentService
                            .searchAndFilterStaffPayments(
                                    keyword,
                                    selectedStatus,
                                    selectedMethod);

            if (payments == null) {
                payments = Collections.emptyList();
            }

            setListAttributes(
                    request,
                    payments,
                    keyword,
                    selectedStatus,
                    selectedMethod);

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách thanh toán.");

            setListAttributes(
                    request,
                    Collections.emptyList(),
                    keyword,
                    selectedStatus,
                    selectedMethod);
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/payments/list.jsp")
                .forward(request, response);
    }

    private void setListAttributes(
            HttpServletRequest request,
            List<Payment> payments,
            String keyword,
            String selectedStatus,
            String selectedMethod) {

        long paidCount = 0;
        long pendingCount = 0;
        long failedCount = 0;
        long cancelledCount = 0;

        for (Payment payment : payments) {

            if (payment == null
                    || payment.getStatus() == null) {

                continue;
            }

            switch (normalizeUpper(payment.getStatus())) {

                case "PAID":
                    paidCount++;
                    break;

                case "PENDING":
                    pendingCount++;
                    break;

                case "FAILED":
                    failedCount++;
                    break;

                case "CANCELLED":
                    cancelledCount++;
                    break;

                default:
                    break;
            }
        }

        request.setAttribute("payments", payments);
        request.setAttribute("keyword", keyword);
        request.setAttribute(
                "selectedStatus",
                selectedStatus);
        request.setAttribute(
                "selectedMethod",
                selectedMethod);

        request.setAttribute(
                "totalPayments",
                payments.size());
        request.setAttribute("paidCount", paidCount);
        request.setAttribute(
                "pendingCount",
                pendingCount);
        request.setAttribute(
                "failedCount",
                failedCount);
        request.setAttribute(
                "cancelledCount",
                cancelledCount);
    }

    private boolean authorize(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        if (!StaffAuthorization.isLoggedIn(request)) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return false;
        }

        if (!StaffAuthorization.canManagePayments(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này.");

            return false;
        }

        return true;
    }

    private boolean isValidStatus(String status) {

        return status.isBlank()
                || "PENDING".equals(status)
                || "PAID".equals(status)
                || "FAILED".equals(status)
                || "CANCELLED".equals(status);
    }

    private boolean isValidMethod(String method) {

        return method.isBlank()
                || "CASH".equals(method)
                || "BANKING".equals(method)
                || "MOMO".equals(method)
                || "VNPAY".equals(method);
    }

    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(
                "text/html; charset=UTF-8");
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeUpper(String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }
}
