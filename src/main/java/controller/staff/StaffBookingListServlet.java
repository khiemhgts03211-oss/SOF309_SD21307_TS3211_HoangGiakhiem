package controller.staff;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Ticket;
import model.User;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/staff/bookings")
public class StaffBookingListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TicketService ticketService;

    @Override
    public void init() throws ServletException {
        ticketService = new TicketServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        String keyword = trim(
                request.getParameter("keyword"));

        String status = trim(
                request.getParameter("status"));

        List<Ticket> tickets;

        if (keyword != null && !keyword.isBlank()) {

            tickets = ticketService.searchTickets(keyword);

            if (status != null && !status.isBlank()) {
                tickets.removeIf(ticket ->
                        ticket.getStatus() == null
                        || !status.equalsIgnoreCase(
                                ticket.getStatus()));
            }

        } else if (status != null && !status.isBlank()) {

            tickets =
                    ticketService.getTicketsByStatus(status);

        } else {

            tickets = ticketService.getAllTickets();
        }

        request.setAttribute("tickets", tickets);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedStatus", status);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/bookings/list.jsp")
                .forward(request, response);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}