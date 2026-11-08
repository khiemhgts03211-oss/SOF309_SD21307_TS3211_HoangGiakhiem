package controller.admin.tickets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Ticket;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets/detail")
public class DetailTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(request.getParameter("id"));

        Ticket ticket =
                ticketService.getTicketById(id);

        request.setAttribute("ticket", ticket);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/tickets/detail.jsp")
                .forward(request, response);

    }

}