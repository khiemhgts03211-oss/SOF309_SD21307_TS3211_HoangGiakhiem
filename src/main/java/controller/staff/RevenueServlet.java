package controller.staff;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.PaymentService;
import service.TicketDetailService;
import service.TicketService;
import service.impl.PaymentServiceImpl;
import service.impl.TicketDetailServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/revenue")
public class RevenueServlet extends HttpServlet {

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    private final TicketService ticketService =
            new TicketServiceImpl();

    private final TicketDetailService ticketDetailService =
            new TicketDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        LocalDate now = LocalDate.now();

        int month = now.getMonthValue();
        int year = now.getYear();

        // Nếu người dùng chọn tháng/năm khác
        String monthParam = request.getParameter("month");
        String yearParam = request.getParameter("year");

        if (monthParam != null && !monthParam.isBlank()) {
            month = Integer.parseInt(monthParam);
        }

        if (yearParam != null && !yearParam.isBlank()) {
            year = Integer.parseInt(yearParam);
        }

        // ===== Revenue =====
        double todayRevenue =
                paymentService.getTodayRevenue();

        double monthRevenue =
                paymentService.getRevenueByMonth(month, year);

        double yearRevenue =
                paymentService.getRevenueByYear(year);

        // ===== Ticket Statistics =====
        int soldTickets =
                ticketService.countPaidTickets();

        int checkedInTickets =
                ticketService.countCheckedInTickets();

        // ===== Service Statistics =====
        int serviceQuantity =
                ticketDetailService.getTodayServiceQuantity();

        double serviceRevenue =
                ticketDetailService.getTodayServiceRevenue();

        // ===== Paid Payments =====
        request.setAttribute(
                "payments",
                paymentService.getPaidPayments());

        // ===== Dashboard =====
        request.setAttribute(
                "todayRevenue",
                todayRevenue);

        request.setAttribute(
                "monthRevenue",
                monthRevenue);

        request.setAttribute(
                "yearRevenue",
                yearRevenue);

        request.setAttribute(
                "soldTickets",
                soldTickets);

        request.setAttribute(
                "checkedInTickets",
                checkedInTickets);

        request.setAttribute(
                "serviceQuantity",
                serviceQuantity);

        request.setAttribute(
                "serviceRevenue",
                serviceRevenue);

        request.setAttribute(
                "month",
                month);

        request.setAttribute(
                "year",
                year);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/revenue.jsp")
                .forward(request, response);
    }
}