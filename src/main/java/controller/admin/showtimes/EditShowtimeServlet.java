package controller.admin.showtimes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Movie;
import model.Room;
import model.Showtime;
import service.MovieService;
import service.RoomService;
import service.ShowtimeService;
import service.impl.MovieServiceImpl;
import service.impl.RoomServiceImpl;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/admin/showtimes/edit")
public class EditShowtimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ShowtimeService showtimeService =
            new ShowtimeServiceImpl();

    private final MovieService movieService =
            new MovieServiceImpl();

    private final RoomService roomService =
            new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Integer id =
                Integer.parseInt(request.getParameter("id"));

        Showtime showtime =
                showtimeService.getShowtimeById(id);

        List<Movie> movies =
                movieService.findAll();

        List<Room> rooms =
                roomService.findAll();

        request.setAttribute("showtime", showtime);
        request.setAttribute("movies", movies);
        request.setAttribute("rooms", rooms);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/showtimes/edit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        Showtime s = new Showtime();

        s.setShowtimeId(
                Integer.parseInt(
                        request.getParameter("showtimeId")));

        s.setMovieId(
                Integer.parseInt(
                        request.getParameter("movieId")));

        s.setRoomId(
                Integer.parseInt(
                        request.getParameter("roomId")));

        s.setShowDate(
                Date.valueOf(
                        request.getParameter("showDate")));

        s.setStartTime(
                Time.valueOf(
                        request.getParameter("startTime") + ":00"));

        s.setEndTime(
                Time.valueOf(
                        request.getParameter("endTime") + ":00"));

        s.setTicketPrice(
                new BigDecimal(
                        request.getParameter("ticketPrice")));

        s.setStatus(
                request.getParameter("status"));

        showtimeService.updateShowtime(s);

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/showtimes");
    }

}