package controller.staff;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Ticket;
import model.TicketDetail;
import service.TicketDetailService;
import service.TicketService;
import service.impl.TicketDetailServiceImpl;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/print-ticket")
public class PrintTicketServlet extends HttpServlet {

    private final TicketService ticketService =
            new TicketServiceImpl();

    private final TicketDetailService ticketDetailService =
            new TicketDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String bookingCode =
                request.getParameter("bookingCode");

        if (bookingCode != null && !bookingCode.isBlank()) {

            Ticket ticket =
                    ticketService.getTicketByBookingCode(bookingCode);

            if (ticket == null) {

                request.setAttribute(
                        "error",
                        "Ticket not found.");

            } else {

                request.setAttribute(
                        "ticket",
                        ticket);

                List<TicketDetail> details =
                        ticketDetailService.getTicketDetailsByTicket(
                                ticket.getTicketId());

                request.setAttribute(
                        "details",
                        details);

            }

        }

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/print-ticket.jsp")
                .forward(request, response);

    }

}