package controller.staff;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Ticket;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/search-ticket")
public class SearchTicketServlet extends HttpServlet {

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/search-ticket.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String bookingCode =
                request.getParameter("bookingCode");

        Ticket ticket =
                ticketService.getTicketByBookingCode(bookingCode);

        if (ticket == null) {

            request.setAttribute(
                    "error",
                    "Booking code not found.");

        } else {

            request.setAttribute(
                    "ticket",
                    ticket);

        }

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/search-ticket.jsp")
                .forward(request, response);

    }

}