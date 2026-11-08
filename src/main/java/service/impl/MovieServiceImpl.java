package service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import dao.MovieDAO;
import dao.impl.MovieDAOImpl;
import model.Movie;
import service.MovieService;

public class MovieServiceImpl implements MovieService {

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_GENRE_LENGTH = 100;
    private static final int MAX_KEYWORD_LENGTH = 150;
    private static final int MAX_STATUS_LENGTH = 30;
    private static final int MAX_PAGE_SIZE = 100;

    private final MovieDAO movieDAO;

    public MovieServiceImpl() {
        this.movieDAO = new MovieDAOImpl();
    }

    /*
     * ==================================================
     * GENERIC SERVICE
     * ==================================================
     */

    @Override
    public List<Movie> findAll() {

        List<Movie> movies = movieDAO.findAll();

        return safeMovieList(movies);
    }

    @Override
    public Movie findById(Integer movieId) {

        if (movieId == null || movieId <= 0) {
            return null;
        }

        return movieDAO.findById(movieId);
    }

    @Override
    public boolean save(Movie movie) {

        if (!isValidMovieForSave(movie, false)) {
            return false;
        }

        normalizeMovie(movie);

        /*
         * Không cho thêm trùng tên phim.
         */
        if (existsTitle(movie.getTitle())) {
            return false;
        }

        return movieDAO.insert(movie);
    }

    @Override
    public boolean update(Movie movie) {

        if (!isValidMovieForSave(movie, true)) {
            return false;
        }

        /*
         * Phim phải tồn tại trước khi cập nhật.
         */
        Movie existingMovie =
                movieDAO.findById(
                        movie.getMovieId());

        if (existingMovie == null) {
            return false;
        }

        normalizeMovie(movie);

        return movieDAO.update(movie);
    }

    @Override
    public boolean delete(Integer id) {

        if (!isValidId(id)) {
            return false;
        }

        Movie movie = movieDAO.findById(id);

        if (movie == null) {
            return false;
        }

        return movieDAO.delete(id);
    }

    @Override
    public int count() {

        int total = movieDAO.count();

        return Math.max(total, 0);
    }

    @Override
    public List<Movie> search(String keyword) {

        String normalizedKeyword =
                normalizeKeyword(keyword);

        if (normalizedKeyword.isBlank()) {
            return findAll();
        }

        List<Movie> movies =
                movieDAO.search(
                        normalizedKeyword);

        return safeMovieList(movies);
    }

    @Override
    public List<Movie> findByPage(
            int page,
            int pageSize) {

        if (page <= 0 || pageSize <= 0) {
            return Collections.emptyList();
        }

        int safePageSize =
                Math.min(
                        pageSize,
                        MAX_PAGE_SIZE);

        List<Movie> movies =
                movieDAO.findByPage(
                        page,
                        safePageSize);

        return safeMovieList(movies);
    }

    /*
     * ==================================================
     * CUSTOMER MOVIE METHODS
     * ==================================================
     */

    @Override
    public List<Movie> findNowShowing() {

        List<Movie> movies =
                movieDAO.findNowShowing();

        return movies == null
                ? Collections.emptyList()
                : movies;
    }

    @Override
    public List<Movie> findComingSoon() {

        List<Movie> movies =
                movieDAO.findComingSoon();

        return movies == null
                ? Collections.emptyList()
                : movies;
    }

    @Override
    public List<Movie> findByGenre(String genre) {

        String normalizedGenre =
                normalizeGenre(genre);

        if (normalizedGenre.isBlank()) {
            return Collections.emptyList();
        }

        List<Movie> movies =
                movieDAO.findByGenre(
                        normalizedGenre);

        return safeMovieList(movies);
    }

    @Override
    public List<Movie> findByStatus(String status) {

        String normalizedStatus =
                normalizeStatus(status);

        if (!isValidStatus(normalizedStatus)) {
            return Collections.emptyList();
        }

        List<Movie> movies =
                movieDAO.findByStatus(
                        normalizedStatus);

        return safeMovieList(movies);
    }

    @Override
    public boolean existsTitle(String title) {

        String normalizedTitle =
                normalizeTitle(title);

        if (normalizedTitle.isBlank()) {
            return false;
        }

        return movieDAO.existsTitle(
                normalizedTitle);
    }

    @Override
    public List<Movie> searchActiveMovies(
            String keyword) {

        String normalizedKeyword =
                normalizeKeyword(keyword);

        if (normalizedKeyword.isBlank()) {
            return findNowShowing();
        }

        List<Movie> movies =
                movieDAO.searchActiveMovies(
                        normalizedKeyword);

        return safeMovieList(movies);
    }

    @Override
    public List<Movie> searchMovies(
            String keyword,
            String genre) {

        String normalizedKeyword =
                normalizeKeyword(keyword);

        String normalizedGenre =
                normalizeGenre(genre);

        /*
         * Không có điều kiện thì trả danh sách phim ACTIVE.
         */
        if (normalizedKeyword.isBlank()
                && normalizedGenre.isBlank()) {

            return findNowShowing();
        }

        List<Movie> movies =
                movieDAO.searchMovies(
                        normalizedKeyword,
                        normalizedGenre);

        return safeMovieList(movies);
    }

    @Override
    public List<String> getAllGenres() {

        List<String> genres =
                movieDAO.getAllGenres();

        if (genres == null || genres.isEmpty()) {
            return Collections.emptyList();
        }

        /*
         * Loại bỏ giá trị null, rỗng và trùng thể loại.
         */
        return genres.stream()
                .filter(genre ->
                        genre != null
                                && !genre.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    /*
     * ==================================================
     * ADMIN FILTER AND SORT
     * ==================================================
     */

    @Override
    public List<Movie> filter(
            String genre,
            String status) {

        String normalizedGenre =
                normalizeGenre(genre);

        String normalizedStatus =
                normalizeStatus(status);

        if (!normalizedStatus.isBlank()
                && !isValidStatus(normalizedStatus)) {

            return Collections.emptyList();
        }

        List<Movie> movies =
                movieDAO.filter(
                        normalizedGenre,
                        normalizedStatus);

        return safeMovieList(movies);
    }

    @Override
    public List<Movie> sort(
            String sortBy,
            String order) {

        String safeSortBy =
                normalizeSortBy(sortBy);

        String safeOrder =
                normalizeOrder(order);

        List<Movie> movies =
                movieDAO.sort(
                        safeSortBy,
                        safeOrder);

        return safeMovieList(movies);
    }

    /*
     * ==================================================
     * VALIDATION
     * ==================================================
     */

    private boolean isValidMovieForSave(
            Movie movie,
            boolean requireId) {

        if (movie == null) {
            return false;
        }

        if (requireId
                && movie.getMovieId() <= 0) {

            return false;
        }

        String title =
                normalizeTitle(
                        movie.getTitle());

        String genre =
                normalizeGenre(
                        movie.getGenre());

        String status =
                normalizeStatus(
                        movie.getStatus());

        if (title.isBlank()) {
            return false;
        }

        if (title.length() > MAX_TITLE_LENGTH) {
            return false;
        }

        if (genre.length() > MAX_GENRE_LENGTH) {
            return false;
        }

        /*
         * Thời lượng phim phải hợp lý.
         */
        if (movie.getDuration() <= 0
                || movie.getDuration() > 1000) {

            return false;
        }

        if (movie.getReleaseDate() == null) {
            return false;
        }

        if (!isValidStatus(status)) {
            return false;
        }

        return true;
    }

    private boolean isValidId(Integer id) {

        return id != null && id > 0;
    }

    private boolean isValidStatus(String status) {

        return "ACTIVE".equals(status)
                || "INACTIVE".equals(status);
    }

    /*
     * ==================================================
     * NORMALIZATION
     * ==================================================
     */

    private void normalizeMovie(Movie movie) {

        movie.setTitle(
                normalizeTitle(
                        movie.getTitle()));

        movie.setGenre(
                normalizeGenre(
                        movie.getGenre()));

        movie.setDescription(
                normalizeNullable(
                        movie.getDescription()));

        movie.setPoster(
                normalizeNullable(
                        movie.getPoster()));

        movie.setStatus(
                normalizeStatus(
                        movie.getStatus()));
    }

    private String normalizeTitle(String value) {

        String normalizedValue =
                normalize(value);

        if (normalizedValue.length()
                > MAX_TITLE_LENGTH) {

            normalizedValue =
                    normalizedValue.substring(
                            0,
                            MAX_TITLE_LENGTH);
        }

        return normalizedValue;
    }

    private String normalizeGenre(String value) {

        String normalizedValue =
                normalize(value);

        if (normalizedValue.length()
                > MAX_GENRE_LENGTH) {

            normalizedValue =
                    normalizedValue.substring(
                            0,
                            MAX_GENRE_LENGTH);
        }

        return normalizedValue;
    }

    private String normalizeKeyword(String value) {

        String normalizedValue =
                normalize(value);

        if (normalizedValue.length()
                > MAX_KEYWORD_LENGTH) {

            normalizedValue =
                    normalizedValue.substring(
                            0,
                            MAX_KEYWORD_LENGTH);
        }

        return normalizedValue;
    }

    private String normalizeStatus(String value) {

        String normalizedValue =
                normalize(value)
                        .toUpperCase(Locale.ROOT);

        if (normalizedValue.length()
                > MAX_STATUS_LENGTH) {

            return "";
        }

        return normalizedValue;
    }

    private String normalizeNullable(String value) {

        String normalizedValue =
                normalize(value);

        return normalizedValue.isBlank()
                ? null
                : normalizedValue;
    }

    private String normalizeSortBy(String sortBy) {

        String normalizedSortBy =
                normalize(sortBy)
                        .toLowerCase(Locale.ROOT);

        switch (normalizedSortBy) {

            case "title":
            case "genre":
            case "duration":
            case "release_date":
            case "status":
            case "movie_id":
                return normalizedSortBy;

            default:
                return "movie_id";
        }
    }

    private String normalizeOrder(String order) {

        return "ASC".equalsIgnoreCase(
                normalize(order))
                        ? "ASC"
                        : "DESC";
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private List<Movie> safeMovieList(
            List<Movie> movies) {

        return movies == null
                ? Collections.emptyList()
                : movies;
    }
}