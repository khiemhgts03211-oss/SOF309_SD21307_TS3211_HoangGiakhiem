package controller.movie;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import model.Showtime;
import service.MovieService;
import service.ShowtimeService;
import service.impl.MovieServiceImpl;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/showtimes")
public class ShowtimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private MovieService movieService;
    private ShowtimeService showtimeService;

    @Override
    public void init() throws ServletException {

        try {

            movieService = new MovieServiceImpl();
            showtimeService = new ShowtimeServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo service cho trang lịch chiếu.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        /*
         * Trang lịch chiếu là trang công khai.
         * Không kiểm tra đăng nhập tại Servlet này.
         */

        Integer movieId =
                parsePositiveInteger(
                        request.getParameter("movieId"));

        if (movieId == null) {

            redirectToHome(
                    request,
                    response,
                    "invalid_movie_id");

            return;
        }

        try {

            /*
             * ==========================================
             * KIỂM TRA PHIM
             * ==========================================
             */

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

            if (!hasValidMovieData(movie)) {

                redirectToHome(
                        request,
                        response,
                        "invalid_movie_data");

                return;
            }

            sanitizeMovie(movie);

            /*
             * ==========================================
             * LẤY SUẤT CHIẾU HỢP LỆ
             * ==========================================
             */

            List<Showtime> showtimes =
                    showtimeService
                            .getAvailableShowtimesByMovie(
                                    movieId);

            /*
             * Kiểm tra lại tại Servlet để tránh:
             * - DAO trả dữ liệu sai phim.
             * - Suất chiếu đã qua.
             * - Trạng thái không hợp lệ.
             * - Thiếu phòng, ngày, giờ hoặc giá vé.
             */
            showtimes =
                    sanitizeAvailableShowtimes(
                            showtimes,
                            movieId);

            /*
             * Sắp xếp:
             * ngày tăng dần → giờ bắt đầu tăng dần.
             */
            sortShowtimes(showtimes);

            /*
             * ==========================================
             * GỬI DỮ LIỆU SANG JSP
             * ==========================================
             */

            request.setAttribute(
                    "movie",
                    movie);

            request.setAttribute(
                    "showtimes",
                    showtimes);

            request.setAttribute(
                    "showtimeCount",
                    showtimes.size());

            request.setAttribute(
                    "pageTitle",
                    "Lịch chiếu - "
                            + movie.getTitle());

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/showtimes.jsp")
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
                "Trang lịch chiếu chỉ hỗ trợ phương thức GET.");
    }

    /*
     * ==================================================
     * KIỂM TRA VÀ LỌC DANH SÁCH SUẤT CHIẾU
     * ==================================================
     */

    private List<Showtime> sanitizeAvailableShowtimes(
            List<Showtime> showtimes,
            Integer movieId) {

        if (showtimes == null
                || showtimes.isEmpty()
                || movieId == null
                || movieId <= 0) {

            return Collections.emptyList();
        }

        List<Showtime> validShowtimes =
                new ArrayList<>();

        for (Showtime showtime : showtimes) {

            if (!isValidShowtime(
                    showtime,
                    movieId)) {

                continue;
            }

            sanitizeShowtime(showtime);

            validShowtimes.add(showtime);
        }

        return validShowtimes;
    }

    private boolean isValidShowtime(
            Showtime showtime,
            Integer requestedMovieId) {

        if (showtime == null) {
            return false;
        }

        if (showtime.getShowtimeId() <= 0) {
            return false;
        }

        /*
         * getMovieId() trong model hiện thường là int.
         * So sánh bằng giá trị số.
         */
        if (showtime.getMovieId()
                != requestedMovieId.intValue()) {

            return false;
        }

        if (showtime.getRoomId() <= 0) {
            return false;
        }

        if (showtime.getShowDate() == null
                || showtime.getStartTime() == null) {

            return false;
        }

        BigDecimal ticketPrice =
                showtime.getTicketPrice();

        if (ticketPrice == null
                || ticketPrice.compareTo(
                        BigDecimal.ZERO) <= 0) {

            return false;
        }

        if (!isAvailableStatus(
                showtime.getStatus())) {

            return false;
        }

        /*
         * Không hiển thị suất chiếu đã qua.
         */
        return !isPastShowtime(showtime);
    }

    private boolean isAvailableStatus(
            String status) {

        String normalizedStatus =
                normalize(status)
                        .toUpperCase(Locale.ROOT);

        return "OPEN".equals(normalizedStatus)
                || "ACTIVE".equals(normalizedStatus);
    }

    /*
     * ==================================================
     * KIỂM TRA SUẤT CHIẾU ĐÃ QUA
     * ==================================================
     */

    private boolean isPastShowtime(
            Showtime showtime) {

        Date showDate =
                showtime.getShowDate();

        Time startTime =
                showtime.getStartTime();

        if (showDate == null
                || startTime == null) {

            return true;
        }

        LocalDate today =
                LocalDate.now();

        LocalTime now =
                LocalTime.now();

        LocalDate showLocalDate =
                showDate.toLocalDate();

        LocalTime showLocalTime =
                startTime.toLocalTime();

        if (showLocalDate.isBefore(today)) {
            return true;
        }

        if (showLocalDate.isAfter(today)) {
            return false;
        }

        /*
         * Nếu chiếu trong hôm nay thì giờ bắt đầu
         * phải lớn hơn thời điểm hiện tại.
         */
        return !showLocalTime.isAfter(now);
    }

    /*
     * ==================================================
     * SẮP XẾP
     * ==================================================
     */

    private void sortShowtimes(
            List<Showtime> showtimes) {

        if (showtimes == null
                || showtimes.size() < 2) {

            return;
        }

        Comparator<Showtime> comparator =
                Comparator.comparing(
                        Showtime::getShowDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()))
                        .thenComparing(
                                Showtime::getStartTime,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                        .thenComparingInt(
                                Showtime::getShowtimeId);

        showtimes.sort(comparator);
    }

    /*
     * ==================================================
     * KIỂM TRA PHIM
     * ==================================================
     */

    private boolean isActiveMovie(
            Movie movie) {

        if (movie == null
                || movie.getStatus() == null) {

            return false;
        }

        return "ACTIVE".equalsIgnoreCase(
                movie.getStatus().trim());
    }

    private boolean hasValidMovieData(
            Movie movie) {

        if (movie == null) {
            return false;
        }

        if (movie.getMovieId() <= 0) {
            return false;
        }

        if (movie.getTitle() == null
                || movie.getTitle().isBlank()) {

            return false;
        }

        return movie.getDuration() >= 0
                && movie.getDuration() <= 1000;
    }

    /*
     * ==================================================
     * CHUẨN HÓA DỮ LIỆU
     * ==================================================
     */

    private void sanitizeMovie(
            Movie movie) {

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

    private void sanitizeShowtime(
            Showtime showtime) {

        showtime.setStatus(
                normalize(showtime.getStatus())
                        .toUpperCase(Locale.ROOT));

        showtime.setMovieTitle(
                normalize(
                        showtime.getMovieTitle()));

        showtime.setRoomName(
                normalize(
                        showtime.getRoomName()));
    }

    /*
     * ==================================================
     * PARSE VÀ ĐIỀU HƯỚNG
     * ==================================================
     */

    private Integer parsePositiveInteger(
            String value) {

        if (value == null
                || value.isBlank()) {

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

        response.sendRedirect(
                request.getContextPath()
                        + "/home?error="
                        + errorCode);
    }

    /*
     * ==================================================
     * RESPONSE
     * ==================================================
     */

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

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }
}