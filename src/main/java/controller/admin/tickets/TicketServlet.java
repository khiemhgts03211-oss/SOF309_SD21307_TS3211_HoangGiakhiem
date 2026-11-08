package controller.admin.tickets;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Ticket;
import service.TicketService;
import service.impl.TicketServiceImpl;

@WebServlet("/admin/tickets")
public class TicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final TicketService ticketService =
            new TicketServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int size = 5;

        String pageParam =
                request.getParameter("page");

        if (pageParam != null) {

            page = Integer.parseInt(pageParam);

        }

        String keyword =
                request.getParameter("keyword");

        String status =
                request.getParameter("status");

        String sort =
                request.getParameter("sort");

        String order =
                request.getParameter("order");

        List<Ticket> tickets;

        if (keyword != null && !keyword.isBlank()) {

            tickets =
                    ticketService.searchTickets(keyword);

        } else if (status != null && !status.isBlank()) {

            tickets =
                    ticketService.filter(status);

        } else if (sort != null && !sort.isBlank()) {

            tickets =
                    ticketService.sort(sort, order);

        } else {

            tickets =
                    ticketService.getTicketsByPage(page, size);

        }

        int totalRecord =
                ticketService.countTickets();

        int totalPage =
                (int) Math.ceil(
                        (double) totalRecord / size);

        request.setAttribute("tickets", tickets);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("order", order);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/tickets/list.jsp")
                .forward(request, response);
    }
}