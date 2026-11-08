package controller.movie;

import java.io.IOException;

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

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ShowtimeService showtimeService = new ShowtimeServiceImpl();
    private final MovieService movieService = new MovieServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String showtimeIdParam = request.getParameter("showtimeId");

        if (showtimeIdParam == null || showtimeIdParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        int showtimeId;

        try {
            showtimeId = Integer.parseInt(showtimeIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        Showtime showtime = showtimeService.getShowtimeById(showtimeId);

        if (showtime == null) {
            response.sendRedirect(request.getContextPath() + "/movies");
            return;
        }

        Movie movie = movieService.findById(showtime.getMovieId());
        Room room = roomService.findById(showtime.getRoomId());

        request.setAttribute("showtime", showtime);
        request.setAttribute("movie", movie);
        request.setAttribute("room", room);

        request.getRequestDispatcher("/WEB-INF/views/customer/booking.jsp")
               .forward(request, response);
    }

}