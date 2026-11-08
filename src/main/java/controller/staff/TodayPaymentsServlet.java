package controller.staff;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/staff/today-payments")
public class TodayPaymentsServlet extends HttpServlet {

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Payment> payments =
                paymentService.getTodayPayments();

        request.setAttribute(
                "payments",
                payments);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/today-payments.jsp")
                .forward(request, response);

    }

}