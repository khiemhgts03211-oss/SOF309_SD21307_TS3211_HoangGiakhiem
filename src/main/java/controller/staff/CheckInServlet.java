package controller.staff;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Ticket;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/checkin")
public class CheckInServlet extends HttpServlet {

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String bookingCode =
                request.getParameter("bookingCode");

        if (bookingCode != null && !bookingCode.isBlank()) {

            Ticket ticket =
                    ticketService.getTicketByBookingCode(bookingCode);

            request.setAttribute("ticket", ticket);
            request.setAttribute("bookingCode", bookingCode);

        }

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/checkin.jsp")
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
                    "Booking code does not exist.");

        } else if ("USED".equals(ticket.getStatus())) {

            request.setAttribute(
                    "error",
                    "Ticket already checked in.");

        } else if ("CANCELLED".equals(ticket.getStatus())) {

            request.setAttribute(
                    "error",
                    "Ticket has been cancelled.");

        } else if (!"PAID".equals(ticket.getStatus())) {

            request.setAttribute(
                    "error",
                    "Ticket must be paid before check-in.");

        } else {

            ticketService.checkInTicket(
                    ticket.getTicketId());

            ticket.setStatus("USED");

            request.setAttribute(
                    "success",
                    "Check-in successful.");

        }

        request.setAttribute("ticket", ticket);
        request.setAttribute("bookingCode", bookingCode);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/checkin.jsp")
                .forward(request, response);

    }

}
