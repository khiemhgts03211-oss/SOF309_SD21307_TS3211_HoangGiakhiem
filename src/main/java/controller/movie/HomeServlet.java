package controller.movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MAX_KEYWORD_LENGTH = 150;
    private static final int MAX_GENRE_LENGTH = 100;

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

        String keyword =
                normalize(
                        request.getParameter("keyword"));

        String selectedGenre =
                normalize(
                        request.getParameter("genre"));

        String warningMessage = null;

        /*
         * Loại bỏ ký tự điều khiển.
         */
        keyword =
                removeControlCharacters(keyword);

        selectedGenre =
                removeControlCharacters(
                        selectedGenre);

        /*
         * Kiểm tra độ dài từ khóa.
         */
        if (keyword.length() > MAX_KEYWORD_LENGTH) {

            keyword =
                    keyword.substring(
                            0,
                            MAX_KEYWORD_LENGTH);

            warningMessage =
                    "Từ khóa tìm kiếm tối đa 150 ký tự.";
        }

        /*
         * Kiểm tra độ dài thể loại.
         */
        if (selectedGenre.length() > MAX_GENRE_LENGTH) {

            selectedGenre = "";

            warningMessage =
                    "Thể loại được chọn không hợp lệ.";
        }

        try {

            /*
             * =========================================
             * LẤY DANH SÁCH THỂ LOẠI
             * =========================================
             */

            List<String> genres =
                    movieService.getAllGenres();

            genres = sanitizeGenres(genres);

            /*
             * Kiểm tra thể loại gửi lên có tồn tại.
             */
            if (!selectedGenre.isBlank()) {

                String validGenre =
                        findMatchingGenre(
                                genres,
                                selectedGenre);

                if (validGenre == null) {

                    selectedGenre = "";

                    warningMessage =
                            "Thể loại được chọn không tồn tại.";

                } else {

                    selectedGenre = validGenre;
                }
            }

            boolean filtering =
                    !keyword.isBlank()
                            || !selectedGenre.isBlank();

            List<Movie> nowShowing;
            List<Movie> comingSoon;

            /*
             * =========================================
             * TÌM KIẾM / LỌC
             * =========================================
             */

            if (filtering) {

                /*
                 * searchMovies() chỉ lấy phim ACTIVE.
                 *
                 * Khi tìm kiếm hoặc lọc, kết quả được
                 * hiển thị trong khu vực kết quả tìm kiếm.
                 */
                nowShowing =
                        movieService.searchMovies(
                                keyword,
                                selectedGenre);

                comingSoon =
                        Collections.emptyList();

            } else {

                /*
                 * Phim đang chiếu:
                 * ACTIVE và release_date <= hôm nay.
                 */
                nowShowing =
                        movieService.findNowShowing();

                /*
                 * Phim sắp chiếu:
                 * ACTIVE và release_date > hôm nay.
                 */
                comingSoon =
                        movieService.findComingSoon();
            }

            /*
             * Kiểm tra danh sách và dữ liệu từng phim.
             */
            nowShowing =
                    sanitizeCustomerMovies(
                            nowShowing);

            comingSoon =
                    sanitizeCustomerMovies(
                            comingSoon);

            /*
             * =========================================
             * GỬI DỮ LIỆU SANG JSP
             * =========================================
             */

            request.setAttribute(
                    "nowShowing",
                    nowShowing);

            request.setAttribute(
                    "comingSoon",
                    comingSoon);

            request.setAttribute(
                    "genres",
                    genres);

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "selectedGenre",
                    selectedGenre);

            request.setAttribute(
                    "filtering",
                    filtering);

            /*
             * Giữ để tương thích với JSP cũ.
             */
            request.setAttribute(
                    "searching",
                    filtering);

            request.setAttribute(
                    "nowShowingCount",
                    nowShowing.size());

            request.setAttribute(
                    "comingSoonCount",
                    comingSoon.size());

            request.setAttribute(
                    "pageTitle",
                    filtering
                            ? "Tìm kiếm phim"
                            : "Trang chủ");

            if (warningMessage != null) {

                request.setAttribute(
                        "warningMessage",
                        warningMessage);
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            setErrorAttributes(
                    request,
                    keyword,
                    selectedGenre,
                    warningMessage);
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/home.jsp")
                .forward(request, response);
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

    /*
     * Loại bỏ phim có dữ liệu không hợp lệ.
     */
    private List<Movie> sanitizeCustomerMovies(
            List<Movie> movies) {

        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }

        List<Movie> validMovies =
                new ArrayList<>();

        for (Movie movie : movies) {

            if (!isValidCustomerMovie(movie)) {
                continue;
            }

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

            validMovies.add(movie);
        }

        return validMovies;
    }

    private boolean isValidCustomerMovie(
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

        /*
         * Khách hàng chỉ được xem phim ACTIVE.
         */
        if (movie.getStatus() == null
                || !"ACTIVE".equalsIgnoreCase(
                        movie.getStatus().trim())) {

            return false;
        }

        /*
         * Chặn thời lượng bất thường.
         */
        if (movie.getDuration() < 0
                || movie.getDuration() > 1000) {

            return false;
        }

        return true;
    }

    private List<String> sanitizeGenres(
            List<String> genres) {

        if (genres == null || genres.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> validGenres =
                new ArrayList<>();

        for (String genre : genres) {

            String normalizedGenre =
                    normalize(genre);

            if (normalizedGenre.isBlank()) {
                continue;
            }

            if (normalizedGenre.length()
                    > MAX_GENRE_LENGTH) {

                continue;
            }

            boolean duplicated = false;

            for (String existingGenre : validGenres) {

                if (existingGenre.equalsIgnoreCase(
                        normalizedGenre)) {

                    duplicated = true;
                    break;
                }
            }

            if (!duplicated) {
                validGenres.add(normalizedGenre);
            }
        }

        validGenres.sort(
                String.CASE_INSENSITIVE_ORDER);

        return validGenres;
    }

    private String findMatchingGenre(
            List<String> genres,
            String selectedGenre) {

        if (genres == null
                || genres.isEmpty()
                || selectedGenre == null
                || selectedGenre.isBlank()) {

            return null;
        }

        for (String genre : genres) {

            if (genre != null
                    && genre.equalsIgnoreCase(
                            selectedGenre.trim())) {

                return genre;
            }
        }

        return null;
    }

    private void setErrorAttributes(
            HttpServletRequest request,
            String keyword,
            String selectedGenre,
            String warningMessage) {

        boolean filtering =
                !normalize(keyword).isBlank()
                        || !normalize(
                                selectedGenre).isBlank();

        request.setAttribute(
                "errorMessage",
                "Không thể tải danh sách phim. "
                        + "Vui lòng thử lại sau.");

        if (warningMessage != null) {

            request.setAttribute(
                    "warningMessage",
                    warningMessage);
        }

        request.setAttribute(
                "nowShowing",
                Collections.emptyList());

        request.setAttribute(
                "comingSoon",
                Collections.emptyList());

        request.setAttribute(
                "genres",
                Collections.emptyList());

        request.setAttribute(
                "keyword",
                normalize(keyword));

        request.setAttribute(
                "selectedGenre",
                normalize(selectedGenre));

        request.setAttribute(
                "filtering",
                filtering);

        request.setAttribute(
                "searching",
                filtering);

        request.setAttribute(
                "nowShowingCount",
                0);

        request.setAttribute(
                "comingSoonCount",
                0);

        request.setAttribute(
                "pageTitle",
                "Trang chủ");
    }

    private String removeControlCharacters(
            String value) {

        if (value == null || value.isBlank()) {
            return "";
        }

        return value
                .replaceAll(
                        "[\\p{Cntrl}&&[^\r\n\t]]",
                        "")
                .replace("\r", " ")
                .replace("\n", " ")
                .replace("\t", " ")
                .trim();
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}