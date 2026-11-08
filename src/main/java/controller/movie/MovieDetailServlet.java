package controller.movie;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet("/movies/detail")
public class MovieDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String STATUS_ACTIVE = "ACTIVE";

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_GENRE_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 5000;
    private static final int MAX_POSTER_LENGTH = 500;
    private static final int MAX_DURATION = 1000;

    private MovieService movieService;

    @Override
    public void init() throws ServletException {

        try {

            movieService = new MovieServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo MovieService.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        Integer movieId =
                parsePositiveInteger(
                        request.getParameter("id"));

        if (movieId == null) {

            redirectToHome(
                    request,
                    response,
                    "invalid_movie_id");

            return;
        }

        try {

            Movie movie =
                    movieService.findById(movieId);

            if (movie == null) {

                redirectToHome(
                        request,
                        response,
                        "movie_not_found");

                return;
            }

            if (!isActiveMovie(movie)) {

                redirectToHome(
                        request,
                        response,
                        "movie_unavailable");

                return;
            }

            if (!hasValidRequiredData(movie)) {

                redirectToHome(
                        request,
                        response,
                        "invalid_movie_data");

                return;
            }

            sanitizeMovie(movie);

            request.setAttribute(
                    "movie",
                    movie);

            request.setAttribute(
                    "pageTitle",
                    movie.getTitle());

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/movie-detail.jsp")
                    .forward(request, response);

        } catch (Exception exception) {

            exception.printStackTrace();

            redirectToHome(
                    request,
                    response,
                    "server_error");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Trang chi tiết phim chỉ hỗ trợ GET.");
    }

    private boolean isActiveMovie(Movie movie) {

        if (movie == null
                || movie.getStatus() == null) {

            return false;
        }

        String status =
                movie.getStatus()
                        .trim()
                        .toUpperCase(Locale.ROOT);

        return STATUS_ACTIVE.equals(status);
    }

    private boolean hasValidRequiredData(Movie movie) {

        if (movie == null) {
            return false;
        }

        if (movie.getMovieId() <= 0) {
            return false;
        }

        String title =
                normalize(movie.getTitle());

        if (title.isBlank()
                || title.length() > MAX_TITLE_LENGTH) {

            return false;
        }

        if (movie.getDuration() < 0
                || movie.getDuration() > MAX_DURATION) {

            return false;
        }

        String genre =
                normalize(movie.getGenre());

        if (genre.length() > MAX_GENRE_LENGTH) {
            return false;
        }

        String description =
                normalize(movie.getDescription());

        if (description.length()
                > MAX_DESCRIPTION_LENGTH) {

            return false;
        }

        String poster =
                normalize(movie.getPoster());

        if (poster.length() > MAX_POSTER_LENGTH) {
            return false;
        }

        return true;
    }

    private void sanitizeMovie(Movie movie) {

        movie.setTitle(
                normalize(movie.getTitle()));

        movie.setGenre(
                normalize(movie.getGenre()));

        movie.setDescription(
                normalize(movie.getDescription()));

        movie.setPoster(
                normalize(movie.getPoster()));

        movie.setStatus(
                normalize(movie.getStatus())
                        .toUpperCase(Locale.ROOT));
    }

    private Integer parsePositiveInteger(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            int parsedValue =
                    Integer.parseInt(
                            value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : null;

        } catch (NumberFormatException exception) {

            return null;
        }
    }

    private void redirectToHome(
            HttpServletRequest request,
            HttpServletResponse response,
            String errorCode)
            throws IOException {

        String safeErrorCode =
                URLEncoder.encode(
                        errorCode,
                        StandardCharsets.UTF_8);

        response.sendRedirect(
                request.getContextPath()
                        + "/home?error="
                        + safeErrorCode);
    }

    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate");

        response.setHeader(
                "Pragma",
                "no-cache");

        response.setDateHeader(
                "Expires",
                0);
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}