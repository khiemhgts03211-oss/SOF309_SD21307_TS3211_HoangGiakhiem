package controller.staff;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Showtime;
import service.PaymentService;
import service.ShowtimeService;
import service.TicketService;
import service.impl.PaymentServiceImpl;
import service.impl.ShowtimeServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    private final PaymentService paymentService =
            new PaymentServiceImpl();

    private final ShowtimeService showtimeService =
            new ShowtimeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Date today =
                Date.valueOf(LocalDate.now());

        request.setAttribute(
                "todayTickets",
                ticketService.getTicketsByDate(today).size());

        request.setAttribute(
                "todayRevenue",
                paymentService.getRevenueByDate(today));

        List<Showtime> showtimes =
                showtimeService.getShowtimesByDate(today);

        request.setAttribute(
                "todayShowtimes",
                showtimes);

        request.setAttribute(
                "showtimeCount",
                showtimes.size());

        request.setAttribute(
                "pendingPayments",
                paymentService.getPaymentsByStatus("PENDING").size());

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/dashboard.jsp")
                .forward(request, response);

    }

}