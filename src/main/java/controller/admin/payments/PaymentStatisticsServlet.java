package controller.admin.payments;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/admin/payments/statistics")
public class PaymentStatisticsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Tổng số Payment
        request.setAttribute(
                "totalPayments",
                paymentService.countPayments());

        // Số Payment đã thanh toán
        request.setAttribute(
                "paidPayments",
                paymentService.countPaidPayments());

        // Số Payment đang chờ
        request.setAttribute(
                "pendingPayments",
                paymentService.countPendingPayments());

        // Tổng doanh thu
        request.setAttribute(
                "totalRevenue",
                paymentService.getTotalRevenue());

        // Doanh thu
        request.setAttribute(
                "todayRevenue",
                paymentService.getRevenueByDate(
                        new java.sql.Date(System.currentTimeMillis())));

        java.time.LocalDate now = java.time.LocalDate.now();

        request.setAttribute(
                "monthRevenue",
                paymentService.getRevenueByMonth(
                        now.getMonthValue(),
                        now.getYear()));

        request.setAttribute(
                "yearRevenue",
                paymentService.getRevenueByYear(
                        now.getYear()));

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/payments/statistics.jsp")
                .forward(request, response);
    }
}