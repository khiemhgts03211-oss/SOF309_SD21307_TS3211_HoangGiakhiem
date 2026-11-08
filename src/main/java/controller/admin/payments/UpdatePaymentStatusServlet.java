package controller.admin.payments;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/admin/payments/update-status")
public class UpdatePaymentStatusServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        /*
         * Hỗ trợ cả paymentId và id
         */
        String idParam = request.getParameter("paymentId");

        if (idParam == null || idParam.trim().isEmpty()) {
            idParam = request.getParameter("id");
        }

        /*
         * Không có ID thì quay lại danh sách
         */
        if (idParam == null || idParam.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/payments");

            return;
        }

        try {

            int paymentId =
                    Integer.parseInt(idParam.trim());

            String status =
                    request.getParameter("status");

            if (status != null) {
                status = status.trim().toUpperCase();
            }

            boolean updated =
                    paymentService.updatePaymentStatus(
                            paymentId,
                            status);

            if (updated) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/payments?success=updated");

            } else {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/payments?error=updateFailed");
            }

        } catch (NumberFormatException exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/payments?error=invalidId");

        } catch (Exception exception) {

            exception.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/payments?error=server");
        }
    }

}