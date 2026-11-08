package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.TicketDetailDAO;
import model.Service;
import model.TicketDetail;

public class TicketDetailDAOImpl extends BaseDAO implements TicketDetailDAO {
	
	/*
	 * ==============================
	 * SQL QUERY
	 * ==============================
	 */

	private static final String FIND_ALL =
	        "SELECT * FROM ticket_details ORDER BY detail_id DESC";

	private static final String FIND_BY_ID =
	        "SELECT * FROM ticket_details WHERE detail_id = ?";

	private static final String INSERT =
	        "INSERT INTO ticket_details("
	        + "ticket_id,"
	        + "service_id,"
	        + "quantity,"
	        + "amount"
	        + ") VALUES(?,?,?,?)";

	private static final String UPDATE =
	        "UPDATE ticket_details SET "
	        + "ticket_id=?,"
	        + "service_id=?,"
	        + "quantity=?,"
	        + "amount=? "
	        + "WHERE detail_id=?";

	private static final String DELETE =
	        "DELETE FROM ticket_details WHERE detail_id=?";

	private static final String COUNT =
	        "SELECT COUNT(*) FROM ticket_details";

	private static final String SEARCH =
	        "SELECT * FROM ticket_details "
	        + "WHERE ticket_id LIKE ? "
	        + "OR service_id LIKE ? "
	        + "ORDER BY detail_id DESC";

	private static final String PAGINATION =
	        "SELECT * FROM ticket_details "
	        + "ORDER BY detail_id DESC "
	        + "OFFSET ? ROWS "
	        + "FETCH NEXT ? ROWS ONLY";

	private static final String FIND_BY_TICKET =
	        "SELECT td.*, " +
	        "s.service_name, " +
	        "s.price AS service_price " +
	        "FROM ticket_details td " +
	        "JOIN services s ON td.service_id = s.service_id " +
	        "WHERE td.ticket_id = ? " +
	        "ORDER BY td.detail_id DESC";

	private static final String FIND_BY_SERVICE =
	        "SELECT * FROM ticket_details "
	        + "WHERE service_id=? "
	        + "ORDER BY detail_id DESC";

	private static final String FIND_BY_TICKET_AND_SERVICE =
	        "SELECT * FROM ticket_details "
	        + "WHERE ticket_id=? "
	        + "AND service_id=?";

	private static final String EXISTS_DETAIL =
	        "SELECT COUNT(*) "
	        + "FROM ticket_details "
	        + "WHERE ticket_id=? "
	        + "AND service_id=?";

	private static final String DELETE_BY_TICKET =
	        "DELETE FROM ticket_details WHERE ticket_id=?";

	private static final String TOTAL_QUANTITY =
	        "SELECT ISNULL(SUM(quantity),0) "
	      + "FROM ticket_details "
	      + "WHERE ticket_id=?";

	private static final String SERVICE_TOTAL =
	        "SELECT ISNULL(SUM(amount), 0) " +
	        "FROM ticket_details " +
	        "WHERE ticket_id = ?";

	private static final String TODAY_SERVICE_QUANTITY =
	        "SELECT ISNULL(SUM(quantity), 0) " +
	        "FROM ticket_details " +
	        "WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";

	private static final String TODAY_SERVICE_REVENUE =
	        "SELECT ISNULL(SUM(amount), 0) " +
	        "FROM ticket_details " +
	        "WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";
	
	/*
	 * ==============================
	 * RESULT SET MAPPING
	 * ==============================
	 */

	private TicketDetail mapResultSet(ResultSet rs)
	        throws SQLException {

	    TicketDetail detail = new TicketDetail();

	    detail.setDetailId(rs.getInt("detail_id"));
	    detail.setTicketId(rs.getInt("ticket_id"));
	    detail.setServiceId(rs.getInt("service_id"));
	    detail.setQuantity(rs.getInt("quantity"));
	    detail.setAmount(rs.getBigDecimal("amount"));
	    detail.setCreatedAt(rs.getTimestamp("created_at"));

	    Service service = new Service();

	    service.setServiceId(rs.getInt("service_id"));
	    service.setServiceName(rs.getString("service_name"));
	    service.setPrice(rs.getBigDecimal("service_price"));

	    detail.setService(service);

	    return detail;
	}
	
	@Override
	public List<TicketDetail> findAll() {

	    List<TicketDetail> ticketDetails = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_ALL);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            ticketDetails.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetails;
	}

	@Override
	public TicketDetail findById(Integer id) {

	    TicketDetail ticketDetail = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ID);

	        preparedStatement.setInt(1, id);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            ticketDetail = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetail;
	}

	@Override
	public boolean insert(TicketDetail ticketDetail) {

	    boolean inserted = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(INSERT);

	        preparedStatement.setInt(1, ticketDetail.getTicketId());
	        preparedStatement.setInt(2, ticketDetail.getServiceId());
	        preparedStatement.setInt(3, ticketDetail.getQuantity());
	        preparedStatement.setBigDecimal(4, ticketDetail.getAmount());

	        inserted = preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return inserted;
	}

	@Override
	public boolean update(TicketDetail ticketDetail) {

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(UPDATE);

	        preparedStatement.setInt(1, ticketDetail.getTicketId());
	        preparedStatement.setInt(2, ticketDetail.getServiceId());
	        preparedStatement.setInt(3, ticketDetail.getQuantity());
	        preparedStatement.setBigDecimal(4, ticketDetail.getAmount());
	        preparedStatement.setInt(5, ticketDetail.getDetailId());

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
	public List<TicketDetail> search(String keyword) {

	    List<TicketDetail> ticketDetails = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(SEARCH);

	        String searchKeyword = "%" + keyword + "%";

	        preparedStatement.setString(1, searchKeyword);
	        preparedStatement.setString(2, searchKeyword);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            ticketDetails.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetails;
	}

	@Override
	public List<TicketDetail> findByPage(int page, int pageSize) {

	    List<TicketDetail> ticketDetails = new ArrayList<>();

	    int offset = (page - 1) * pageSize;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(PAGINATION);

	        preparedStatement.setInt(1, offset);
	        preparedStatement.setInt(2, pageSize);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            ticketDetails.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetails;
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
	public List<TicketDetail> findByTicket(Integer ticketId) {

	    List<TicketDetail> details = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_BY_TICKET);

	        preparedStatement.setInt(1, ticketId);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            details.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return details;
	}

	@Override
	public List<TicketDetail> findByService(Integer serviceId) {

	    List<TicketDetail> ticketDetails = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_SERVICE);

	        preparedStatement.setInt(1, serviceId);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            ticketDetails.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetails;
	}

	@Override
	public boolean deleteByTicket(Integer ticketId) {

	    boolean deleted = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(DELETE_BY_TICKET);

	        preparedStatement.setInt(1, ticketId);

	        deleted = preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return deleted;
	}

	@Override
	public int getTotalQuantity(Integer ticketId) {

	    int totalQuantity = 0;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(TOTAL_QUANTITY);

	        preparedStatement.setInt(1, ticketId);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            totalQuantity = resultSet.getInt(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return totalQuantity;
	}
	
	@Override
	public TicketDetail findByTicketAndService(Integer ticketId, Integer serviceId) {

	    TicketDetail ticketDetail = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_TICKET_AND_SERVICE);

	        preparedStatement.setInt(1, ticketId);
	        preparedStatement.setInt(2, serviceId);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            ticketDetail = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return ticketDetail;
	}
	
	@Override
	public boolean existsDetail(Integer ticketId, Integer serviceId) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_DETAIL);

	        preparedStatement.setInt(1, ticketId);
	        preparedStatement.setInt(2, serviceId);

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
	public double getServiceTotal(Integer ticketId) {

	    double total = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(SERVICE_TOTAL);

	        preparedStatement.setInt(1, ticketId);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            total = resultSet.getDouble(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return total;
	}
	
	@Override
	public int getTodayServiceQuantity() {

	    int quantity = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        TODAY_SERVICE_QUANTITY);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            quantity = resultSet.getInt(1);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return quantity;
	}
	
	@Override
	public double getTodayServiceRevenue() {

	    double revenue = 0;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        TODAY_SERVICE_REVENUE);

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
	
}
