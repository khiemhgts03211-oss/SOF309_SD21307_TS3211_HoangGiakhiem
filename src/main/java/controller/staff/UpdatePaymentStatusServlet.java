package controller.staff;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/staff/payment-status")
public class UpdatePaymentStatusServlet
        extends HttpServlet {

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer paymentId =
                Integer.parseInt(
                        request.getParameter("paymentId"));

        String status =
                request.getParameter("status");

        boolean success =
                paymentService.updatePaymentStatus(
                        paymentId,
                        status);

        if (success) {

            request.getSession().setAttribute(
                    "success",
                    "Payment updated successfully.");

        } else {

            request.getSession().setAttribute(
                    "error",
                    "Unable to update payment.");

        }

        response.sendRedirect(
                request.getContextPath()
                + "/staff/today-payments");

    }

}