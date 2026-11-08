package controller.staff.payments;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import model.Ticket;
import service.PaymentService;
import service.TicketService;
import service.impl.PaymentServiceImpl;
import service.impl.TicketServiceImpl;
import utils.StaffAuthorization;

@WebServlet("/staff/payments/status")
public class PaymentStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_PAID = "PAID";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_FAILED = "FAILED";

    private PaymentService paymentService;
    private TicketService ticketService;

    @Override
    public void init() throws ServletException {
        paymentService = new PaymentServiceImpl();
        ticketService = new TicketServiceImpl();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        /*
         * =========================================
         * KIỂM TRA ĐĂNG NHẬP VÀ PHÂN QUYỀN
         * =========================================
         */

        if (!authorize(request, response)) {
            return;
        }

        /*
         * =========================================
         * NHẬN THAM SỐ
         * =========================================
         */

        Integer paymentId =
                parsePositiveInteger(
                        request.getParameter("paymentId"));

        String requestedStatus =
                normalizeUpper(
                        request.getParameter("status"));

        String returnPage =
                normalizeLower(
                        request.getParameter("returnPage"));

        String keyword =
                normalize(
                        request.getParameter("keyword"));

        String selectedStatus =
                normalizeUpper(
                        request.getParameter("selectedStatus"));

        String selectedMethod =
                normalizeUpper(
                        request.getParameter("selectedMethod"));

        /*
         * =========================================
         * KIỂM TRA PAYMENT ID
         * =========================================
         */

        if (paymentId == null) {

            redirectToList(
                    request,
                    response,
                    keyword,
                    selectedStatus,
                    selectedMethod,
                    "error=invalid_id");

            return;
        }

        /*
         * Chỉ cho phép chuyển sang:
         * PAID, CANCELLED hoặc FAILED.
         */
        if (!isAllowedTargetStatus(requestedStatus)) {

            redirectAfterUpdate(
                    request,
                    response,
                    paymentId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    selectedMethod,
                    "error=invalid_status");

            return;
        }

        try {

            /*
             * =========================================
             * LẤY PAYMENT
             * =========================================
             */

            Payment payment =
                    paymentService.getPaymentById(paymentId);

            if (payment == null) {

                redirectAfterUpdate(
                        request,
                        response,
                        paymentId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        selectedMethod,
                        "error=not_found");

                return;
            }

            String currentStatus =
                    normalizeUpper(payment.getStatus());

            /*
             * =========================================
             * KIỂM TRA TRẠNG THÁI HIỆN TẠI
             * =========================================
             */

            String currentStatusError =
                    getCurrentStatusError(currentStatus);

            if (currentStatusError != null) {

                redirectAfterUpdate(
                        request,
                        response,
                        paymentId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        selectedMethod,
                        currentStatusError);

                return;
            }

            /*
             * Chỉ payment PENDING mới được thay đổi.
             */
            if (!STATUS_PENDING.equals(currentStatus)) {

                redirectAfterUpdate(
                        request,
                        response,
                        paymentId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        selectedMethod,
                        getTransitionError(requestedStatus));

                return;
            }

            /*
             * =========================================
             * KIỂM TRA NGHIỆP VỤ KHI XÁC NHẬN PAID
             * =========================================
             */

            Ticket ticket = null;

            if (STATUS_PAID.equals(requestedStatus)) {

                /*
                 * payment.getTicketId() là int,
                 * không được so sánh với null.
                 */
                if (payment.getTicketId() <= 0) {

                    redirectAfterUpdate(
                            request,
                            response,
                            paymentId,
                            returnPage,
                            keyword,
                            selectedStatus,
                            selectedMethod,
                            "error=ticket_not_found");

                    return;
                }

                ticket =
                        ticketService.getTicketById(
                                payment.getTicketId());

                if (ticket == null) {

                    redirectAfterUpdate(
                            request,
                            response,
                            paymentId,
                            returnPage,
                            keyword,
                            selectedStatus,
                            selectedMethod,
                            "error=ticket_not_found");

                    return;
                }

                String ticketStatus =
                        normalizeUpper(ticket.getStatus());

                if (STATUS_CANCELLED.equals(ticketStatus)) {

                    redirectAfterUpdate(
                            request,
                            response,
                            paymentId,
                            returnPage,
                            keyword,
                            selectedStatus,
                            selectedMethod,
                            "error=ticket_cancelled");

                    return;
                }

                /*
                 * Số tiền payment phải lớn hơn 0.
                 */
                if (payment.getAmount() == null
                        || payment.getAmount().signum() <= 0) {

                    redirectAfterUpdate(
                            request,
                            response,
                            paymentId,
                            returnPage,
                            keyword,
                            selectedStatus,
                            selectedMethod,
                            "error=invalid_amount");

                    return;
                }

                /*
                 * Số tiền thanh toán không được nhỏ hơn
                 * tổng tiền của vé.
                 */
                if (ticket.getTotalPrice() != null
                        && payment.getAmount()
                                .compareTo(ticket.getTotalPrice()) < 0) {

                    redirectAfterUpdate(
                            request,
                            response,
                            paymentId,
                            returnPage,
                            keyword,
                            selectedStatus,
                            selectedMethod,
                            "error=insufficient_amount");

                    return;
                }
            }

            /*
             * =========================================
             * CẬP NHẬT TRẠNG THÁI PAYMENT
             * =========================================
             */

            boolean paymentUpdated =
                    paymentService.updatePaymentStatus(
                            paymentId,
                            requestedStatus);

            if (!paymentUpdated) {

                redirectAfterUpdate(
                        request,
                        response,
                        paymentId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        selectedMethod,
                        "error=update_failed");

                return;
            }

            /*
             * =========================================
             * CẬP NHẬT TICKET KHI PAYMENT PAID
             * =========================================
             */

            if (STATUS_PAID.equals(requestedStatus)
                    && ticket != null) {

                String ticketStatus =
                        normalizeUpper(ticket.getStatus());

                if ("BOOKED".equals(ticketStatus)) {

                    ticketService.updateStatus(
                            ticket.getTicketId(),
                            STATUS_PAID);

                    Ticket updatedTicket =
                            ticketService.getTicketById(
                                    ticket.getTicketId());

                    if (updatedTicket == null
                            || !STATUS_PAID.equalsIgnoreCase(
                                    updatedTicket.getStatus())) {

                        redirectAfterUpdate(
                                request,
                                response,
                                paymentId,
                                returnPage,
                                keyword,
                                selectedStatus,
                                selectedMethod,
                                "error=ticket_update_failed");

                        return;
                    }
                }
            }

            /*
             * =========================================
             * KIỂM TRA KẾT QUẢ SAU CẬP NHẬT
             * =========================================
             */

            Payment updatedPayment =
                    paymentService.getPaymentById(paymentId);

            if (updatedPayment == null
                    || !requestedStatus.equalsIgnoreCase(
                            updatedPayment.getStatus())) {

                redirectAfterUpdate(
                        request,
                        response,
                        paymentId,
                        returnPage,
                        keyword,
                        selectedStatus,
                        selectedMethod,
                        "error=update_failed");

                return;
            }

            /*
             * =========================================
             * THÀNH CÔNG
             * =========================================
             */

            redirectAfterUpdate(
                    request,
                    response,
                    paymentId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    selectedMethod,
                    getSuccessParameter(requestedStatus));

        } catch (Exception exception) {

            exception.printStackTrace();

            redirectAfterUpdate(
                    request,
                    response,
                    paymentId,
                    returnPage,
                    keyword,
                    selectedStatus,
                    selectedMethod,
                    "error=server_error");
        }
    }

    /*
     * Không cho thay đổi trạng thái bằng GET.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/staff/payments");
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
                    "Bạn không có quyền thực hiện thao tác này.");

            return false;
        }

        return true;
    }

    private boolean isAllowedTargetStatus(
            String status) {

        return STATUS_PAID.equals(status)
                || STATUS_CANCELLED.equals(status)
                || STATUS_FAILED.equals(status);
    }

    /*
     * Trả về lỗi tương ứng nếu payment
     * đã ở trạng thái kết thúc.
     */
    private String getCurrentStatusError(
            String currentStatus) {

        if (STATUS_PAID.equals(currentStatus)) {
            return "error=already_paid";
        }

        if (STATUS_CANCELLED.equals(currentStatus)) {
            return "error=already_cancelled";
        }

        if (STATUS_FAILED.equals(currentStatus)) {
            return "error=already_failed";
        }

        return null;
    }

    private String getTransitionError(
            String requestedStatus) {

        if (STATUS_PAID.equals(requestedStatus)) {
            return "error=cannot_confirm";
        }

        if (STATUS_CANCELLED.equals(requestedStatus)) {
            return "error=cannot_cancel";
        }

        return "error=cannot_fail";
    }

    private String getSuccessParameter(
            String requestedStatus) {

        if (STATUS_PAID.equals(requestedStatus)) {
            return "success=paid";
        }

        if (STATUS_CANCELLED.equals(requestedStatus)) {
            return "success=cancelled";
        }

        return "success=failed";
    }

    private Integer parsePositiveInteger(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            int parsedValue =
                    Integer.parseInt(value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeUpper(String value) {

        return normalize(value).toUpperCase();
    }

    private String normalizeLower(String value) {

        return normalize(value).toLowerCase();
    }

    /*
     * =========================================
     * ĐIỀU HƯỚNG SAU KHI CẬP NHẬT
     * =========================================
     */
    private void redirectAfterUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            int paymentId,
            String returnPage,
            String keyword,
            String selectedStatus,
            String selectedMethod,
            String result)
            throws IOException {

        /*
         * Quay lại trang thanh toán hôm nay.
         */
        if ("today".equalsIgnoreCase(returnPage)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/payments/today?"
                            + result);

            return;
        }

        /*
         * Quay lại trang chi tiết.
         */
        if ("detail".equalsIgnoreCase(returnPage)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/payments/detail"
                            + "?id="
                            + paymentId
                            + "&"
                            + result);

            return;
        }

        /*
         * Mặc định quay lại danh sách.
         */
        redirectToList(
                request,
                response,
                keyword,
                selectedStatus,
                selectedMethod,
                result);
    }

    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response,
            String keyword,
            String selectedStatus,
            String selectedMethod,
            String result)
            throws IOException {

        StringBuilder url =
                new StringBuilder(
                        request.getContextPath()
                                + "/staff/payments");

        boolean hasParameter = false;

        if (keyword != null && !keyword.isBlank()) {

            url.append("?keyword=")
                    .append(encode(keyword));

            hasParameter = true;
        }

        if (selectedStatus != null
                && !selectedStatus.isBlank()) {

            url.append(hasParameter ? "&" : "?")
                    .append("status=")
                    .append(encode(selectedStatus));

            hasParameter = true;
        }

        if (selectedMethod != null
                && !selectedMethod.isBlank()) {

            url.append(hasParameter ? "&" : "?")
                    .append("method=")
                    .append(encode(selectedMethod));

            hasParameter = true;
        }

        url.append(hasParameter ? "&" : "?")
                .append(result);

        response.sendRedirect(url.toString());
    }

    private String encode(String value) {

        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8);
    }
}