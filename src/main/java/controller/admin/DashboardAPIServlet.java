package controller.admin;

import java.io.IOException;

import dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/dashboard")
public class DashboardAPIServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DashboardDAO dao = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format(
                "{"
                + "\"users\":%d,"
                + "\"movies\":%d,"
                + "\"rooms\":%d,"
                + "\"showtimes\":%d,"
                + "\"tickets\":%d,"
                + "\"revenue\":%.2f"
                + "}",
                dao.totalUsers(),
                dao.totalMovies(),
                dao.totalRooms(),
                dao.totalShowtimes(),
                dao.totalTickets(),
                dao.totalRevenue());

        response.getWriter().print(json);
    }

}