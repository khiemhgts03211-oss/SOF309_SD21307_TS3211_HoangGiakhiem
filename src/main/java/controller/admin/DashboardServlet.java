package controller.admin;

import java.io.IOException;

import dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import dao.DashboardDAO;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    DashboardDAO dao = new DashboardDAO();
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("users",
                dao.totalUsers());

        request.setAttribute("movies",
                dao.totalMovies());

        request.setAttribute("rooms",
                dao.totalRooms());

        request.setAttribute("showtimes",
                dao.totalShowtimes());

        request.setAttribute("tickets",
                dao.totalTickets());

        request.setAttribute("revenue",
                dao.totalRevenue());

        Map<Integer, Double> revenueByMonth =
                dao.revenueByMonth();

        request.setAttribute(
                "revenueByMonth",
                revenueByMonth);
        
        request.setAttribute(
                "ticketByMonth",
                dao.ticketByMonth());
        
        request.setAttribute(
                "movieChart",
                dao.movieTicketChart());
        
        request.setAttribute(
                "recentBookings",
                dao.recentBookings());
        
        request.getRequestDispatcher(
                "/WEB-INF/views/admin/dashboard.jsp")
                .forward(request, response);
    }

    
}