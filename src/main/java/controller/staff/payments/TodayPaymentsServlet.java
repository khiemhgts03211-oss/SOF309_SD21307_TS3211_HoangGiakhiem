package controller.staff.payments;

import java.io.IOException;
import java.math.BigDecimal;
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

@WebServlet("/staff/payments/today")
public class TodayPaymentsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(
                "text/html; charset=UTF-8");

        if (!authorize(request, response)) {
            return;
        }

        try {

            List<Payment> payments =
                    paymentService.getTodayPayments();

            if (payments == null) {
                payments = Collections.emptyList();
            }

            setStatistics(request, payments);

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách thanh toán hôm nay.");

            setStatistics(
                    request,
                    Collections.emptyList());
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/payments/today.jsp")
                .forward(request, response);
    }

    private void setStatistics(
            HttpServletRequest request,
            List<Payment> payments) {

        long paidCount = 0;
        long pendingCount = 0;
        long failedCount = 0;
        long cancelledCount = 0;

        BigDecimal todayRevenue = BigDecimal.ZERO;

        for (Payment payment : payments) {

            if (payment == null) {
                continue;
            }

            String status =
                    normalizeUpper(payment.getStatus());

            switch (status) {

                case "PAID":
                    paidCount++;

                    if (payment.getAmount() != null
                            && payment.getAmount().signum() > 0) {

                        todayRevenue =
                                todayRevenue.add(
                                        payment.getAmount());
                    }

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
        request.setAttribute(
                "todayRevenue",
                todayRevenue);
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

    private String normalizeUpper(String value) {

        return value == null
                ? ""
                : value.trim()
                        .toUpperCase(Locale.ROOT);
    }
}
