package controller.admin.tickets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets/update-status")
public class UpdateTicketStatusServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer id =
                Integer.parseInt(request.getParameter("id"));

        String status =
                request.getParameter("status");

        ticketService.updateStatus(id, status);

        response.sendRedirect(
                request.getContextPath()
                + "/admin/tickets");

    }

}