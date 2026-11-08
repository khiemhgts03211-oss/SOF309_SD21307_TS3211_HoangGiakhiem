package controller.admin.payments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

@WebServlet("/admin/payments")
public class PaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 5;
    private static final int MAX_KEYWORD_LENGTH = 150;

    private PaymentService paymentService;

    @Override
    public void init() throws ServletException {

        try {
            paymentService = new PaymentServiceImpl();
        } catch (Exception exception) {
            throw new ServletException(
                    "Không thể khởi tạo PaymentService.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        String keyword =
                normalize(request.getParameter("keyword"));

        String status =
                normalizeUpper(request.getParameter("status"));

        String method =
                normalizeUpper(request.getParameter("method"));

        String sort =
                normalize(request.getParameter("sort"))
                        .toLowerCase(Locale.ROOT);

        int currentPage =
                parsePositiveInteger(
                        request.getParameter("page"),
                        1);

        String warningMessage = null;

        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            keyword = keyword.substring(0, MAX_KEYWORD_LENGTH);
            warningMessage =
                    "Từ khóa tìm kiếm tối đa 150 ký tự.";
        }

        if (!isValidStatus(status)) {
            status = "";
            warningMessage =
                    "Trạng thái thanh toán không hợp lệ.";
        }

        if (!isValidMethod(method)) {
            method = "";
            warningMessage =
                    "Phương thức thanh toán không hợp lệ.";
        }

        if (!isValidSort(sort)) {
            sort = "";
            warningMessage =
                    "Kiểu sắp xếp không hợp lệ.";
        }

        try {

            /*
             * DAO thực hiện tìm kiếm + lọc trong một câu SQL,
             * tránh điều kiện sau ghi đè điều kiện trước.
             */
            List<Payment> filteredPayments =
                    paymentService.searchAndFilterStaffPayments(
                            keyword,
                            status,
                            method);

            if (filteredPayments == null) {
                filteredPayments = new ArrayList<>();
            } else {
                filteredPayments =
                        new ArrayList<>(filteredPayments);
            }

            /*
             * Sắp xếp trên chính danh sách đã search/filter,
             * không gọi lại DAO.sort() để tránh mất điều kiện.
             */
            sortPayments(filteredPayments, sort);

            int totalRecord = filteredPayments.size();

            int totalPage =
                    totalRecord == 0
                            ? 1
                            : (int) Math.ceil(
                                    (double) totalRecord / PAGE_SIZE);

            if (currentPage > totalPage) {
                currentPage = totalPage;
            }

            int fromIndex =
                    (currentPage - 1) * PAGE_SIZE;

            int toIndex =
                    Math.min(
                            fromIndex + PAGE_SIZE,
                            totalRecord);

            List<Payment> pagePayments;

            if (totalRecord == 0
                    || fromIndex >= totalRecord) {

                pagePayments = Collections.emptyList();

            } else {

                pagePayments =
                        new ArrayList<>(
                                filteredPayments.subList(
                                        fromIndex,
                                        toIndex));
            }

            request.setAttribute("payments", pagePayments);

            request.setAttribute("keyword", keyword);

            request.setAttribute("status", status);
            request.setAttribute("selectedStatus", status);

            request.setAttribute("method", method);
            request.setAttribute("selectedMethod", method);

            request.setAttribute("sort", sort);

            request.setAttribute("currentPage", currentPage);
            request.setAttribute("page", currentPage);

            request.setAttribute("totalPage", totalPage);
            request.setAttribute("totalPages", totalPage);

            request.setAttribute("totalRecord", totalRecord);
            request.setAttribute("totalItems", totalRecord);

            request.setAttribute(
                    "hasCriteria",
                    !keyword.isBlank()
                            || !status.isBlank()
                            || !method.isBlank()
                            || !sort.isBlank());

            if (warningMessage != null) {
                request.setAttribute(
                        "warningMessage",
                        warningMessage);
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách thanh toán. "
                            + "Vui lòng thử lại sau.");

            request.setAttribute(
                    "payments",
                    Collections.emptyList());

            request.setAttribute("keyword", keyword);

            request.setAttribute("status", status);
            request.setAttribute("selectedStatus", status);

            request.setAttribute("method", method);
            request.setAttribute("selectedMethod", method);

            request.setAttribute("sort", sort);

            request.setAttribute("currentPage", 1);
            request.setAttribute("page", 1);

            request.setAttribute("totalPage", 1);
            request.setAttribute("totalPages", 1);

            request.setAttribute("totalRecord", 0);
            request.setAttribute("totalItems", 0);
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/payments/list.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Trang danh sách thanh toán chỉ hỗ trợ GET.");
    }

    private void sortPayments(
            List<Payment> payments,
            String sort) {

        if (payments == null
                || payments.size() < 2) {
            return;
        }

        Comparator<Payment> comparator;

        switch (sort) {

            case "oldest":

                comparator =
                        Comparator.comparing(
                                Payment::getPaidAt,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .thenComparingInt(
                                        Payment::getPaymentId);

                break;

            case "amount_asc":
            case "amountasc":

                comparator =
                        Comparator.comparing(
                                Payment::getAmount,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .thenComparingInt(
                                        Payment::getPaymentId);

                break;

            case "amount_desc":
            case "amountdesc":

                comparator =
                        Comparator.comparing(
                                Payment::getAmount,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .reversed()
                                .thenComparing(
                                        Comparator.comparingInt(
                                                Payment::getPaymentId)
                                                .reversed());

                break;

            case "transaction_asc":

                comparator =
                        Comparator.comparing(
                                (Payment payment) ->
                                        safeString(
                                                payment.getTransactionCode()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "transaction_desc":

                comparator =
                        Comparator.comparing(
                                (Payment payment) ->
                                        safeString(
                                                payment.getTransactionCode()),
                                String.CASE_INSENSITIVE_ORDER)
                                .reversed();

                break;

            case "newest":
            case "":
            default:

                comparator =
                        Comparator.comparing(
                                Payment::getPaidAt,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .reversed()
                                .thenComparing(
                                        Comparator.comparingInt(
                                                Payment::getPaymentId)
                                                .reversed());

                break;
        }

        payments.sort(comparator);
    }

    private boolean isValidStatus(String status) {

        return status == null
                || status.isBlank()
                || "PENDING".equals(status)
                || "PAID".equals(status)
                || "FAILED".equals(status)
                || "CANCELLED".equals(status);
    }

    private boolean isValidMethod(String method) {

        return method == null
                || method.isBlank()
                || "CASH".equals(method)
                || "BANKING".equals(method)
                || "MOMO".equals(method)
                || "VNPAY".equals(method);
    }

    private boolean isValidSort(String sort) {

        return sort == null
                || sort.isBlank()
                || "newest".equals(sort)
                || "oldest".equals(sort)
                || "amount_asc".equals(sort)
                || "amount_desc".equals(sort)
                || "amountasc".equals(sort)
                || "amountdesc".equals(sort)
                || "transaction_asc".equals(sort)
                || "transaction_desc".equals(sort);
    }

    private int parsePositiveInteger(
            String value,
            int defaultValue) {

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {

            int parsedValue =
                    Integer.parseInt(value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : defaultValue;

        } catch (NumberFormatException exception) {

            return defaultValue;
        }
    }

    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate");

        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    private String normalizeUpper(String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String safeString(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}
