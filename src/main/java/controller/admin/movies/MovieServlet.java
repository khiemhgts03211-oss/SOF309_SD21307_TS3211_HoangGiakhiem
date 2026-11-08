package controller.admin.movies;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Movie;
import service.MovieService;
import service.impl.MovieServiceImpl;

@WebServlet("/admin/movies")
public class MovieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 5;
    private static final int MAX_KEYWORD_LENGTH = 150;
    private static final int MAX_FILTER_LENGTH = 100;

    private static final Pattern DIACRITICS =
            Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

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

        /*
         * ==================================================
         * NHẬN THAM SỐ
         * ==================================================
         */

        String keyword =
                normalize(request.getParameter("keyword"));

        String genre =
                normalize(request.getParameter("genre"));

        String status =
                normalizeUpper(request.getParameter("status"));

        String sort =
                normalize(request.getParameter("sort"));

        String order =
                normalizeUpper(request.getParameter("order"));

        int currentPage =
                parsePositiveInteger(
                        request.getParameter("page"),
                        1);

        String warningMessage = null;

        /*
         * ==================================================
         * KIỂM TRA DỮ LIỆU ĐẦU VÀO
         * ==================================================
         */

        keyword = removeControlCharacters(keyword);
        genre = removeControlCharacters(genre);
        status = removeControlCharacters(status);
        sort = removeControlCharacters(sort);
        order = removeControlCharacters(order);

        if (keyword.length() > MAX_KEYWORD_LENGTH) {

            keyword =
                    keyword.substring(
                            0,
                            MAX_KEYWORD_LENGTH);

            warningMessage =
                    "Từ khóa tìm kiếm tối đa 150 ký tự.";
        }

        if (genre.length() > MAX_FILTER_LENGTH) {

            genre = "";

            warningMessage =
                    "Thể loại được chọn không hợp lệ.";
        }

        if (!isValidStatus(status)) {

            status = "";

            warningMessage =
                    "Trạng thái được chọn không hợp lệ.";
        }

        sort = normalizeSortField(sort);
        order = normalizeOrder(order);

        try {

            /*
             * ==================================================
             * LẤY DỮ LIỆU GỐC
             * ==================================================
             */

            List<Movie> allMovies =
                    movieService.findAll();

            if (allMovies == null) {
                allMovies = Collections.emptyList();
            }

            List<String> genres =
                    buildGenreList(allMovies);

            /*
             * Không chấp nhận thể loại giả mạo trên URL.
             */
            if (!genre.isBlank()) {

                String validGenre =
                        findMatchingGenre(
                                genres,
                                genre);

                if (validGenre == null) {

                    genre = "";

                    warningMessage =
                            "Thể loại được chọn không tồn tại.";

                } else {

                    genre = validGenre;
                }
            }

            /*
             * ==================================================
             * SEARCH + FILTER
             * ==================================================
             */

            List<Movie> filteredMovies =
                    new ArrayList<>();

            for (Movie movie : allMovies) {

                if (movie == null
                        || movie.getMovieId() <= 0) {

                    continue;
                }

                if (!matchesKeyword(
                        movie,
                        keyword)) {

                    continue;
                }

                if (!matchesGenre(
                        movie,
                        genre)) {

                    continue;
                }

                if (!matchesStatus(
                        movie,
                        status)) {

                    continue;
                }

                filteredMovies.add(movie);
            }

            /*
             * ==================================================
             * SORT
             * ==================================================
             */

            sortMovies(
                    filteredMovies,
                    sort,
                    order);

            /*
             * ==================================================
             * PAGINATION
             * ==================================================
             */

            int totalMovie =
                    filteredMovies.size();

            int totalPage =
                    totalMovie == 0
                            ? 1
                            : (int) Math.ceil(
                                    (double) totalMovie
                                            / PAGE_SIZE);

            if (currentPage > totalPage) {
                currentPage = totalPage;
            }

            int fromIndex =
                    (currentPage - 1) * PAGE_SIZE;

            int toIndex =
                    Math.min(
                            fromIndex + PAGE_SIZE,
                            totalMovie);

            List<Movie> movies;

            if (totalMovie == 0
                    || fromIndex >= totalMovie) {

                movies =
                        Collections.emptyList();

            } else {

                movies =
                        new ArrayList<>(
                                filteredMovies.subList(
                                        fromIndex,
                                        toIndex));
            }

            /*
             * ==================================================
             * GỬI DỮ LIỆU SANG JSP
             * ==================================================
             */

            request.setAttribute(
                    "movies",
                    movies);

            request.setAttribute(
                    "genres",
                    genres);

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "genre",
                    genre);

            /*
             * Một số JSP dùng selectedGenre.
             */
            request.setAttribute(
                    "selectedGenre",
                    genre);

            request.setAttribute(
                    "status",
                    status);

            /*
             * Một số JSP dùng selectedStatus.
             */
            request.setAttribute(
                    "selectedStatus",
                    status);

            request.setAttribute(
                    "sort",
                    sort);

            request.setAttribute(
                    "order",
                    order);

            request.setAttribute(
                    "currentPage",
                    currentPage);

            request.setAttribute(
                    "totalPage",
                    totalPage);

            request.setAttribute(
                    "totalMovie",
                    totalMovie);

            request.setAttribute(
                    "pageSize",
                    PAGE_SIZE);

            request.setAttribute(
                    "hasSearch",
                    !keyword.isBlank());

            request.setAttribute(
                    "hasFilter",
                    !genre.isBlank()
                            || !status.isBlank());

            request.setAttribute(
                    "hasSort",
                    !sort.isBlank());

            if (warningMessage != null) {

                request.setAttribute(
                        "warningMessage",
                        warningMessage);
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách phim. "
                            + "Vui lòng thử lại sau.");

            request.setAttribute(
                    "movies",
                    Collections.emptyList());

            request.setAttribute(
                    "genres",
                    Collections.emptyList());

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "genre",
                    genre);

            request.setAttribute(
                    "selectedGenre",
                    genre);

            request.setAttribute(
                    "status",
                    status);

            request.setAttribute(
                    "selectedStatus",
                    status);

            request.setAttribute(
                    "sort",
                    sort);

            request.setAttribute(
                    "order",
                    order);

            request.setAttribute(
                    "currentPage",
                    1);

            request.setAttribute(
                    "totalPage",
                    1);

            request.setAttribute(
                    "totalMovie",
                    0);
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/movies/list.jsp")
                .forward(request, response);
    }

    /*
     * ==================================================
     * SEARCH
     * ==================================================
     */

    private boolean matchesKeyword(
            Movie movie,
            String keyword) {

        if (keyword == null
                || keyword.isBlank()) {

            return true;
        }

        String normalizedKeyword =
                normalizeForSearch(keyword);

        String title =
                normalizeForSearch(
                        movie.getTitle());

        String movieGenre =
                normalizeForSearch(
                        movie.getGenre());

        return title.contains(normalizedKeyword)
                || movieGenre.contains(
                        normalizedKeyword);
    }

    /*
     * ==================================================
     * FILTER
     * ==================================================
     */

    private boolean matchesGenre(
            Movie movie,
            String genre) {

        if (genre == null
                || genre.isBlank()) {

            return true;
        }

        return movie.getGenre() != null
                && movie.getGenre()
                        .trim()
                        .equalsIgnoreCase(
                                genre.trim());
    }

    private boolean matchesStatus(
            Movie movie,
            String status) {

        if (status == null
                || status.isBlank()) {

            return true;
        }

        return movie.getStatus() != null
                && movie.getStatus()
                        .trim()
                        .equalsIgnoreCase(
                                status.trim());
    }

    /*
     * ==================================================
     * SORT
     * ==================================================
     */

    private void sortMovies(
            List<Movie> movies,
            String sort,
            String order) {

        if (movies == null
                || movies.size() < 2
                || sort == null
                || sort.isBlank()) {

            return;
        }

        Comparator<Movie> comparator;

        switch (sort) {

            case "title":

                comparator =
                        Comparator.comparing(
                                movie ->
                                        safeString(
                                                movie.getTitle()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "genre":

                comparator =
                        Comparator.comparing(
                                movie ->
                                        safeString(
                                                movie.getGenre()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "duration":

                comparator =
                        Comparator.comparingInt(
                                Movie::getDuration);

                break;

            case "release_date":

                comparator =
                        Comparator.comparing(
                                Movie::getReleaseDate,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()));

                break;

            case "status":

                comparator =
                        Comparator.comparing(
                                movie ->
                                        safeString(
                                                movie.getStatus()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "created_at":

                comparator =
                        Comparator.comparing(
                                Movie::getCreatedAt,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()));

                break;

            case "movie_id":
            default:

                comparator =
                        Comparator.comparingInt(
                                Movie::getMovieId);

                break;
        }

        if ("DESC".equals(order)) {
            comparator = comparator.reversed();
        }

        movies.sort(comparator);
    }

    /*
     * ==================================================
     * GENRES
     * ==================================================
     */

    private List<String> buildGenreList(
            List<Movie> movies) {

        if (movies == null
                || movies.isEmpty()) {

            return Collections.emptyList();
        }

        List<String> genres =
                new ArrayList<>();

        for (Movie movie : movies) {

            if (movie == null) {
                continue;
            }

            String movieGenre =
                    normalize(movie.getGenre());

            if (movieGenre.isBlank()) {
                continue;
            }

            boolean duplicated = false;

            for (String existingGenre : genres) {

                if (existingGenre.equalsIgnoreCase(
                        movieGenre)) {

                    duplicated = true;
                    break;
                }
            }

            if (!duplicated) {
                genres.add(movieGenre);
            }
        }

        genres.sort(
                String.CASE_INSENSITIVE_ORDER);

        return genres;
    }

    private String findMatchingGenre(
            List<String> genres,
            String selectedGenre) {

        if (genres == null
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

    /*
     * ==================================================
     * VALIDATION
     * ==================================================
     */

    private boolean isValidStatus(
            String status) {

        return status == null
                || status.isBlank()
                || "ACTIVE".equals(status)
                || "INACTIVE".equals(status);
    }

    private String normalizeSortField(
            String sort) {

        String normalizedSort =
                normalize(sort)
                        .toLowerCase(Locale.ROOT);

        switch (normalizedSort) {

            case "movie_id":
            case "title":
            case "genre":
            case "duration":
            case "release_date":
            case "status":
            case "created_at":
                return normalizedSort;

            default:
                return "";
        }
    }

    private String normalizeOrder(
            String order) {

        return "DESC".equalsIgnoreCase(order)
                ? "DESC"
                : "ASC";
    }

    private int parsePositiveInteger(
            String value,
            int defaultValue) {

        if (value == null
                || value.isBlank()) {

            return defaultValue;
        }

        try {

            int parsedValue =
                    Integer.parseInt(
                            value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : defaultValue;

        } catch (NumberFormatException exception) {

            return defaultValue;
        }
    }

    /*
     * ==================================================
     * NORMALIZATION
     * ==================================================
     */

    private String normalizeForSearch(
            String value) {

        String normalizedValue =
                normalize(value)
                        .toLowerCase(Locale.ROOT);

        normalizedValue =
                Normalizer.normalize(
                        normalizedValue,
                        Normalizer.Form.NFD);

        normalizedValue =
                DIACRITICS.matcher(
                        normalizedValue)
                        .replaceAll("");

        /*
         * Chữ đ không được loại bỏ bởi NFD.
         */
        return normalizedValue
                .replace('đ', 'd');
    }

    private String removeControlCharacters(
            String value) {

        if (value == null
                || value.isBlank()) {

            return "";
        }

        StringBuilder result =
                new StringBuilder();

        for (int index = 0;
                index < value.length();
                index++) {

            char character =
                    value.charAt(index);

            if (!Character.isISOControl(character)) {
                result.append(character);
            }
        }

        return result.toString().trim();
    }

    private String normalizeUpper(
            String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String safeString(
            String value) {

        return value == null
                ? ""
                : value.trim();
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
}