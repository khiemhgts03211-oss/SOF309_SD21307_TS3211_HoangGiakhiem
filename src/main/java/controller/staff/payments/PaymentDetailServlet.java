package controller.staff.payments;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;
import utils.StaffAuthorization;

@WebServlet("/staff/payments/detail")
public class PaymentDetailServlet extends HttpServlet {

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

        Integer paymentId =
                parsePositiveInteger(
                        request.getParameter("id"));

        if (paymentId == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/payments"
                            + "?error=invalid_id");

            return;
        }

        try {

            Payment payment =
                    paymentService
                            .getStaffPaymentDetail(
                                    paymentId);

            if (payment == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/staff/payments"
                                + "?error=not_found");

                return;
            }

            request.setAttribute("payment", payment);

            request.getRequestDispatcher(
                    "/WEB-INF/views/staff/payments/detail.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/staff/payments"
                            + "?error=server_error");
        }
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
}
