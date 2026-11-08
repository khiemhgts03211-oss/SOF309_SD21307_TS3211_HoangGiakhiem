package dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.PaymentDAO;
import model.Payment;
import model.PaymentStatistic;

public class PaymentDAOImpl extends BaseDAO implements PaymentDAO{

	/*
	 * ==============================
	 * SQL QUERY
	 * ==============================
	 */

	private static final String FIND_ALL =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "m.title AS movie_title " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "ORDER BY p.payment_id DESC";

	private static final String FIND_BY_ID =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id = t.ticket_id " +
		    "JOIN users u ON t.user_id = u.user_id " +
		    "JOIN showtimes s ON t.showtime_id = s.showtime_id " +
		    "JOIN movies m ON s.movie_id = m.movie_id " +
		    "JOIN rooms r ON t.room_id = r.room_id " +
		    "WHERE p.payment_id = ?";

	private static final String INSERT =
	        "INSERT INTO payments("
	        + "ticket_id,"
	        + "transaction_code,"
	        + "payment_method,"
	        + "amount,"
	        + "status,"
	        + "paid_at"
	        + ") VALUES(?,?,?,?,?,?)";

	private static final String UPDATE =
	        "UPDATE payments SET "
	        + "ticket_id=?,"
	        + "transaction_code=?,"
	        + "payment_method=?,"
	        + "amount=?,"
	        + "status=?,"
	        + "paid_at=? "
	        + "WHERE payment_id=?";

	private static final String DELETE =
	        "DELETE FROM payments WHERE payment_id=?";

	private static final String COUNT =
	        "SELECT COUNT(*) FROM payments";

	private static final String SEARCH =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE " +
		    "p.transaction_code LIKE ? " +
		    "OR t.booking_code LIKE ? " +
		    "OR u.full_name LIKE ?";

	private static final String PAGINATION =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "ORDER BY p.payment_id DESC " +
		    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	/*
	 * ==============================
	 * BUSINESS QUERY
	 * ==============================
	 */

	private static final String FIND_BY_TICKET =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name, " +
	        "m.title AS movie_title, " +
	        "r.room_name, " +
	        "s.start_time " +
	        "FROM payments p " +
	        "JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "JOIN showtimes s " +
	        "ON t.showtime_id = s.showtime_id " +
	        "JOIN movies m " +
	        "ON s.movie_id = m.movie_id " +
	        "JOIN rooms r " +
	        "ON t.room_id = r.room_id " +
	        "WHERE p.ticket_id = ? " +
	        "ORDER BY p.payment_id DESC";

	private static final String FIND_BY_STATUS =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE p.status=? " +
		    "ORDER BY p.payment_id DESC";
	
	private static final String EXISTS_TRANSACTION_CODE =
	        "SELECT COUNT(*) "
	        + "FROM payments "
	        + "WHERE transaction_code=?";
	
	private static final String FIND_BY_TRANSACTION_CODE =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE p.transaction_code=?";

	private static final String FIND_BY_METHOD =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE p.payment_method=? " +
		    "ORDER BY p.payment_id DESC";

	private static final String FIND_BY_DATE =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE CAST(p.paid_at AS DATE)=? " +
		    "ORDER BY p.payment_id DESC";

	private static final String REVENUE_BY_DATE =
	        "SELECT ISNULL(SUM(amount),0) "
	        + "FROM payments "
	        + "WHERE status='PAID' "
	        + "AND CAST(paid_at AS DATE)=?";

	private static final String REVENUE_BY_MONTH =
	        "SELECT ISNULL(SUM(amount),0) "
	        + "FROM payments "
	        + "WHERE status='PAID' "
	        + "AND MONTH(paid_at)=? "
	        + "AND YEAR(paid_at)=?";

	private static final String REVENUE_BY_YEAR =
	        "SELECT ISNULL(SUM(amount),0) "
	        + "FROM payments "
	        + "WHERE status='PAID' "
	        + "AND YEAR(paid_at)=?";
	
	private static final String FILTER =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id " +
		    "WHERE (?='' OR p.status=?) " +
		    "AND (?='' OR p.payment_method=?) " +
		    "ORDER BY p.payment_id DESC";
	
	private static final String SORT =
		    "SELECT p.*, " +
		    "t.booking_code, " +
		    "u.full_name, " +
		    "m.title AS movie_title, " +
		    "r.room_name, " +
		    "s.start_time " +
		    "FROM payments p " +
		    "JOIN tickets t ON p.ticket_id=t.ticket_id " +
		    "JOIN users u ON t.user_id=u.user_id " +
		    "JOIN showtimes s ON t.showtime_id=s.showtime_id " +
		    "JOIN movies m ON s.movie_id=m.movie_id " +
		    "JOIN rooms r ON t.room_id=r.room_id ";
	
	private static final String UPDATE_STATUS =
	        "UPDATE payments "
	      + "SET status=? "
	      + "WHERE payment_id=?";
	
	private static final String COUNT_PAID =
	        "SELECT COUNT(*) FROM payments WHERE status='PAID'";

	private static final String COUNT_PENDING =
	        "SELECT COUNT(*) FROM payments WHERE status='PENDING'";

	private static final String TOTAL_REVENUE =
	        "SELECT ISNULL(SUM(amount),0) " +
	        "FROM payments WHERE status='PAID'";
	
	private static final String SEARCH_STAFF_PAYMENTS =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "m.title AS movie_title " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "WHERE " +
	        "UPPER(LTRIM(RTRIM(p.transaction_code))) COLLATE Latin1_General_CI_AI LIKE ? " +
	        "OR UPPER(LTRIM(RTRIM(t.booking_code))) LIKE ? " +
	        "OR UPPER(LTRIM(RTRIM(u.full_name))) LIKE ? " +
	        "OR UPPER(LTRIM(RTRIM(u.email))) LIKE ? " +
	        "OR UPPER(LTRIM(RTRIM(m.title))) LIKE ? " +
	        "ORDER BY p.payment_id DESC";
	
	private static final String SEARCH_FILTER_STAFF_PAYMENTS =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "m.title AS movie_title " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "WHERE " +
	        "(? = '' OR " +
	        " UPPER(LTRIM(RTRIM(p.transaction_code))) LIKE ? " +
	        " OR UPPER(LTRIM(RTRIM(t.booking_code))) COLLATE Latin1_General_CI_AI LIKE ? " +
	        " OR UPPER(LTRIM(RTRIM(u.full_name))) COLLATE Latin1_General_CI_AI LIKE ? " +
	        " OR UPPER(LTRIM(RTRIM(u.email))) COLLATE Latin1_General_CI_AI LIKE ? " +
	        " OR UPPER(LTRIM(RTRIM(m.title))) COLLATE Latin1_General_CI_AI LIKE ?) " +
	        "AND (? = '' OR " +
	        " UPPER(LTRIM(RTRIM(p.status))) = ?) " +
	        "AND (? = '' OR " +
	        " UPPER(LTRIM(RTRIM(p.payment_method))) = ?) " +
	        "ORDER BY p.payment_id DESC";
	
	private static final String FIND_STAFF_PAYMENT_DETAIL =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "t.total_price AS ticket_total_price, " +
	        "t.people_count, " +
	        "t.status AS ticket_status, " +
	        "u.user_id AS customer_id, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "u.phone AS customer_phone, " +
	        "m.title AS movie_title, " +
	        "r.room_name, " +
	        "sh.show_date, " +
	        "sh.start_time, " +
	        "sh.end_time " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "INNER JOIN rooms r " +
	        "ON t.room_id = r.room_id " +
	        "WHERE p.payment_id = ?";
	
	private static final String FIND_TODAY_PAYMENTS =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "m.title AS movie_title " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "WHERE CAST(p.paid_at AS DATE) = CAST(GETDATE() AS DATE) " +
	        "ORDER BY p.payment_id DESC";
	
	private static final String FIND_STAFF_PAYMENTS_BY_DATE_RANGE =
	        "SELECT " +
	        "p.*, " +
	        "t.booking_code, " +
	        "u.full_name AS customer_name, " +
	        "u.email AS customer_email, " +
	        "m.title AS movie_title " +
	        "FROM payments p " +
	        "INNER JOIN tickets t " +
	        "ON p.ticket_id = t.ticket_id " +
	        "INNER JOIN users u " +
	        "ON t.user_id = u.user_id " +
	        "INNER JOIN showtimes sh " +
	        "ON t.showtime_id = sh.showtime_id " +
	        "INNER JOIN movies m " +
	        "ON sh.movie_id = m.movie_id " +
	        "WHERE CAST(p.paid_at AS DATE) BETWEEN ? AND ? " +
	        "ORDER BY p.paid_at DESC, p.payment_id DESC";
	
	private static final String CALCULATE_PAID_REVENUE =
	        "SELECT COALESCE(SUM(amount), 0) AS total_revenue " +
	        "FROM payments " +
	        "WHERE status = 'PAID' " +
	        "AND CAST(paid_at AS DATE) BETWEEN ? AND ?";
	
	private static final String COUNT_BY_STATUS_AND_DATE_RANGE =
	        "SELECT COUNT(*) AS total " +
	        "FROM payments " +
	        "WHERE UPPER(LTRIM(RTRIM(status))) = ? " +
	        "AND CAST(paid_at AS DATE) BETWEEN ? AND ?";
	
	private static final String DAILY_PAYMENT_STATISTICS =
	        "SELECT " +
	        "CAST(paid_at AS DATE) AS statistic_date, " +
	        "COUNT(*) AS transaction_count, " +
	        "COALESCE(SUM(amount), 0) AS total_revenue " +
	        "FROM payments " +
	        "WHERE status = 'PAID' " +
	        "AND CAST(paid_at AS DATE) BETWEEN ? AND ? " +
	        "GROUP BY CAST(paid_at AS DATE) " +
	        "ORDER BY statistic_date ASC";
	
	private static final String PAYMENT_METHOD_STATISTICS =
	        "SELECT " +
	        "payment_method, " +
	        "COUNT(*) AS transaction_count, " +
	        "COALESCE(SUM(amount), 0) AS total_revenue " +
	        "FROM payments " +
	        "WHERE status = 'PAID' " +
	        "AND CAST(paid_at AS DATE) BETWEEN ? AND ? " +
	        "GROUP BY payment_method " +
	        "ORDER BY total_revenue DESC";
	
	/*
	 * ==============================
	 * RESULT SET MAPPING
	 * ==============================
	 */

	private Payment mapResultSet(ResultSet resultSet)
	        throws SQLException {

	    Payment payment = new Payment();

	    payment.setPaymentId(
	            resultSet.getInt("payment_id"));

	    payment.setTicketId(
	            resultSet.getInt("ticket_id"));

	    payment.setTransactionCode(
	            resultSet.getString("transaction_code"));

	    payment.setPaymentMethod(
	            resultSet.getString("payment_method"));

	    payment.setAmount(
	            resultSet.getBigDecimal("amount"));

	    payment.setStatus(
	            resultSet.getString("status"));

	    payment.setPaidAt(
	            resultSet.getTimestamp("paid_at"));

	    return payment;
	}
	
	private void mapStaffDisplayFields(
	        Payment payment,
	        ResultSet resultSet)
	        throws SQLException {

	    payment.setBookingCode(
	            resultSet.getString("booking_code"));

	    payment.setCustomerName(
	            resultSet.getString("customer_name"));

	    payment.setCustomerEmail(
	            resultSet.getString("customer_email"));

	    payment.setMovieTitle(
	            resultSet.getString("movie_title"));
	}
	
	@Override
	public List<Payment> findAll() {

	    List<Payment> payments =
	            new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_ALL);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment =
	                    mapResultSet(resultSet);

	            mapStaffDisplayFields(
	                    payment,
	                    resultSet);

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}

	@Override
	public Payment findById(Integer id) {

	    Payment payment = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ID);

	        preparedStatement.setInt(1, id);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            payment = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payment;
	}

	@Override
	public boolean insert(Payment payment) {

	    boolean inserted = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(INSERT);

	        preparedStatement.setInt(1, payment.getTicketId());
	        preparedStatement.setString(2, payment.getTransactionCode());
	        preparedStatement.setString(3, payment.getPaymentMethod());
	        preparedStatement.setBigDecimal(4, payment.getAmount());
	        preparedStatement.setString(5, payment.getStatus());
	        preparedStatement.setTimestamp(6, payment.getPaidAt());

	        inserted = preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return inserted;
	}

	@Override
	public boolean update(Payment payment) {

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(UPDATE);

	        preparedStatement.setInt(1, payment.getTicketId());
	        preparedStatement.setString(2, payment.getTransactionCode());
	        preparedStatement.setString(3, payment.getPaymentMethod());
	        preparedStatement.setBigDecimal(4, payment.getAmount());
	        preparedStatement.setString(5, payment.getStatus());
	        preparedStatement.setTimestamp(6, payment.getPaidAt());
	        preparedStatement.setInt(7, payment.getPaymentId());

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
	public List<Payment> search(String keyword) {

	    List<Payment> payments = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(SEARCH);

	        String searchKeyword = "%" + keyword + "%";

	        preparedStatement.setString(1, searchKeyword);
	        preparedStatement.setString(2, searchKeyword);
	        preparedStatement.setString(3, searchKeyword);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
	}

	@Override
	public List<Payment> findByPage(int page, int pageSize) {

	    List<Payment> payments = new ArrayList<>();

	    int offset = (page - 1) * pageSize;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(PAGINATION);

	        preparedStatement.setInt(1, offset);
	        preparedStatement.setInt(2, pageSize);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
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
	public List<Payment> findByTicket(
	        Integer ticketId) {

	    List<Payment> payments =
	            new java.util.ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_BY_TICKET);

	        preparedStatement.setInt(
	                1,
	                ticketId);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(
	                    mapResultSet(resultSet));
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}

	@Override
	public Payment findByTransactionCode(String transactionCode) {

	    Payment payment = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_TRANSACTION_CODE);

	        preparedStatement.setString(1, transactionCode);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            payment = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payment;
	}

	@Override
	public List<Payment> findByMethod(String paymentMethod) {

	    List<Payment> payments = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_METHOD);

	        preparedStatement.setString(1, paymentMethod);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
	}

	@Override
	public List<Payment> findByStatus(String status) {

	    List<Payment> payments = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_STATUS);

	        preparedStatement.setString(1, status);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
	}

	@Override
	public List<Payment> findByDate(Date paymentDate) {

	    List<Payment> payments = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_DATE);

	        preparedStatement.setDate(1, paymentDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
	}

	@Override
	public double getRevenueByDate(Date paymentDate) {

	    double revenue = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(REVENUE_BY_DATE);

	        preparedStatement.setDate(1, paymentDate);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            revenue = resultSet.getDouble(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return revenue;
	}

	@Override
	public double getRevenueByMonth(int month, int year) {

	    double revenue = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(REVENUE_BY_MONTH);

	        preparedStatement.setInt(1, month);
	        preparedStatement.setInt(2, year);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            revenue = resultSet.getDouble(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return revenue;
	}

	@Override
	public double getRevenueByYear(int year) {

	    double revenue = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(REVENUE_BY_YEAR);

	        preparedStatement.setInt(1, year);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            revenue = resultSet.getDouble(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return revenue;
	}
	
	@Override
	public boolean existsTransactionCode(String transactionCode) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_TRANSACTION_CODE);

	        preparedStatement.setString(1, transactionCode);

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
	public List<Payment> filter(String status, String method) {

	    List<Payment> payments = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FILTER);

	        preparedStatement.setString(1, status);
	        preparedStatement.setString(2, status);

	        preparedStatement.setString(3, method);
	        preparedStatement.setString(4, method);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            payments.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return payments;
	}
	
	@Override
	public List<Payment> sort(String sortBy, String order) {

	    List<Payment> payments = new ArrayList<>();

	    String normalizedSort =
	            sortBy == null
	                    ? ""
	                    : sortBy.trim().toLowerCase();

	    String sortColumn;
	    String sortOrder;

	    /*
	     * Hỗ trợ cả giá trị cũ của Service và
	     * giá trị mới đang được gửi từ list.jsp.
	     */
	    switch (normalizedSort) {

	        case "newest":
	            sortColumn = "p.paid_at";
	            sortOrder = "DESC";
	            break;

	        case "oldest":
	            sortColumn = "p.paid_at";
	            sortOrder = "ASC";
	            break;

	        case "amount_asc":
	        case "amountasc":
	            sortColumn = "p.amount";
	            sortOrder = "ASC";
	            break;

	        case "amount_desc":
	        case "amountdesc":
	            sortColumn = "p.amount";
	            sortOrder = "DESC";
	            break;

	        case "amount":
	            sortColumn = "p.amount";
	            sortOrder = normalizeOrder(order);
	            break;

	        case "paid_at":
	            sortColumn = "p.paid_at";
	            sortOrder = normalizeOrder(order);
	            break;

	        case "status":
	            sortColumn = "p.status";
	            sortOrder = normalizeOrder(order);
	            break;

	        case "payment_method":
	            sortColumn = "p.payment_method";
	            sortOrder = normalizeOrder(order);
	            break;

	        case "transaction_asc":
	            sortColumn = "p.transaction_code";
	            sortOrder = "ASC";
	            break;

	        case "transaction_desc":
	            sortColumn = "p.transaction_code";
	            sortOrder = "DESC";
	            break;

	        case "transaction_code":
	            sortColumn = "p.transaction_code";
	            sortOrder = normalizeOrder(order);
	            break;

	        default:
	            sortColumn = "p.payment_id";
	            sortOrder = "DESC";
	            break;
	    }

	    String sql =
	            SORT
	            + " ORDER BY "
	            + sortColumn
	            + " "
	            + sortOrder
	            + ", p.payment_id DESC";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(sql);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment =
	                    mapResultSet(resultSet);

	            /*
	             * SORT SELECT hiện trả về full_name nhưng
	             * không dùng alias customer_name.
	             */
	            payment.setBookingCode(
	                    resultSet.getString("booking_code"));

	            payment.setCustomerName(
	                    resultSet.getString("full_name"));

	            payment.setMovieTitle(
	                    resultSet.getString("movie_title"));

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi sắp xếp thanh toán: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}

	private String normalizeOrder(String order) {

	    return "ASC".equalsIgnoreCase(order)
	            ? "ASC"
	            : "DESC";
	}

	@Override
	public boolean updateStatus(
	        Integer paymentId,
	        String status) {

	    if (paymentId == null
	            || paymentId <= 0
	            || status == null
	            || status.isBlank()) {

	        return false;
	    }

	    String sql =
	            "UPDATE payments " +
	            "SET status = ?, " +
	            "paid_at = CASE " +
	            "    WHEN ? = 'PAID' THEN GETDATE() " +
	            "    ELSE NULL " +
	            "END " +
	            "WHERE payment_id = ? " +
	            "AND UPPER(LTRIM(RTRIM(status))) = 'PENDING'";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(sql);

	        String normalizedStatus =
	                status.trim().toUpperCase();

	        preparedStatement.setString(
	                1,
	                normalizedStatus);

	        preparedStatement.setString(
	                2,
	                normalizedStatus);

	        preparedStatement.setInt(
	                3,
	                paymentId);

	        return preparedStatement.executeUpdate() > 0;

	    } catch (java.sql.SQLException exception) {

	        exception.printStackTrace();

	        return false;

	    } finally {

	        closeResources();
	    }
	}
	
	@Override
	public long countPaid() {

	    long total = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(COUNT_PAID);

	        resultSet = preparedStatement.executeQuery();

	        if(resultSet.next()) {

	            total = resultSet.getLong(1);

	        }

	    } catch(SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return total;
	}
	
	@Override
	public long countPending() {

	    long total = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(COUNT_PENDING);

	        resultSet = preparedStatement.executeQuery();

	        if(resultSet.next()) {

	            total = resultSet.getLong(1);

	        }

	    } catch(SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return total;
	}
	
	@Override
	public double getTotalRevenue() {

	    double revenue = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(TOTAL_REVENUE);

	        resultSet = preparedStatement.executeQuery();

	        if(resultSet.next()) {

	            revenue = resultSet.getDouble(1);

	        }

	    } catch(SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return revenue;
	}
	
	@Override
	public List<Payment> findTodayPayments() {

	    List<Payment> payments =
	            new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_TODAY_PAYMENTS);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment =
	                    mapResultSet(resultSet);

	            mapStaffDisplayFields(
	                    payment,
	                    resultSet);

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}
	
	@Override
	public List<Payment> searchStaffPayments(
	        String keyword) {

	    if (keyword == null || keyword.isBlank()) {
	        return findAll();
	    }

	    List<Payment> payments =
	            new ArrayList<>();

	    String searchValue =
	            "%" + keyword.trim().toUpperCase() + "%";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        SEARCH_STAFF_PAYMENTS);

	        for (int i = 1; i <= 5; i++) {
	            preparedStatement.setString(
	                    i,
	                    searchValue);
	        }

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment =
	                    mapResultSet(resultSet);

	            mapStaffDisplayFields(
	                    payment,
	                    resultSet);

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}
	
	@Override
	public List<Payment> searchAndFilterStaffPayments(
	        String keyword,
	        String status,
	        String method) {

	    List<Payment> payments =
	            new ArrayList<>();

	    String normalizedKeyword =
	            keyword == null
	                    ? ""
	                    : keyword.trim().toUpperCase();

	    String normalizedStatus =
	            status == null
	                    ? ""
	                    : status.trim().toUpperCase();

	    String normalizedMethod =
	            method == null
	                    ? ""
	                    : method.trim().toUpperCase();

	    String searchValue =
	            "%" + normalizedKeyword + "%";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        SEARCH_FILTER_STAFF_PAYMENTS);

	        /*
	         * Từ khóa
	         */
	        preparedStatement.setString(
	                1,
	                normalizedKeyword);

	        preparedStatement.setString(
	                2,
	                searchValue);

	        preparedStatement.setString(
	                3,
	                searchValue);

	        preparedStatement.setString(
	                4,
	                searchValue);

	        preparedStatement.setString(
	                5,
	                searchValue);

	        preparedStatement.setString(
	                6,
	                searchValue);

	        /*
	         * Trạng thái
	         */
	        preparedStatement.setString(
	                7,
	                normalizedStatus);

	        preparedStatement.setString(
	                8,
	                normalizedStatus);

	        /*
	         * Phương thức
	         */
	        preparedStatement.setString(
	                9,
	                normalizedMethod);

	        preparedStatement.setString(
	                10,
	                normalizedMethod);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment =
	                    mapResultSet(resultSet);

	            mapStaffDisplayFields(
	                    payment,
	                    resultSet);

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi tìm kiếm và lọc thanh toán: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}
	
	@Override
	public Payment findStaffPaymentDetail(Integer paymentId) {

	    if (paymentId == null || paymentId <= 0) {
	        return null;
	    }

	    Payment payment = null;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_STAFF_PAYMENT_DETAIL);

	        preparedStatement.setInt(1, paymentId);

	        resultSet =
	                preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            payment = mapResultSet(resultSet);

	            payment.setBookingCode(
	                    resultSet.getString(
	                            "booking_code"));

	            payment.setCustomerId(
	                    resultSet.getInt(
	                            "customer_id"));

	            payment.setCustomerName(
	                    resultSet.getString(
	                            "customer_name"));

	            payment.setCustomerEmail(
	                    resultSet.getString(
	                            "customer_email"));

	            payment.setCustomerPhone(
	                    resultSet.getString(
	                            "customer_phone"));

	            payment.setMovieTitle(
	                    resultSet.getString(
	                            "movie_title"));

	            payment.setRoomName(
	                    resultSet.getString(
	                            "room_name"));

	            payment.setShowDate(
	                    resultSet.getDate(
	                            "show_date"));

	            payment.setStartTime(
	                    resultSet.getTime(
	                            "start_time"));

	            payment.setEndTime(
	                    resultSet.getTime(
	                            "end_time"));

	            payment.setTicketTotalPrice(
	                    resultSet.getBigDecimal(
	                            "ticket_total_price"));

	            payment.setPeopleCount(
	                    resultSet.getInt(
	                            "people_count"));

	            payment.setTicketStatus(
	                    resultSet.getString(
	                            "ticket_status"));
	        }

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi lấy chi tiết thanh toán: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payment;
	}
	
	@Override
	public List<Payment> findStaffPaymentsByDateRange(
	        java.sql.Date fromDate,
	        java.sql.Date toDate) {

	    List<Payment> payments = new ArrayList<>();

	    if (fromDate == null || toDate == null) {
	        return payments;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_STAFF_PAYMENTS_BY_DATE_RANGE);

	        preparedStatement.setDate(1, fromDate);
	        preparedStatement.setDate(2, toDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Payment payment = mapResultSet(resultSet);

	            mapStaffDisplayFields(
	                    payment,
	                    resultSet);

	            payments.add(payment);
	        }

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi lấy thanh toán theo khoảng ngày: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return payments;
	}
	
	@Override
	public BigDecimal calculatePaidRevenue(
	        java.sql.Date fromDate,
	        java.sql.Date toDate) {

	    if (fromDate == null || toDate == null) {
	        return BigDecimal.ZERO;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        CALCULATE_PAID_REVENUE);

	        preparedStatement.setDate(1, fromDate);
	        preparedStatement.setDate(2, toDate);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            BigDecimal revenue =
	                    resultSet.getBigDecimal("total_revenue");

	            return revenue == null
	                    ? BigDecimal.ZERO
	                    : revenue;
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return BigDecimal.ZERO;
	}
	
	@Override
	public int countPaymentsByStatusAndDateRange(
	        String status,
	        java.sql.Date fromDate,
	        java.sql.Date toDate) {

	    if (status == null
	            || status.isBlank()
	            || fromDate == null
	            || toDate == null) {

	        return 0;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        COUNT_BY_STATUS_AND_DATE_RANGE);

	        preparedStatement.setString(
	                1,
	                status.trim().toUpperCase());

	        preparedStatement.setDate(2, fromDate);
	        preparedStatement.setDate(3, toDate);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("total");
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return 0;
	}
	
	@Override
	public List<PaymentStatistic> getDailyPaymentStatistics(
	        java.sql.Date fromDate,
	        java.sql.Date toDate) {

	    List<PaymentStatistic> statistics =
	            new ArrayList<>();

	    if (fromDate == null || toDate == null) {
	        return statistics;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        DAILY_PAYMENT_STATISTICS);

	        preparedStatement.setDate(1, fromDate);
	        preparedStatement.setDate(2, toDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            PaymentStatistic statistic =
	                    new PaymentStatistic();

	            statistic.setStatisticDate(
	                    resultSet.getDate(
	                            "statistic_date"));

	            statistic.setTransactionCount(
	                    resultSet.getInt(
	                            "transaction_count"));

	            statistic.setTotalRevenue(
	                    resultSet.getBigDecimal(
	                            "total_revenue"));

	            statistics.add(statistic);
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return statistics;
	}
	
	@Override
	public List<PaymentStatistic> getPaymentMethodStatistics(
	        java.sql.Date fromDate,
	        java.sql.Date toDate) {

	    List<PaymentStatistic> statistics =
	            new ArrayList<>();

	    if (fromDate == null || toDate == null) {
	        return statistics;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        PAYMENT_METHOD_STATISTICS);

	        preparedStatement.setDate(1, fromDate);
	        preparedStatement.setDate(2, toDate);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            PaymentStatistic statistic =
	                    new PaymentStatistic();

	            statistic.setPaymentMethod(
	                    resultSet.getString(
	                            "payment_method"));

	            statistic.setTransactionCount(
	                    resultSet.getInt(
	                            "transaction_count"));

	            statistic.setTotalRevenue(
	                    resultSet.getBigDecimal(
	                            "total_revenue"));

	            statistics.add(statistic);
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return statistics;
	}
	
}
