package controller.admin.payments;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;

@WebServlet("/admin/payments/detail")
public class DetailPaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer id =
                Integer.parseInt(request.getParameter("id"));

        Payment payment =
                paymentService.getPaymentById(id);

        request.setAttribute("payment", payment);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/payments/detail.jsp")
                .forward(request, response);
    }
}