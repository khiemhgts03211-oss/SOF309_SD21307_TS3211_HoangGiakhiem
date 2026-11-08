package controller.admin.payments;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/admin/payments/export")
public class ExportPaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Payment> payments =
                paymentService.getAllPayments();

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=payments.csv");

        PrintWriter writer = response.getWriter();

        writer.println(
                "Payment ID,Transaction Code,Booking Code,Customer,Payment Method,Amount,Status,Paid At");

        for (Payment payment : payments) {

            writer.println(

                    payment.getPaymentId() + ","

                    + payment.getTransactionCode() + ","

                    + payment.getBookingCode() + ","

                    + payment.getCustomerName() + ","

                    + payment.getPaymentMethod() + ","

                    + payment.getAmount() + ","

                    + payment.getStatus() + ","

                    + payment.getPaidAt()

            );

        }

        writer.flush();
        writer.close();

    }

}