package controller.staff.payments;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import model.PaymentStatistic;
import service.PaymentService;
import service.impl.PaymentServiceImpl;
import utils.StaffAuthorization;

@WebServlet("/staff/payments/statistics")
public class PaymentStatisticsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final long MAX_RANGE_DAYS = 366;

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

        LocalDate today = LocalDate.now();
        LocalDate defaultFromDate =
                today.minusDays(29);

        DateInput fromInput =
                parseDateInput(
                        request.getParameter("fromDate"),
                        defaultFromDate);

        DateInput toInput =
                parseDateInput(
                        request.getParameter("toDate"),
                        today);

        LocalDate fromLocalDate = fromInput.value;
        LocalDate toLocalDate = toInput.value;

        if (!fromInput.valid || !toInput.valid) {

            request.setAttribute(
                    "errorMessage",
                    "Ngày nhập không đúng định dạng yyyy-MM-dd.");
        }

        if (fromLocalDate.isAfter(toLocalDate)) {

            request.setAttribute(
                    "errorMessage",
                    "Ngày bắt đầu không được lớn hơn ngày kết thúc.");

            fromLocalDate = defaultFromDate;
            toLocalDate = today;
        }

        long rangeDays =
                java.time.temporal.ChronoUnit.DAYS
                        .between(
                                fromLocalDate,
                                toLocalDate);

        if (rangeDays > MAX_RANGE_DAYS) {

            request.setAttribute(
                    "errorMessage",
                    "Khoảng thống kê tối đa là 366 ngày.");

            fromLocalDate =
                    toLocalDate.minusDays(
                            MAX_RANGE_DAYS);
        }

        Date fromDate = Date.valueOf(fromLocalDate);
        Date toDate = Date.valueOf(toLocalDate);

        try {

            List<Payment> payments =
                    paymentService
                            .getStaffPaymentsByDateRange(
                                    fromDate,
                                    toDate);

            List<PaymentStatistic> dailyStatistics =
                    paymentService
                            .getDailyPaymentStatistics(
                                    fromDate,
                                    toDate);

            List<PaymentStatistic> methodStatistics =
                    paymentService
                            .getPaymentMethodStatistics(
                                    fromDate,
                                    toDate);

            if (payments == null) {
                payments = Collections.emptyList();
            }

            if (dailyStatistics == null) {
                dailyStatistics =
                        Collections.emptyList();
            }

            if (methodStatistics == null) {
                methodStatistics =
                        Collections.emptyList();
            }

            request.setAttribute("payments", payments);
            request.setAttribute(
                    "dailyStatistics",
                    dailyStatistics);
            request.setAttribute(
                    "methodStatistics",
                    methodStatistics);

            request.setAttribute(
                    "totalPayments",
                    payments.size());

            request.setAttribute(
                    "totalRevenue",
                    safeRevenue(
                            paymentService.getPaidRevenue(
                                    fromDate,
                                    toDate)));

            request.setAttribute(
                    "paidCount",
                    paymentService
                            .countPaymentsByStatusAndDateRange(
                                    "PAID",
                                    fromDate,
                                    toDate));

            request.setAttribute(
                    "pendingCount",
                    paymentService
                            .countPaymentsByStatusAndDateRange(
                                    "PENDING",
                                    fromDate,
                                    toDate));

            request.setAttribute(
                    "failedCount",
                    paymentService
                            .countPaymentsByStatusAndDateRange(
                                    "FAILED",
                                    fromDate,
                                    toDate));

            request.setAttribute(
                    "cancelledCount",
                    paymentService
                            .countPaymentsByStatusAndDateRange(
                                    "CANCELLED",
                                    fromDate,
                                    toDate));

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải thống kê thanh toán.");

            setEmptyStatistics(request);
        }

        request.setAttribute(
                "fromDate",
                fromLocalDate.toString());

        request.setAttribute(
                "toDate",
                toLocalDate.toString());

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/payments/statistics.jsp")
                .forward(request, response);
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
                    "Bạn không có quyền xem thống kê thanh toán.");

            return false;
        }

        return true;
    }

    private DateInput parseDateInput(
            String value,
            LocalDate defaultValue) {

        if (value == null || value.isBlank()) {
            return new DateInput(defaultValue, true);
        }

        try {

            return new DateInput(
                    LocalDate.parse(value.trim()),
                    true);

        } catch (DateTimeParseException exception) {

            return new DateInput(
                    defaultValue,
                    false);
        }
    }

    private BigDecimal safeRevenue(BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    private void setEmptyStatistics(
            HttpServletRequest request) {

        request.setAttribute(
                "payments",
                Collections.emptyList());

        request.setAttribute(
                "dailyStatistics",
                Collections.emptyList());

        request.setAttribute(
                "methodStatistics",
                Collections.emptyList());

        request.setAttribute("totalPayments", 0);
        request.setAttribute(
                "totalRevenue",
                BigDecimal.ZERO);
        request.setAttribute("paidCount", 0);
        request.setAttribute("pendingCount", 0);
        request.setAttribute("failedCount", 0);
        request.setAttribute("cancelledCount", 0);
    }

    private static final class DateInput {

        private final LocalDate value;
        private final boolean valid;

        private DateInput(
                LocalDate value,
                boolean valid) {

            this.value = value;
            this.valid = valid;
        }
    }
}
