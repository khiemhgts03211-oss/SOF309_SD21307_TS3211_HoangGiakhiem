package controller.admin.showtimes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import model.Room;
import model.Showtime;
import service.MovieService;
import service.RoomService;
import service.ShowtimeService;
import service.impl.MovieServiceImpl;
import service.impl.RoomServiceImpl;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/admin/showtimes/add")
public class AddShowtimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ShowtimeService showtimeService = new ShowtimeServiceImpl();
    private final MovieService movieService = new MovieServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Movie> movies = movieService.findAll();
        List<Room> rooms = roomService.findAll();

        request.setAttribute("movies", movies);
        request.setAttribute("rooms", rooms);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/showtimes/add.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Showtime s = new Showtime();

        s.setMovieId(Integer.parseInt(request.getParameter("movieId")));
        s.setRoomId(Integer.parseInt(request.getParameter("roomId")));

        s.setShowDate(
                Date.valueOf(request.getParameter("showDate")));

        s.setStartTime(
                Time.valueOf(request.getParameter("startTime") + ":00"));

        s.setEndTime(
                Time.valueOf(request.getParameter("endTime") + ":00"));

        s.setTicketPrice(
                new BigDecimal(request.getParameter("ticketPrice")));

        s.setStatus(request.getParameter("status"));

        boolean success = showtimeService.createShowtime(s);

        if (!success) {

            request.setAttribute("error",
                    "Room already has a showtime at this time.");

            doGet(request, response);
            return;
        }

        response.sendRedirect(
                request.getContextPath()
                + "/admin/showtimes");
    }

}