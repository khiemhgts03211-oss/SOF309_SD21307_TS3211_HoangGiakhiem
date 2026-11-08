package controller.staff;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Showtime;
import service.ShowtimeService;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/staff/showtimes")
public class TodayShowtimesServlet extends HttpServlet {

    private final ShowtimeService showtimeService =
            new ShowtimeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Showtime> showtimes =
                showtimeService.getTodayShowtimes();

        request.setAttribute(
                "showtimes",
                showtimes);

        request.getRequestDispatcher(
                "/WEB-INF/views/staff/showtimes.jsp")
                .forward(request, response);

    }

}