package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.ServiceDAO;
import model.Service;

public class ServiceDAOImpl extends BaseDAO implements ServiceDAO {

	/*
	 * ==============================
	 * SQL QUERY
	 * ==============================
	 */

	private static final String FIND_ALL =
	        "SELECT * FROM services ORDER BY service_id DESC";

	private static final String FIND_BY_ID =
	        "SELECT * FROM services WHERE service_id=?";

	private static final String INSERT =
	        "INSERT INTO services("
	        + "category_id,"
	        + "service_name,"
	        + "price,"
	        + "status"
	        + ") VALUES(?,?,?,?)";

	private static final String UPDATE =
	        "UPDATE services SET "
	        + "category_id=?,"
	        + "service_name=?,"
	        + "price=?,"
	        + "status=? "
	        + "WHERE service_id=?";

	private static final String DELETE =
	        "DELETE FROM services WHERE service_id=?";

	private static final String COUNT =
	        "SELECT COUNT(*) FROM services";

	private static final String SEARCH =
	        "SELECT * FROM services "
	        + "WHERE service_name LIKE ? "
	        + "ORDER BY service_id DESC";

	private static final String PAGINATION =
	        "SELECT * FROM services "
	        + "ORDER BY service_id DESC "
	        + "OFFSET ? ROWS "
	        + "FETCH NEXT ? ROWS ONLY";

	/*
	 * ==============================
	 * BUSINESS QUERY
	 * ==============================
	 */

	
	private static final String FIND_BY_CATEGORY =
	        "SELECT * FROM services "
	        + "WHERE category_id=? "
	        + "ORDER BY service_name";

	private static final String FIND_BY_STATUS =
	        "SELECT * FROM services "
	        + "WHERE status=? "
	        + "ORDER BY service_name";

	private static final String FIND_AVAILABLE =
	        "SELECT * FROM services "
	        + "WHERE status='ACTIVE' "
	        + "ORDER BY service_name";

	private static final String EXISTS_SERVICE =
	        "SELECT COUNT(*) "
	        + "FROM services "
	        + "WHERE service_name=?";
	
	private static final String EXISTS_BY_CATEGORY =
	        "SELECT COUNT(*) FROM services WHERE category_id=?";
	
	private static final String FIND_ALL_ACTIVE =
	        "SELECT * " +
	        "FROM services " +
	        "WHERE status='ACTIVE' " +
	        "ORDER BY service_name";
	
	/*
	 * ==============================
	 * RESULT SET MAPPING
	 * ==============================
	 */

	private Service mapResultSet(ResultSet rs) throws SQLException {

	    Service service = new Service();

	    service.setServiceId(rs.getInt("service_id"));
	    service.setCategoryId(rs.getInt("category_id"));
	    service.setServiceName(rs.getString("service_name"));
	    service.setPrice(rs.getBigDecimal("price"));
	    service.setStatus(rs.getString("status"));
	    service.setCreatedAt(rs.getTimestamp("created_at"));

	    return service;
	}
	
	@Override
	public List<Service> findAll() {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_ALL);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
	}

	@Override
	public Service findById(Integer id) {

	    Service service = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ID);

	        preparedStatement.setInt(1, id);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            service = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return service;
	}

	@Override
	public boolean insert(Service service) {

	    boolean inserted = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(INSERT);

	        preparedStatement.setInt(1, service.getCategoryId());
	        preparedStatement.setString(2, service.getServiceName());
	        preparedStatement.setBigDecimal(3, service.getPrice());
	        preparedStatement.setString(4, service.getStatus());

	        inserted = preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return inserted;
	}

	@Override
	public boolean update(Service service) {

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(UPDATE);

	        preparedStatement.setInt(1, service.getCategoryId());
	        preparedStatement.setString(2, service.getServiceName());
	        preparedStatement.setBigDecimal(3, service.getPrice());
	        preparedStatement.setString(4, service.getStatus());
	        preparedStatement.setInt(5, service.getServiceId());

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
	public List<Service> search(String keyword) {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(SEARCH);

	        String searchKeyword = "%" + keyword + "%";

	        preparedStatement.setString(1, searchKeyword);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
	}

	@Override
	public List<Service> findByPage(int page, int pageSize) {

	    List<Service> services = new ArrayList<>();

	    int offset = (page - 1) * pageSize;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(PAGINATION);

	        preparedStatement.setInt(1, offset);
	        preparedStatement.setInt(2, pageSize);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
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
	public List<Service> findByCategory(Integer categoryId) {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_CATEGORY);

	        preparedStatement.setInt(1, categoryId);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
	}

	@Override
	public List<Service> findByStatus(String status) {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_STATUS);

	        preparedStatement.setString(1, status);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
	}

	@Override
	public boolean existsService(String serviceName) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_SERVICE);

	        preparedStatement.setString(1, serviceName);

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
	public List<Service> findAvailableServices() {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_AVAILABLE);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;
	}
	
	@Override
	public boolean existsByCategory(Integer categoryId) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(EXISTS_BY_CATEGORY);

	        preparedStatement.setInt(1, categoryId);

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
	public List<Service> filter(Integer categoryId, String status) {

	    List<Service> list = new ArrayList<>();

	    StringBuilder sql = new StringBuilder();

	    sql.append("SELECT s.*, c.category_name ");
	    sql.append("FROM services s ");
	    sql.append("INNER JOIN service_categories c ");
	    sql.append("ON s.category_id = c.category_id ");
	    sql.append("WHERE 1=1 ");

	    if (categoryId != null) {
	        sql.append("AND s.category_id = ? ");
	    }

	    if (status != null && !status.isBlank()) {
	        sql.append("AND s.status = ? ");
	    }

	    sql.append("ORDER BY s.service_id DESC");

	    try {

	        getConnection();

	        preparedStatement =
	                connection.prepareStatement(sql.toString());

	        int index = 1;

	        if (categoryId != null) {
	            preparedStatement.setInt(index++, categoryId);
	        }

	        if (status != null && !status.isBlank()) {
	            preparedStatement.setString(index++, status);
	        }

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Service service = new Service();

	            service.setServiceId(resultSet.getInt("service_id"));
	            service.setCategoryId(resultSet.getInt("category_id"));
	            service.setCategoryName(resultSet.getString("category_name"));
	            service.setServiceName(resultSet.getString("service_name"));
	            service.setPrice(resultSet.getBigDecimal("price"));
	            service.setStatus(resultSet.getString("status"));
	            service.setCreatedAt(resultSet.getTimestamp("created_at"));

	            list.add(service);

	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return list;

	}
	
	@Override
	public List<Service> sort(String sortBy, String order) {

	    List<Service> list = new ArrayList<>();

	    if (sortBy == null || sortBy.isEmpty()) {
	        sortBy = "service_id";
	    }

	    if (!order.equalsIgnoreCase("ASC")
	            && !order.equalsIgnoreCase("DESC")) {
	        order = "ASC";
	    }

	    String sql =
	            "SELECT s.*, c.category_name " +
	            "FROM services s " +
	            "INNER JOIN service_categories c " +
	            "ON s.category_id = c.category_id " +
	            "ORDER BY " + sortBy + " " + order;

	    try {

	        getConnection();

	        preparedStatement =
	                connection.prepareStatement(sql);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            Service service = new Service();

	            service.setServiceId(resultSet.getInt("service_id"));
	            service.setCategoryId(resultSet.getInt("category_id"));
	            service.setCategoryName(resultSet.getString("category_name"));
	            service.setServiceName(resultSet.getString("service_name"));
	            service.setPrice(resultSet.getBigDecimal("price"));
	            service.setStatus(resultSet.getString("status"));
	            service.setCreatedAt(resultSet.getTimestamp("created_at"));

	            list.add(service);

	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return list;

	}
	
	@Override
	public List<Service> findAllActive() {

	    List<Service> services = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_ALL_ACTIVE);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            services.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return services;

	}
	
}
