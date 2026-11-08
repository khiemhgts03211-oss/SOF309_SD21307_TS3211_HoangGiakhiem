package controller.admin.tickets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets/delete")
public class DeleteTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer id =
                Integer.parseInt(request.getParameter("id"));

        ticketService.deleteTicket(id);

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/tickets");

    }

}