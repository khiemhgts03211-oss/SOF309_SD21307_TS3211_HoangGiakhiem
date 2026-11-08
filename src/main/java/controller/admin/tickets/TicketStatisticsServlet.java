package controller.admin.tickets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets/statistics")
public class TicketStatisticsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int totalTickets =
                ticketService.countTickets();

        int bookedTickets =
                ticketService.countBookedTickets();

        int paidTickets =
                ticketService.countPaidTickets();

        double revenue =
                ticketService.getTotalRevenue();

        request.setAttribute(
                "totalTickets",
                totalTickets);

        request.setAttribute(
                "bookedTickets",
                bookedTickets);

        request.setAttribute(
                "paidTickets",
                paidTickets);

        request.setAttribute(
                "revenue",
                revenue);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/tickets/statistics.jsp")
                .forward(request, response);
    }
}