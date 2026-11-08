package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dao.BaseDAO;
import dao.MovieDAO;
import model.Movie;

public class MovieDAOImpl extends BaseDAO implements MovieDAO {

    /*
     * ==================================================
     * SQL QUERY
     * ==================================================
     */

    private static final String FIND_ALL =
            "SELECT * "
            + "FROM movies "
            + "ORDER BY movie_id DESC";

    private static final String FIND_BY_ID =
            "SELECT * "
            + "FROM movies "
            + "WHERE movie_id = ?";

    private static final String INSERT =
            "INSERT INTO movies ("
            + "title, "
            + "genre, "
            + "duration, "
            + "description, "
            + "release_date, "
            + "poster, "
            + "status"
            + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE movies SET "
            + "title = ?, "
            + "genre = ?, "
            + "duration = ?, "
            + "description = ?, "
            + "release_date = ?, "
            + "poster = ?, "
            + "status = ? "
            + "WHERE movie_id = ?";

    private static final String DELETE =
            "DELETE FROM movies "
            + "WHERE movie_id = ?";

    private static final String COUNT =
            "SELECT COUNT(*) AS total "
            + "FROM movies";

    private static final String SEARCH =
            "SELECT * "
            + "FROM movies "
            + "WHERE ISNULL(title, '') "
            + "      COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(genre, '') "
            + "      COLLATE Latin1_General_CI_AI LIKE ? "
            + "ORDER BY movie_id DESC";

    private static final String PAGINATION =
            "SELECT * "
            + "FROM movies "
            + "ORDER BY movie_id DESC "
            + "OFFSET ? ROWS "
            + "FETCH NEXT ? ROWS ONLY";

    /*
     * Chỉ hiển thị phim ACTIVE cho khách hàng.
     */
    private static final String FIND_NOW_SHOWING =
            "SELECT * "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = 'ACTIVE' "
            + "AND release_date IS NOT NULL "
            + "AND release_date <= CAST(GETDATE() AS DATE) "
            + "ORDER BY release_date DESC, movie_id DESC";

    private static final String FIND_COMING_SOON =
            "SELECT * "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = 'ACTIVE' "
            + "AND release_date IS NOT NULL "
            + "AND release_date > CAST(GETDATE() AS DATE) "
            + "ORDER BY release_date ASC, movie_id ASC";

    private static final String FIND_BY_GENRE =
            "SELECT * "
            + "FROM movies "
            + "WHERE ISNULL(genre, '') "
            + "      COLLATE Latin1_General_CI_AI = ? "
            + "ORDER BY title ASC";

    private static final String FIND_BY_STATUS =
            "SELECT * "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = ? "
            + "ORDER BY title ASC";

    private static final String EXISTS_TITLE =
            "SELECT COUNT(*) AS total "
            + "FROM movies "
            + "WHERE LTRIM(RTRIM(ISNULL(title, ''))) "
            + "      COLLATE Latin1_General_CI_AI = ?";

    private static final String FILTER =
            "SELECT * "
            + "FROM movies "
            + "WHERE (? = '' "
            + "       OR ISNULL(genre, '') "
            + "          COLLATE Latin1_General_CI_AI = ?) "
            + "AND (? = '' "
            + "     OR UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = ?) "
            + "ORDER BY movie_id DESC";

    /*
     * Tìm phim ACTIVE theo title hoặc genre.
     */
    private static final String SEARCH_ACTIVE_MOVIES =
            "SELECT * "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = 'ACTIVE' "
            + "AND ("
            + "       ISNULL(title, '') "
            + "           COLLATE Latin1_General_CI_AI LIKE ? "
            + "    OR ISNULL(genre, '') "
            + "           COLLATE Latin1_General_CI_AI LIKE ?"
            + ") "
            + "ORDER BY release_date DESC, movie_id DESC";

    /*
     * Tìm kiếm kết hợp lọc thể loại.
     */
    private static final String SEARCH_AND_FILTER_ACTIVE_MOVIES =
            "SELECT * "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = 'ACTIVE' "
            + "AND ("
            + "       ? = '' "
            + "    OR ISNULL(title, '') "
            + "       COLLATE Latin1_General_CI_AI LIKE ?"
            + ") "
            + "AND ("
            + "       ? = '' "
            + "    OR ISNULL(genre, '') "
            + "       COLLATE Latin1_General_CI_AI = ?"
            + ") "
            + "ORDER BY release_date DESC, movie_id DESC";

    private static final String FIND_ALL_ACTIVE_GENRES =
            "SELECT DISTINCT "
            + "LTRIM(RTRIM(genre)) AS genre "
            + "FROM movies "
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(status, '')))) = 'ACTIVE' "
            + "AND genre IS NOT NULL "
            + "AND LTRIM(RTRIM(genre)) <> '' "
            + "ORDER BY genre ASC";

    /*
     * ==================================================
     * RESULT SET MAPPING
     * ==================================================
     */

    private Movie mapResultSet(ResultSet resultSet)
            throws SQLException {

        if (resultSet == null) {
            return null;
        }

        Movie movie = new Movie();

        movie.setMovieId(
                resultSet.getInt("movie_id"));

        movie.setTitle(
                normalizeDatabaseValue(
                        resultSet.getString("title")));

        movie.setGenre(
                normalizeDatabaseValue(
                        resultSet.getString("genre")));

        movie.setDuration(
                resultSet.getInt("duration"));

        movie.setDescription(
                normalizeDatabaseValue(
                        resultSet.getString("description")));

        movie.setReleaseDate(
                resultSet.getDate("release_date"));

        movie.setPoster(
                normalizeDatabaseValue(
                        resultSet.getString("poster")));

        movie.setStatus(
                normalizeUpper(
                        resultSet.getString("status")));

        /*
         * Cột created_at đang có trong bảng movies
         * của project hiện tại.
         */
        movie.setCreatedAt(
                resultSet.getTimestamp("created_at"));

        return movie;
    }

    /*
     * ==================================================
     * CRUD
     * ==================================================
     */

    @Override
    public List<Movie> findAll() {

        List<Movie> movies = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(FIND_ALL);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi lấy danh sách phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public Movie findById(Integer movieId) {

        if (movieId == null || movieId <= 0) {
            return null;
        }

        Movie movie = null;

        String sql =
                "SELECT "
                + "movie_id, "
                + "title, "
                + "genre, "
                + "duration, "
                + "release_date, "
                + "description, "
                + "poster, "
                + "status, "
                + "created_at "
                + "FROM movies "
                + "WHERE movie_id = ?";

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setInt(1, movieId);

            resultSet =
                    preparedStatement.executeQuery();

            if (resultSet.next()) {
                movie = mapResultSet(resultSet);
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Lỗi lấy chi tiết phim: "
                            + exception.getMessage());

            exception.printStackTrace();

        } finally {

            closeResources();
        }

        return movie;
    }

    @Override
    public boolean insert(Movie movie) {

        if (!isValidMovieForSave(movie, false)) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(INSERT);

            preparedStatement.setString(
                    1,
                    normalize(movie.getTitle()));

            preparedStatement.setString(
                    2,
                    normalize(movie.getGenre()));

            preparedStatement.setInt(
                    3,
                    movie.getDuration());

            preparedStatement.setString(
                    4,
                    normalizeNullable(
                            movie.getDescription()));

            preparedStatement.setDate(
                    5,
                    movie.getReleaseDate());

            preparedStatement.setString(
                    6,
                    normalizeNullable(
                            movie.getPoster()));

            preparedStatement.setString(
                    7,
                    normalizeUpper(
                            movie.getStatus()));

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi thêm phim",
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public boolean update(Movie movie) {

        if (!isValidMovieForSave(movie, true)) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(UPDATE);

            preparedStatement.setString(
                    1,
                    normalize(movie.getTitle()));

            preparedStatement.setString(
                    2,
                    normalize(movie.getGenre()));

            preparedStatement.setInt(
                    3,
                    movie.getDuration());

            preparedStatement.setString(
                    4,
                    normalizeNullable(
                            movie.getDescription()));

            preparedStatement.setDate(
                    5,
                    movie.getReleaseDate());

            preparedStatement.setString(
                    6,
                    normalizeNullable(
                            movie.getPoster()));

            preparedStatement.setString(
                    7,
                    normalizeUpper(
                            movie.getStatus()));

            preparedStatement.setInt(
                    8,
                    movie.getMovieId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi cập nhật phim ID: "
                            + movie.getMovieId(),
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public boolean delete(Integer id) {

        if (id == null || id <= 0) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi xóa phim ID: " + id,
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public int count() {

        int total = 0;

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(COUNT);

            resultSet =
                    preparedStatement.executeQuery();

            if (resultSet.next()) {

                total =
                        resultSet.getInt("total");
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi đếm số lượng phim",
                    exception);

        } finally {

            closeResources();
        }

        return total;
    }

    /*
     * ==================================================
     * SEARCH / FILTER / PAGINATION
     * ==================================================
     */

    @Override
    public List<Movie> search(String keyword) {

        String normalizedKeyword =
                normalize(keyword);

        if (normalizedKeyword.isBlank()) {
            return findAll();
        }

        List<Movie> movies =
                new ArrayList<>();

        String searchValue =
                "%" + normalizedKeyword + "%";

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(SEARCH);

            preparedStatement.setString(
                    1,
                    searchValue);

            preparedStatement.setString(
                    2,
                    searchValue);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm kiếm phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> findByPage(
            int page,
            int pageSize) {

        List<Movie> movies =
                new ArrayList<>();

        if (page <= 0 || pageSize <= 0) {
            return movies;
        }

        int safePageSize =
                Math.min(pageSize, 100);

        int offset =
                (page - 1) * safePageSize;

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            PAGINATION);

            preparedStatement.setInt(
                    1,
                    offset);

            preparedStatement.setInt(
                    2,
                    safePageSize);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi phân trang phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> filter(
            String genre,
            String status) {

        List<Movie> movies =
                new ArrayList<>();

        String normalizedGenre =
                normalize(genre);

        String normalizedStatus =
                normalizeUpper(status);

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(FILTER);

            preparedStatement.setString(
                    1,
                    normalizedGenre);

            preparedStatement.setString(
                    2,
                    normalizedGenre);

            preparedStatement.setString(
                    3,
                    normalizedStatus);

            preparedStatement.setString(
                    4,
                    normalizedStatus);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi lọc phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> sort(
            String sortBy,
            String order) {

        List<Movie> movies =
                new ArrayList<>();

        String safeSortColumn =
                getSafeSortColumn(sortBy);

        String safeOrder =
                "ASC".equalsIgnoreCase(order)
                        ? "ASC"
                        : "DESC";

        /*
         * sortBy chỉ lấy từ danh sách cố định,
         * không dùng trực tiếp giá trị người dùng.
         */
        String sql =
                "SELECT * "
                + "FROM movies "
                + "ORDER BY "
                + safeSortColumn
                + " "
                + safeOrder;

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(sql);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi sắp xếp phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    /*
     * ==================================================
     * CUSTOMER MOVIE DATA
     * ==================================================
     */

    @Override
    public List<Movie> findNowShowing() {

        List<Movie> movies = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_NOW_SHOWING);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie = mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Lỗi lấy phim đang chiếu: "
                            + exception.getMessage());

            exception.printStackTrace();

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> findComingSoon() {

        List<Movie> movies = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_COMING_SOON);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie = mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Lỗi lấy phim sắp chiếu: "
                            + exception.getMessage());

            exception.printStackTrace();

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> findByGenre(
            String genre) {

        List<Movie> movies =
                new ArrayList<>();

        String normalizedGenre =
                normalize(genre);

        if (normalizedGenre.isBlank()) {
            return movies;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_BY_GENRE);

            preparedStatement.setString(
                    1,
                    normalizedGenre);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm phim theo thể loại",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> findByStatus(
            String status) {

        List<Movie> movies =
                new ArrayList<>();

        String normalizedStatus =
                normalizeUpper(status);

        if (normalizedStatus.isBlank()) {
            return movies;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_BY_STATUS);

            preparedStatement.setString(
                    1,
                    normalizedStatus);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (movie != null) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm phim theo trạng thái",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public boolean existsTitle(String title) {

        String normalizedTitle =
                normalize(title);

        if (normalizedTitle.isBlank()) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            EXISTS_TITLE);

            preparedStatement.setString(
                    1,
                    normalizedTitle);

            resultSet =
                    preparedStatement.executeQuery();

            return resultSet.next()
                    && resultSet.getInt("total") > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi kiểm tra tên phim tồn tại",
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public List<Movie> searchActiveMovies(
            String keyword) {

        String normalizedKeyword =
                normalize(keyword);

        if (normalizedKeyword.isBlank()) {
            return findNowShowing();
        }

        List<Movie> movies =
                new ArrayList<>();

        String searchValue =
                "%" + normalizedKeyword + "%";

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            SEARCH_ACTIVE_MOVIES);

            preparedStatement.setString(
                    1,
                    searchValue);

            preparedStatement.setString(
                    2,
                    searchValue);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (isActiveMovie(movie)) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm kiếm phim đang hoạt động",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<Movie> searchMovies(
            String keyword,
            String genre) {

        List<Movie> movies =
                new ArrayList<>();

        String normalizedKeyword =
                normalize(keyword);

        String normalizedGenre =
                normalize(genre);

        String keywordPattern =
                normalizedKeyword.isBlank()
                        ? ""
                        : "%" + normalizedKeyword + "%";

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            SEARCH_AND_FILTER_ACTIVE_MOVIES);

            preparedStatement.setString(
                    1,
                    normalizedKeyword);

            preparedStatement.setString(
                    2,
                    keywordPattern);

            preparedStatement.setString(
                    3,
                    normalizedGenre);

            preparedStatement.setString(
                    4,
                    normalizedGenre);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie =
                        mapResultSet(resultSet);

                if (isActiveMovie(movie)) {
                    movies.add(movie);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm kiếm và lọc phim",
                    exception);

        } finally {

            closeResources();
        }

        return movies;
    }

    @Override
    public List<String> getAllGenres() {

        List<String> genres =
                new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_ALL_ACTIVE_GENRES);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                String genre =
                        normalizeDatabaseValue(
                                resultSet.getString("genre"));

                if (!genre.isBlank()) {
                    genres.add(genre);
                }
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi lấy danh sách thể loại",
                    exception);

        } finally {

            closeResources();
        }

        return genres;
    }

    /*
     * ==================================================
     * VALIDATION HELPERS
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
                normalize(movie.getTitle());

        String genre =
                normalize(movie.getGenre());

        String status =
                normalizeUpper(movie.getStatus());

        if (title.isBlank()
                || title.length() > 255) {

            return false;
        }

        if (genre.length() > 100) {
            return false;
        }

        if (movie.getDuration() <= 0
                || movie.getDuration() > 1000) {

            return false;
        }

        if (!isValidStatus(status)) {
            return false;
        }

        return true;
    }

    private boolean isActiveMovie(Movie movie) {

        return movie != null
                && "ACTIVE".equalsIgnoreCase(
                        normalize(movie.getStatus()));
    }

    private boolean isValidStatus(String status) {

        return "ACTIVE".equals(status)
                || "INACTIVE".equals(status);
    }

    private String getSafeSortColumn(
            String sortBy) {

        String normalizedSortBy =
                normalize(sortBy)
                        .toLowerCase(Locale.ROOT);

        switch (normalizedSortBy) {

            case "title":
                return "title";

            case "release_date":
                return "release_date";

            case "duration":
                return "duration";

            case "genre":
                return "genre";

            case "status":
                return "status";

            default:
                return "movie_id";
        }
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalizeUpper(String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalizeNullable(String value) {

        String normalizedValue =
                normalize(value);

        return normalizedValue.isBlank()
                ? null
                : normalizedValue;
    }

    private String normalizeDatabaseValue(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private void logError(
            String message,
            SQLException exception) {

        System.err.println(
                message
                        + ": "
                        + exception.getMessage());

        exception.printStackTrace();
    }
}