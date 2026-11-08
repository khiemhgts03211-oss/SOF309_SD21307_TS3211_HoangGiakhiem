package dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dao.BaseDAO;
import dao.TicketDAO;
import model.Ticket;

public class TicketDAOImpl extends BaseDAO implements TicketDAO {

    /*
     * ==================================================
     * SQL dùng chung cho các truy vấn cần hiển thị đầy đủ
     * thông tin khách hàng, phim, phòng và suất chiếu.
     * ==================================================
     */
    private static final String BASE_SELECT =
            "SELECT "
            + "t.ticket_id, "
            + "t.booking_code, "
            + "t.user_id, "
            + "t.showtime_id, "
            + "t.room_id, "
            + "t.people_count, "
            + "t.total_price, "
            + "t.status, "
            + "t.created_at, "
            + "u.full_name AS customer_name, "
            + "u.email AS customer_email, "
            + "u.phone AS customer_phone, "
            + "m.title AS movie_title, "
            + "r.room_name AS room_name, "
            + "s.start_time AS start_time "
            + "FROM tickets t "
            + "LEFT JOIN users u "
            + "ON t.user_id = u.user_id "
            + "LEFT JOIN showtimes s "
            + "ON t.showtime_id = s.showtime_id "
            + "LEFT JOIN movies m "
            + "ON s.movie_id = m.movie_id "
            + "LEFT JOIN rooms r "
            + "ON t.room_id = r.room_id ";

    private static final String FIND_ALL =
            BASE_SELECT
            + "ORDER BY t.ticket_id DESC";

    private static final String FIND_BY_ID =
            BASE_SELECT
            + "WHERE t.ticket_id = ?";

    private static final String INSERT =
            "INSERT INTO tickets ("
            + "booking_code, "
            + "user_id, "
            + "showtime_id, "
            + "room_id, "
            + "people_count, "
            + "total_price, "
            + "status"
            + ") "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE tickets SET "
            + "booking_code = ?, "
            + "user_id = ?, "
            + "showtime_id = ?, "
            + "room_id = ?, "
            + "people_count = ?, "
            + "total_price = ?, "
            + "status = ? "
            + "WHERE ticket_id = ?";

    private static final String DELETE_TICKET_DETAIL =
            "DELETE FROM ticket_details WHERE ticket_id = ?";

    private static final String DELETE_PAYMENT =
            "DELETE FROM payments WHERE ticket_id = ?";

    private static final String DELETE =
            "DELETE FROM tickets WHERE ticket_id = ?";

    private static final String COUNT =
            "SELECT COUNT(*) FROM tickets";

    private static final String SEARCH =
            BASE_SELECT
            + "WHERE t.booking_code COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(u.full_name, '') COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(u.email, '') COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(u.phone, '') COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(m.title, '') COLLATE Latin1_General_CI_AI LIKE ? "
            + "OR ISNULL(r.room_name, '') COLLATE Latin1_General_CI_AI LIKE ? "
            + "ORDER BY t.created_at DESC";

    private static final String PAGINATION =
            BASE_SELECT
            + "ORDER BY t.ticket_id DESC "
            + "OFFSET ? ROWS "
            + "FETCH NEXT ? ROWS ONLY";

    private static final String FIND_BY_USER =
            BASE_SELECT
            + "WHERE t.user_id = ? "
            + "ORDER BY t.created_at DESC";

    private static final String FIND_BY_SHOWTIME =
            BASE_SELECT
            + "WHERE t.showtime_id = ? "
            + "ORDER BY t.created_at DESC";

    private static final String FIND_BY_BOOKING_CODE =
            BASE_SELECT
            + "WHERE t.booking_code = ?";

    private static final String FIND_BY_STATUS =
            BASE_SELECT
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(t.status, '')))) = "
            + "UPPER(LTRIM(RTRIM(?))) "
            + "ORDER BY t.ticket_id DESC";

    private static final String FIND_BY_DATE_RANGE =
            BASE_SELECT
            + "WHERE t.created_at BETWEEN ? AND ? "
            + "ORDER BY t.created_at DESC";

    private static final String FIND_BY_USER_AND_STATUS =
            BASE_SELECT
            + "WHERE t.user_id = ? "
            + "AND UPPER(LTRIM(RTRIM(ISNULL(t.status, '')))) = "
            + "UPPER(LTRIM(RTRIM(?))) "
            + "ORDER BY t.created_at DESC";

    private static final String EXISTS_BOOKING_CODE =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE booking_code = ?";

    private static final String FIND_BY_DATE =
            BASE_SELECT
            + "WHERE CAST(t.created_at AS DATE) = ? "
            + "ORDER BY t.created_at DESC";

    private static final String COUNT_BY_USER =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE user_id = ?";

    private static final String TOTAL_REVENUE =
            "SELECT ISNULL(SUM(total_price), 0) "
            + "FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'PAID'";

    private static final String FILTER =
            BASE_SELECT
            + "WHERE UPPER(LTRIM(RTRIM(ISNULL(t.status, '')))) = "
            + "UPPER(LTRIM(RTRIM(?))) "
            + "ORDER BY t.created_at DESC";

    private static final String UPDATE_STATUS =
            "UPDATE tickets SET status = ? "
            + "WHERE ticket_id = ?";

    private static final String COUNT_BOOKED =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'BOOKED'";

    private static final String COUNT_PAID =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'PAID'";

    private static final String CHECK_IN =
            "UPDATE tickets SET status = 'CHECKED_IN' "
            + "WHERE ticket_id = ?";

    private static final String UPDATE_TOTAL_PRICE =
            "UPDATE tickets SET total_price = ? "
            + "WHERE ticket_id = ?";

    private static final String INCREASE_TOTAL_PRICE =
            "UPDATE tickets "
            + "SET total_price = total_price + ? "
            + "WHERE ticket_id = ?";

    private static final String COUNT_CHECKED_IN =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'CHECKED_IN'";

    private static final String COUNT_CANCELLED =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'CANCELLED'";

    private static final String COUNT_TODAY =
            "SELECT COUNT(*) FROM tickets "
            + "WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";

    private static final String TODAY_REVENUE =
            "SELECT ISNULL(SUM(total_price), 0) "
            + "FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'PAID' "
            + "AND CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";

    private static final String MONTH_REVENUE =
            "SELECT ISNULL(SUM(total_price), 0) "
            + "FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'PAID' "
            + "AND MONTH(created_at) = ? "
            + "AND YEAR(created_at) = ?";

    private static final String YEAR_REVENUE =
            "SELECT ISNULL(SUM(total_price), 0) "
            + "FROM tickets "
            + "WHERE UPPER(LTRIM(RTRIM(status))) = 'PAID' "
            + "AND YEAR(created_at) = ?";

    /*
     * ==================================================
     * RESULT SET MAPPING
     * ==================================================
     */
    private Ticket mapResultSet(ResultSet resultSet)
            throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setTicketId(resultSet.getInt("ticket_id"));
        ticket.setBookingCode(resultSet.getString("booking_code"));
        ticket.setUserId(resultSet.getInt("user_id"));
        ticket.setShowtimeId(resultSet.getInt("showtime_id"));
        ticket.setRoomId(resultSet.getInt("room_id"));
        ticket.setPeopleCount(resultSet.getInt("people_count"));
        ticket.setTotalPrice(resultSet.getBigDecimal("total_price"));
        ticket.setStatus(resultSet.getString("status"));
        ticket.setCreatedAt(resultSet.getTimestamp("created_at"));

        ticket.setCustomerName(resultSet.getString("customer_name"));
        ticket.setCustomerEmail(resultSet.getString("customer_email"));
        ticket.setCustomerPhone(resultSet.getString("customer_phone"));

        ticket.setMovieTitle(resultSet.getString("movie_title"));
        ticket.setRoomName(resultSet.getString("room_name"));

        Timestamp startTime =
                resultSet.getTimestamp("start_time");

        ticket.setShowtime(startTime);
        ticket.setStartTime(startTime);

        return ticket;
    }

    @Override
    public List<Ticket> findAll() {

        return executeTicketListQuery(FIND_ALL);
    }

    @Override
    public Ticket findById(Integer id) {

        if (!isValidId(id)) {
            return null;
        }

        Ticket ticket = null;

        try {

            connection = getConnection();
            preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ticket = mapResultSet(resultSet);
            }

        } catch (SQLException exception) {

            logError("Lỗi tìm vé theo ID: " + id, exception);

        } finally {

            closeResources();
        }

        return ticket;
    }

    @Override
    public boolean insert(Ticket ticket) {

        if (ticket == null) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            INSERT,
                            Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(
                    1,
                    ticket.getBookingCode());

            preparedStatement.setInt(
                    2,
                    ticket.getUserId());

            preparedStatement.setInt(
                    3,
                    ticket.getShowtimeId());

            preparedStatement.setInt(
                    4,
                    ticket.getRoomId());

            preparedStatement.setInt(
                    5,
                    ticket.getPeopleCount());

            preparedStatement.setBigDecimal(
                    6,
                    ticket.getTotalPrice());

            preparedStatement.setString(
                    7,
                    ticket.getStatus());

            int affectedRows =
                    preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            ResultSet generatedKeys =
                    preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                ticket.setTicketId(
                        generatedKeys.getInt(1));
            }

            generatedKeys.close();

            return ticket.getTicketId() > 0;

        } catch (Exception exception) {

            exception.printStackTrace();

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public boolean update(Ticket ticket) {

        if (!isValidTicket(ticket, true)) {
            return false;
        }

        try {

            connection = getConnection();
            preparedStatement = connection.prepareStatement(UPDATE);

            preparedStatement.setString(
                    1,
                    normalize(ticket.getBookingCode()));

            preparedStatement.setInt(2, ticket.getUserId());
            preparedStatement.setInt(3, ticket.getShowtimeId());
            preparedStatement.setInt(4, ticket.getRoomId());
            preparedStatement.setInt(5, ticket.getPeopleCount());
            preparedStatement.setBigDecimal(6, ticket.getTotalPrice());
            preparedStatement.setString(
                    7,
                    normalizeStatus(ticket.getStatus()));

            preparedStatement.setInt(8, ticket.getTicketId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi cập nhật vé ID: "
                            + ticket.getTicketId(),
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public boolean delete(Integer id) {

        if (!isValidId(id)) {
            return false;
        }

        boolean deleted = false;

        try {

            connection = getConnection();
            connection.setAutoCommit(false);

            try (java.sql.PreparedStatement deleteDetails =
                    connection.prepareStatement(
                            DELETE_TICKET_DETAIL)) {

                deleteDetails.setInt(1, id);
                deleteDetails.executeUpdate();
            }

            try (java.sql.PreparedStatement deletePayments =
                    connection.prepareStatement(
                            DELETE_PAYMENT)) {

                deletePayments.setInt(1, id);
                deletePayments.executeUpdate();
            }

            try (java.sql.PreparedStatement deleteTicket =
                    connection.prepareStatement(DELETE)) {

                deleteTicket.setInt(1, id);
                deleted = deleteTicket.executeUpdate() > 0;
            }

            connection.commit();

        } catch (SQLException exception) {

            rollbackQuietly();
            logError("Lỗi xóa vé ID: " + id, exception);

        } finally {

            restoreAutoCommitQuietly();
            closeResources();
        }

        return deleted;
    }

    @Override
    public List<Ticket> search(String keyword) {

        String normalizedKeyword = normalize(keyword);

        if (normalizedKeyword.isBlank()) {
            return findAll();
        }

        List<Ticket> tickets = new ArrayList<>();
        String value = "%" + normalizedKeyword + "%";

        try {

            connection = getConnection();
            preparedStatement = connection.prepareStatement(SEARCH);

            for (int index = 1; index <= 6; index++) {
                preparedStatement.setString(index, value);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tickets.add(mapResultSet(resultSet));
            }

        } catch (SQLException exception) {

            logError("Lỗi tìm kiếm vé", exception);

        } finally {

            closeResources();
        }

        return tickets;
    }

    @Override
    public List<Ticket> findByPage(
            int page,
            int pageSize) {

        List<Ticket> tickets = new ArrayList<>();

        if (page <= 0 || pageSize <= 0) {
            return tickets;
        }

        int safePageSize = Math.min(pageSize, 100);
        int offset = (page - 1) * safePageSize;

        try {

            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(PAGINATION);

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, safePageSize);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tickets.add(mapResultSet(resultSet));
            }

        } catch (SQLException exception) {

            logError("Lỗi phân trang vé", exception);

        } finally {

            closeResources();
        }

        return tickets;
    }

    @Override
    public int count() {

        return executeCountQuery(COUNT);
    }

    @Override
    public List<Ticket> findByUser(Integer userId) {

        if (!isValidId(userId)) {
            return new ArrayList<>();
        }

        return executeTicketListQuery(
                FIND_BY_USER,
                userId);
    }

    @Override
    public List<Ticket> findByShowtime(Integer showtimeId) {

        if (!isValidId(showtimeId)) {
            return new ArrayList<>();
        }

        return executeTicketListQuery(
                FIND_BY_SHOWTIME,
                showtimeId);
    }

    @Override
    public List<Ticket> findByStatus(String status) {

        String normalizedStatus =
                normalizeStatus(status);

        if (normalizedStatus.isBlank()) {
            return findAll();
        }

        return executeTicketListQuery(
                FIND_BY_STATUS,
                normalizedStatus);
    }

    @Override
    public Ticket findByBookingCode(
            String bookingCode) {

        String normalizedCode =
                normalize(bookingCode);

        if (normalizedCode.isBlank()) {
            return null;
        }

        Ticket ticket = null;

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            FIND_BY_BOOKING_CODE);

            preparedStatement.setString(
                    1,
                    normalizedCode);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ticket = mapResultSet(resultSet);
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi tìm vé theo mã đặt vé",
                    exception);

        } finally {

            closeResources();
        }

        return ticket;
    }

    @Override
    public List<Ticket> findByDateRange(
            Timestamp fromDate,
            Timestamp toDate) {

        if (fromDate == null
                || toDate == null
                || fromDate.after(toDate)) {

            return new ArrayList<>();
        }

        return executeTicketListQuery(
                FIND_BY_DATE_RANGE,
                fromDate,
                toDate);
    }

    @Override
    public List<Ticket> findByUserAndStatus(
            Integer userId,
            String status) {

        if (!isValidId(userId)) {
            return new ArrayList<>();
        }

        String normalizedStatus =
                normalizeStatus(status);

        if (normalizedStatus.isBlank()) {
            return findByUser(userId);
        }

        return executeTicketListQuery(
                FIND_BY_USER_AND_STATUS,
                userId,
                normalizedStatus);
    }

    @Override
    public boolean existsBookingCode(
            String bookingCode) {

        String normalizedCode =
                normalize(bookingCode);

        if (normalizedCode.isBlank()) {
            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            EXISTS_BOOKING_CODE);

            preparedStatement.setString(
                    1,
                    normalizedCode);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    && resultSet.getInt(1) > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi kiểm tra mã đặt vé",
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public List<Ticket> findByDate(
            Date bookingDate) {

        if (bookingDate == null) {
            return new ArrayList<>();
        }

        return executeTicketListQuery(
                FIND_BY_DATE,
                bookingDate);
    }

    @Override
    public int countByUser(Integer userId) {

        if (!isValidId(userId)) {
            return 0;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            COUNT_BY_USER);

            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? resultSet.getInt(1)
                    : 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi đếm vé theo người dùng",
                    exception);

            return 0;

        } finally {

            closeResources();
        }
    }

    @Override
    public double getTotalRevenue() {

        return executeDoubleQuery(TOTAL_REVENUE);
    }

    @Override
    public List<Ticket> filter(String status) {

        String normalizedStatus =
                normalizeStatus(status);

        if (normalizedStatus.isBlank()) {
            return findAll();
        }

        return executeTicketListQuery(
                FILTER,
                normalizedStatus);
    }

    @Override
    public List<Ticket> sort(
            String sort,
            String order) {

        List<Ticket> tickets = new ArrayList<>();

        String safeSortColumn =
                getSafeSortColumn(sort);

        String safeOrder =
                "DESC".equalsIgnoreCase(order)
                        ? "DESC"
                        : "ASC";

        String sql =
                BASE_SELECT
                + "ORDER BY "
                + safeSortColumn
                + " "
                + safeOrder;

        try {

            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tickets.add(mapResultSet(resultSet));
            }

        } catch (SQLException exception) {

            logError("Lỗi sắp xếp vé", exception);

        } finally {

            closeResources();
        }

        return tickets;
    }

    @Override
    public void updateStatus(
            Integer ticketId,
            String status) {

        if (!isValidId(ticketId)) {
            return;
        }

        String normalizedStatus =
                normalizeStatus(status);

        if (normalizedStatus.isBlank()) {
            return;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            UPDATE_STATUS);

            preparedStatement.setString(
                    1,
                    normalizedStatus);

            preparedStatement.setInt(
                    2,
                    ticketId);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {

            logError(
                    "Lỗi cập nhật trạng thái vé",
                    exception);

        } finally {

            closeResources();
        }
    }

    @Override
    public int countBookedTickets() {

        return executeCountQuery(COUNT_BOOKED);
    }

    @Override
    public int countPaidTickets() {

        return executeCountQuery(COUNT_PAID);
    }

    @Override
    public boolean checkIn(Integer ticketId) {

        if (!isValidId(ticketId)) {
            return false;
        }

        return executeUpdateById(
                CHECK_IN,
                ticketId);
    }

    @Override
    public boolean updateTotalPrice(
            Integer ticketId,
            BigDecimal totalPrice) {

        if (!isValidId(ticketId)
                || totalPrice == null
                || totalPrice.compareTo(BigDecimal.ZERO) < 0) {

            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            UPDATE_TOTAL_PRICE);

            preparedStatement.setBigDecimal(
                    1,
                    totalPrice);

            preparedStatement.setInt(
                    2,
                    ticketId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi cập nhật tổng tiền vé",
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public boolean increaseTotalPrice(
            Integer ticketId,
            BigDecimal amount) {

        if (!isValidId(ticketId)
                || amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            return false;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            INCREASE_TOTAL_PRICE);

            preparedStatement.setBigDecimal(
                    1,
                    amount);

            preparedStatement.setInt(
                    2,
                    ticketId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi tăng tổng tiền vé",
                    exception);

            return false;

        } finally {

            closeResources();
        }
    }

    @Override
    public int countCheckedInTickets() {

        return executeCountQuery(COUNT_CHECKED_IN);
    }

    @Override
    public int countCancelledTickets() {

        return executeCountQuery(COUNT_CANCELLED);
    }

    @Override
    public int countTodayTickets() {

        return executeCountQuery(COUNT_TODAY);
    }

    @Override
    public double getTodayRevenue() {

        return executeDoubleQuery(TODAY_REVENUE);
    }

    @Override
    public double getMonthlyRevenue(
            int month,
            int year) {

        if (month < 1
                || month > 12
                || year <= 0) {

            return 0;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            MONTH_REVENUE);

            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, year);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? resultSet.getDouble(1)
                    : 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi thống kê doanh thu tháng",
                    exception);

            return 0;

        } finally {

            closeResources();
        }
    }

    @Override
    public double getYearlyRevenue(int year) {

        if (year <= 0) {
            return 0;
        }

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(
                            YEAR_REVENUE);

            preparedStatement.setInt(1, year);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? resultSet.getDouble(1)
                    : 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi thống kê doanh thu năm",
                    exception);

            return 0;

        } finally {

            closeResources();
        }
    }

    /*
     * ==================================================
     * HELPER METHODS
     * ==================================================
     */

    private List<Ticket> executeTicketListQuery(
            String sql,
            Object... parameters) {

        List<Ticket> tickets = new ArrayList<>();

        try {

            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(sql);

            for (int index = 0;
                    index < parameters.length;
                    index++) {

                preparedStatement.setObject(
                        index + 1,
                        parameters[index]);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tickets.add(mapResultSet(resultSet));
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi truy vấn danh sách vé",
                    exception);

        } finally {

            closeResources();
        }

        return tickets;
    }

    private int executeCountQuery(String sql) {

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? resultSet.getInt(1)
                    : 0;

        } catch (SQLException exception) {

            logError("Lỗi đếm vé", exception);
            return 0;

        } finally {

            closeResources();
        }
    }

    private double executeDoubleQuery(String sql) {

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? resultSet.getDouble(1)
                    : 0;

        } catch (SQLException exception) {

            logError(
                    "Lỗi thống kê doanh thu",
                    exception);

            return 0;

        } finally {

            closeResources();
        }
    }

    private boolean executeUpdateById(
            String sql,
            Integer id) {

        try {

            connection = getConnection();

            preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException exception) {

            logError("Lỗi cập nhật vé", exception);
            return false;

        } finally {

            closeResources();
        }
    }

    private boolean isValidTicket(
            Ticket ticket,
            boolean requireId) {

        if (ticket == null) {
            return false;
        }

        if (requireId
                && ticket.getTicketId() <= 0) {

            return false;
        }

        if (normalize(
                ticket.getBookingCode()).isBlank()) {

            return false;
        }

        if (ticket.getUserId() <= 0
                || ticket.getShowtimeId() <= 0
                || ticket.getRoomId() <= 0
                || ticket.getPeopleCount() <= 0) {

            return false;
        }

        if (ticket.getTotalPrice() == null
                || ticket.getTotalPrice()
                        .compareTo(BigDecimal.ZERO) < 0) {

            return false;
        }

        return !normalizeStatus(
                ticket.getStatus()).isBlank();
    }

    private boolean isValidId(Integer id) {

        return id != null && id > 0;
    }

    private String getSafeSortColumn(String sort) {

        String normalizedSort =
                normalize(sort)
                        .toLowerCase(Locale.ROOT);

        switch (normalizedSort) {

            case "booking_code":
                return "t.booking_code";

            case "total_price":
                return "t.total_price";

            case "created_at":
                return "t.created_at";

            case "status":
                return "t.status";

            case "customer_name":
                return "u.full_name";

            case "movie_title":
                return "m.title";

            default:
                return "t.ticket_id";
        }
    }

    private String normalizeStatus(String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private void rollbackQuietly() {

        try {

            if (connection != null) {
                connection.rollback();
            }

        } catch (SQLException exception) {

            logError("Lỗi rollback", exception);
        }
    }

    private void restoreAutoCommitQuietly() {

        try {

            if (connection != null) {
                connection.setAutoCommit(true);
            }

        } catch (SQLException exception) {

            logError(
                    "Lỗi khôi phục auto-commit",
                    exception);
        }
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
