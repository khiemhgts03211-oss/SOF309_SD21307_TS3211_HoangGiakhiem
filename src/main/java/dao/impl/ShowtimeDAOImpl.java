package dao.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.ShowtimeDAO;
import model.Showtime;

public class ShowtimeDAOImpl extends BaseDAO implements ShowtimeDAO {

    /*
     * ==============================
     * SQL QUERY
     * ==============================
     */

	private static final String FIND_ALL =
	        "SELECT " +
	        "s.showtime_id, " +
	        "s.movie_id, " +
	        "s.room_id, " +
	        "s.start_time, " +
	        "s.end_time, " +
	        "s.price, " +
	        "s.status, " +
	        "s.created_at, " +
	        "s.show_date, " +
	        "m.title AS movie_title, " +
	        "r.room_name " +
	        "FROM showtimes s " +
	        "INNER JOIN movies m " +
	        "ON s.movie_id = m.movie_id " +
	        "INNER JOIN rooms r " +
	        "ON s.room_id = r.room_id " +
	        "ORDER BY s.showtime_id DESC";

	        private static final String FIND_BY_ID =
	                "SELECT s.*, " +
	                "m.title AS movie_title, " +
	                "r.room_name " +
	                "FROM showtimes s " +
	                "JOIN movies m ON s.movie_id = m.movie_id " +
	                "JOIN rooms r ON s.room_id = r.room_id " +
	                "WHERE s.showtime_id = ?";

	        private static final String INSERT_SQL =
	                "INSERT INTO showtimes "
	                + "(movie_id, room_id, show_date, start_time, end_time, "
	                + "price, status) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

	        private static final String UPDATE_SQL =
	                "UPDATE showtimes SET "
	                + "movie_id = ?, "
	                + "room_id = ?, "
	                + "show_date = ?, "
	                + "start_time = ?, "
	                + "end_time = ?, "
	                + "price = ?, "
	                + "status = ? "
	                + "WHERE showtime_id = ?";

    private static final String DELETE =
            "DELETE FROM showtimes WHERE showtime_id=?";

    private static final String COUNT =
            "SELECT COUNT(*) FROM showtimes";

    private static final String SEARCH =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "WHERE " +
            "m.title LIKE ? " +
            "OR r.room_name LIKE ? " +
            "OR s.status LIKE ? " +
            "ORDER BY s.showtime_id DESC";

    private static final String PAGINATION =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "ORDER BY s.showtime_id DESC " +
            "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    private static final String FIND_BY_MOVIE =
    	    "SELECT " +
    	    "s.showtime_id, " +
    	    "s.movie_id, " +
    	    "s.room_id, " +
    	    "s.show_date, " +
    	    "s.start_time, " +
    	    "s.end_time, " +
    	    "s.price, " +
    	    "s.status, " +
    	    "m.title AS movie_title, " +
    	    "r.room_name " +
    	    "FROM showtimes s " +
    	    "INNER JOIN movies m ON s.movie_id = m.movie_id " +
    	    "INNER JOIN rooms r ON s.room_id = r.room_id " +
    	    "WHERE s.movie_id = ? " +
    	    "AND s.status = 'OPEN' " +
    	    "ORDER BY s.show_date, s.start_time";

    private static final String FIND_BY_ROOM =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "WHERE s.room_id=? " +
            "ORDER BY s.show_date DESC,s.start_time";

    private static final String FIND_BY_DATE =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "WHERE s.show_date=? " +
            "ORDER BY s.start_time";

    private static final String FIND_BY_STATUS =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "WHERE s.status=? " +
            "ORDER BY s.show_date DESC,s.start_time";

    private static final String FIND_AVAILABLE =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id " +
            "WHERE s.status='ACTIVE' " +
            "ORDER BY s.show_date,s.start_time";

    private static final String FIND_BY_MOVIE_AND_DATE =
            "SELECT s.*, " +
            "m.title AS movie_title, " +
            "r.room_name " +
            "FROM showtimes s " +
            "JOIN movies m ON s.movie_id=m.movie_id " +
            "JOIN rooms r ON s.room_id=r.room_id " +
            "WHERE s.movie_id=? " +
            "AND s.show_date=? " +
            "ORDER BY s.start_time";

    private static final String EXISTS_SHOWTIME =
            "SELECT COUNT(*) "
          + "FROM showtimes "
          + "WHERE room_id=? "
          + "AND show_date=? "
          + "AND start_time=?";

    private static final String SORT_BY_DATE =
    	    "SELECT s.*, m.title AS movie_title, r.room_name " +
    	    "FROM showtimes s " +
    	    "JOIN movies m ON s.movie_id = m.movie_id " +
    	    "JOIN rooms r ON s.room_id = r.room_id " +
    	    "ORDER BY s.show_date ASC, s.start_time ASC";

    	private static final String SORT_BY_PRICE =
    	    "SELECT s.*, m.title AS movie_title, r.room_name " +
    	    "FROM showtimes s " +
    	    "JOIN movies m ON s.movie_id = m.movie_id " +
    	    "JOIN rooms r ON s.room_id = r.room_id " +
    	    "ORDER BY s.ticket_price ASC";

    	private static final String SORT_BY_MOVIE =
    	    "SELECT s.*, m.title AS movie_title, r.room_name " +
    	    "FROM showtimes s " +
    	    "JOIN movies m ON s.movie_id = m.movie_id " +
    	    "JOIN rooms r ON s.room_id = r.room_id " +
    	    "ORDER BY m.title ASC";

    	private static final String SORT_BY_ROOM =
    	    "SELECT s.*, m.title AS movie_title, r.room_name " +
    	    "FROM showtimes s " +
    	    "JOIN movies m ON s.movie_id = m.movie_id " +
    	    "JOIN rooms r ON s.room_id = r.room_id " +
    	    "ORDER BY r.room_name ASC";
    	  	
    	private static final String FIND_TODAY =
    	        "SELECT s.*, " +
    	        "m.title AS movie_title, " +
    	        "r.room_name " +
    	        "FROM showtimes s " +
    	        "JOIN movies m ON s.movie_id=m.movie_id " +
    	        "JOIN rooms r ON s.room_id=r.room_id " +
    	        "WHERE CAST(s.start_time AS DATE)=CAST(GETDATE() AS DATE) " +
    	        "ORDER BY s.start_time";
    	
    	private static final String FIND_AVAILABLE_BY_MOVIE =
    	        "SELECT "
    	        + "s.showtime_id, "
    	        + "s.movie_id, "
    	        + "s.room_id, "
    	        + "s.show_date, "
    	        + "s.start_time, "
    	        + "s.end_time, "
    	        + "s.price, "
    	        + "s.status, "
    	        + "s.created_at, "
    	        + "m.title AS movie_title, "
    	        + "r.room_name "
    	        + "FROM showtimes s "
    	        + "INNER JOIN movies m "
    	        + "ON s.movie_id = m.movie_id "
    	        + "INNER JOIN rooms r "
    	        + "ON s.room_id = r.room_id "
    	        + "WHERE s.movie_id = ? "
    	        + "AND UPPER(LTRIM(RTRIM(ISNULL(s.status, '')))) "
    	        + "IN ('OPEN', 'ACTIVE') "
    	        + "AND s.show_date IS NOT NULL "
    	        + "AND s.start_time IS NOT NULL "
    	        + "AND s.room_id > 0 "
    	        + "AND s.price IS NOT NULL "
    	        + "AND s.price > 0 "
    	        + "AND ("
    	        + "    CAST(s.show_date AS DATE) > CAST(GETDATE() AS DATE) "
    	        + "    OR ("
    	        + "        CAST(s.show_date AS DATE) = CAST(GETDATE() AS DATE) "
    	        + "        AND CAST(s.start_time AS TIME) > CAST(GETDATE() AS TIME)"
    	        + "    )"
    	        + ") "
    	        + "ORDER BY "
    	        + "CAST(s.show_date AS DATE) ASC, "
    	        + "CAST(s.start_time AS TIME) ASC";
    	
    /*
     * ==============================
     * RESULT SET MAPPING
     * ==============================
     */

    	private Showtime mapResultSet(ResultSet resultSet)
    	        throws SQLException {

    	    Showtime showtime = new Showtime();

    	    showtime.setShowtimeId(
    	            resultSet.getInt("showtime_id"));

    	    showtime.setMovieId(
    	            resultSet.getInt("movie_id"));

    	    showtime.setRoomId(
    	            resultSet.getInt("room_id"));

    	    /*
    	     * Showtime.java dùng java.sql.Time,
    	     * không dùng Timestamp.
    	     */
    	    showtime.setStartTime(
    	            resultSet.getTime("start_time"));

    	    showtime.setEndTime(
    	            resultSet.getTime("end_time"));

    	    showtime.setTicketPrice(
    	            resultSet.getBigDecimal("price"));

    	    showtime.setStatus(
    	            resultSet.getString("status"));

    	    showtime.setCreatedAt(
    	            resultSet.getTimestamp("created_at"));

    	    showtime.setShowDate(
    	            resultSet.getDate("show_date"));

    	    /*
    	     * Các cột này chỉ có trong câu SQL JOIN.
    	     */
    	    try {
    	        showtime.setMovieTitle(
    	                resultSet.getString("movie_title"));
    	    } catch (SQLException ignored) {
    	        // Query không có movie_title
    	    }

    	    try {
    	        showtime.setRoomName(
    	                resultSet.getString("room_name"));
    	    } catch (SQLException ignored) {
    	        // Query không có room_name
    	    }

    	    return showtime;
    	}
    
    	@Override
    	public List<Showtime> findAll() {

    	    List<Showtime> showtimes =
    	            new ArrayList<>();

    	    try {

    	        connection = getConnection();

    	        preparedStatement =
    	                connection.prepareStatement(FIND_ALL);

    	        resultSet =
    	                preparedStatement.executeQuery();

    	        while (resultSet.next()) {

    	            showtimes.add(
    	                    mapResultSet(resultSet));
    	        }

    	        System.out.println(
    	                "ShowtimeDAOImpl.findAll(): "
    	                        + showtimes.size());

    	    } catch (SQLException exception) {

    	        System.err.println(
    	                "Lỗi lấy danh sách suất chiếu: "
    	                        + exception.getMessage());

    	        exception.printStackTrace();

    	    } finally {

    	        closeResources();
    	    }

    	    return showtimes;
    	}

    @Override
    public Showtime findById(Integer id) {

        Showtime showtime = null;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(FIND_BY_ID);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                showtime = mapResultSet(resultSet);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return showtime;
    }

    @Override
    public boolean insert(Showtime showtime) {

        boolean inserted = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(INSERT_SQL);

            preparedStatement.setInt(1, showtime.getMovieId());
            preparedStatement.setInt(2, showtime.getRoomId());
            preparedStatement.setDate(3, showtime.getShowDate());
            preparedStatement.setTime(4, showtime.getStartTime());
            preparedStatement.setTime(5, showtime.getEndTime());
            preparedStatement.setBigDecimal(6, showtime.getTicketPrice());
            preparedStatement.setString(7, showtime.getStatus());

            inserted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return inserted;
    }

    @Override
    public boolean update(Showtime showtime) {

        boolean updated = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(UPDATE_SQL);

            preparedStatement.setInt(
                    1,
                    showtime.getMovieId());

            preparedStatement.setInt(
                    2,
                    showtime.getRoomId());

            preparedStatement.setDate(
                    3,
                    showtime.getShowDate());

            preparedStatement.setTime(
                    4,
                    showtime.getStartTime());

            preparedStatement.setTime(
                    5,
                    showtime.getEndTime());

            preparedStatement.setBigDecimal(
                    6,
                    showtime.getTicketPrice());

            preparedStatement.setString(
                    7,
                    showtime.getStatus());

            preparedStatement.setInt(
                    8,
                    showtime.getShowtimeId());

            updated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return updated;
    }

    @Override
    public boolean delete(Integer id) {

        boolean deleted = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);

            deleted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return deleted;
    }

    @Override
    public List<Showtime> search(String keyword) {

        List<Showtime> list = new ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(SEARCH);

            String key = "%" + keyword + "%";

            preparedStatement.setString(1, key);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, key);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                list.add(mapResultSet(resultSet));

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return list;
    }

    @Override
    public List<Showtime> findByPage(int page,
                                     int pageSize) {

        // Nếu page hoặc pageSize không hợp lệ
        // thì trả toàn bộ dữ liệu
        if (page <= 0 || pageSize <= 0) {
            return findAll();
        }

        List<Showtime> showtimes =
                new ArrayList<>();

        int offset = (page - 1) * pageSize;

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(PAGINATION);

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, pageSize);

            resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                showtimes.add(
                        mapResultSet(resultSet));

            }

        } catch (SQLException e) {

            e.printStackTrace();

            // Nếu OFFSET FETCH lỗi thì trả về toàn bộ dữ liệu
            return findAll();

        } finally {

            closeResources();

        }

        return showtimes;
    }

	@Override
	public int count() {

	    int total = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(COUNT);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            total = resultSet.getInt(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return total;
	}

	@Override
	public List<Showtime> findByMovie(Integer movieId) {

	    List<Showtime> list = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_BY_MOVIE);

	        preparedStatement.setInt(1, movieId);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            list.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return list;
	}
	
	@Override
	public List<Showtime> findByRoom(Integer roomId) {

	    List<Showtime> showtimes = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ROOM);

	        preparedStatement.setInt(1, roomId);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            showtimes.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return showtimes;
	}
	
	@Override
	public List<Showtime> findByDate(Date showDate) {

	    List<Showtime> showtimes = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_DATE);

	        preparedStatement.setDate(1, showDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            showtimes.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return showtimes;
	}

	@Override
	public List<Showtime> findByMovieAndDate(Integer movieId, Date showDate) {

	    List<Showtime> showtimes = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_MOVIE_AND_DATE);

	        preparedStatement.setInt(1, movieId);
	        preparedStatement.setDate(2, showDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            showtimes.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return showtimes;
	}

	@Override
	public List<Showtime> findByStatus(String status) {

	    List<Showtime> showtimes = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_STATUS);

	        preparedStatement.setString(1, status);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            showtimes.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return showtimes;
	}

	@Override
	public boolean existsShowtime(Integer roomId, Date showDate, Time startTime) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_SHOWTIME);

	        preparedStatement.setInt(1, roomId);
	        preparedStatement.setDate(2, showDate);
	        preparedStatement.setTime(3, startTime);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            exists = resultSet.getInt(1) > 0;

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return exists;
	}
	
	@Override
	public List<Showtime> findAvailableShowtimes() {

	    List<Showtime> showtimes = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_AVAILABLE);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            showtimes.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return showtimes;
	}
	
	@Override
	public List<Showtime> sort(String sortBy) {

	    List<Showtime> list = new ArrayList<>();

	    String sql = FIND_ALL;

	    switch (sortBy) {

	        case "movie":
	            sql = SORT_BY_MOVIE;
	            break;

	        case "room":
	            sql = SORT_BY_ROOM;
	            break;

	        case "price":
	            sql = SORT_BY_PRICE;
	            break;

	        case "date":
	            sql = SORT_BY_DATE;
	            break;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(sql);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            list.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return list;
	}
	
	@Override
	public List<Showtime> findTodayShowtimes() {

	    List<Showtime> list = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_TODAY);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            list.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return list;

	}
	
	@Override
	public List<Showtime> findAvailableByMovie(Integer movieId) {

	    List<Showtime> showtimes = new ArrayList<>();

	    if (movieId == null || movieId <= 0) {
	        return showtimes;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_AVAILABLE_BY_MOVIE);

	        preparedStatement.setInt(1, movieId);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Showtime showtime =
	                    mapResultSet(resultSet);

	            if (showtime != null) {
	                showtimes.add(showtime);
	            }
	        }

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi lấy suất chiếu hợp lệ theo phim: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return showtimes;
	}
	
}