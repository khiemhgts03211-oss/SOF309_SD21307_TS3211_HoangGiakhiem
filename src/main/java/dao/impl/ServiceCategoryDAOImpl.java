package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;
import dao.ServiceCategoryDAO;
import model.ServiceCategory;

public class ServiceCategoryDAOImpl extends BaseDAO implements ServiceCategoryDAO{

	/*
	 * ==============================
	 * SQL QUERY
	 * ==============================
	 */

	private static final String FIND_ALL =
	        "SELECT * FROM service_categories ORDER BY category_id DESC";

	private static final String FIND_BY_ID =
	        "SELECT * FROM service_categories WHERE category_id=?";

	private static final String INSERT =
	        "INSERT INTO service_categories("
	        + "category_name,"
	        + "status"
	        + ") VALUES(?,?)";

	private static final String UPDATE =
	        "UPDATE service_categories SET "
	        + "category_name=?,"
	        + "status=? "
	        + "WHERE category_id=?";

	private static final String DELETE =
	        "DELETE FROM service_categories WHERE category_id=?";

	private static final String COUNT =
	        "SELECT COUNT(*) FROM service_categories";

	private static final String SEARCH =
	        "SELECT * FROM service_categories "
	        + "WHERE category_name LIKE ? "
	        + "ORDER BY category_id DESC";

	private static final String PAGINATION =
	        "SELECT * FROM service_categories "
	        + "ORDER BY category_id DESC "
	        + "OFFSET ? ROWS "
	        + "FETCH NEXT ? ROWS ONLY";

	/*
	 * ==============================
	 * BUSINESS QUERY
	 * ==============================
	 */

	private static final String FIND_BY_STATUS =
	        "SELECT * FROM service_categories "
	        + "WHERE status=? "
	        + "ORDER BY category_name";

	private static final String FIND_AVAILABLE =
	        "SELECT * FROM service_categories "
	        + "WHERE status='ACTIVE' "
	        + "ORDER BY category_name";

	private static final String EXISTS_CATEGORY =
	        "SELECT COUNT(*) "
	        + "FROM service_categories "
	        + "WHERE category_name=?";
	
	private static final String SORT_ID_ASC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY category_id ASC";

	private static final String SORT_ID_DESC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY category_id DESC";

	private static final String SORT_NAME_ASC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY category_name ASC";

	private static final String SORT_NAME_DESC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY category_name DESC";
	
	private static final String SORT_CREATED_ASC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY created_at ASC";

	private static final String SORT_CREATED_DESC =
	        "SELECT * FROM service_categories "
	        + "ORDER BY created_at DESC";
	
	/*
	 * ==============================
	 * RESULT SET MAPPING
	 * ==============================
	 */

	private ServiceCategory mapResultSet(ResultSet rs) throws SQLException {

	    ServiceCategory category = new ServiceCategory();

	    category.setCategoryId(rs.getInt("category_id"));
	    category.setCategoryName(rs.getString("category_name"));
	    category.setStatus(rs.getString("status"));
	    category.setCreatedAt(rs.getTimestamp("created_at"));

	    return category;
	}
	
	@Override
	public List<ServiceCategory> findAll() {

	    List<ServiceCategory> categories = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_ALL);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return categories;
	}

	@Override
	public ServiceCategory findById(Integer id) {

	    ServiceCategory category = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ID);

	        preparedStatement.setInt(1, id);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            category = mapResultSet(resultSet);

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return category;
	}

	@Override
	public boolean insert(ServiceCategory category) {

	    boolean inserted = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(INSERT);

	        preparedStatement.setString(1, category.getCategoryName());
	        preparedStatement.setString(2, category.getStatus());

	        inserted = preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return inserted;
	}

	@Override
	public boolean update(ServiceCategory category) {

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(UPDATE);

	        preparedStatement.setString(1, category.getCategoryName());
	        preparedStatement.setString(2, category.getStatus());
	        preparedStatement.setInt(3, category.getCategoryId());

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
	public List<ServiceCategory> search(String keyword) {

	    List<ServiceCategory> categories = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(SEARCH);

	        String searchKeyword = "%" + keyword + "%";

	        preparedStatement.setString(1, searchKeyword);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return categories;
	}

	@Override
	public List<ServiceCategory> findByPage(int page, int pageSize) {

	    List<ServiceCategory> categories = new ArrayList<>();

	    int offset = (page - 1) * pageSize;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(PAGINATION);

	        preparedStatement.setInt(1, offset);
	        preparedStatement.setInt(2, pageSize);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return categories;
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
	public List<ServiceCategory> findByStatus(String status) {

	    List<ServiceCategory> categories = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_STATUS);

	        preparedStatement.setString(1, status);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return categories;
	}

	@Override
	public boolean existsCategory(String categoryName) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_CATEGORY);

	        preparedStatement.setString(1, categoryName);

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
	public List<ServiceCategory> findAvailableCategories() {

	    List<ServiceCategory> categories = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_AVAILABLE);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return categories;
	}
	
	@Override
	public List<ServiceCategory> sort(String sortBy) {

	    List<ServiceCategory> categories =
	            new ArrayList<>();

	    String normalizedSort =
	            sortBy == null
	                    ? ""
	                    : sortBy.trim();

	    String sql;

	    switch (normalizedSort) {

	        case "idAsc":
	            sql = SORT_ID_ASC;
	            break;

	        case "idDesc":
	            sql = SORT_ID_DESC;
	            break;

	        case "name":
	        case "nameAsc":
	            sql = SORT_NAME_ASC;
	            break;

	        case "nameDesc":
	            sql = SORT_NAME_DESC;
	            break;

	        case "created_at":
	        case "createdAsc":
	            sql = SORT_CREATED_ASC;
	            break;

	        case "createdDesc":
	            sql = SORT_CREATED_DESC;
	            break;

	        default:
	            sql = SORT_ID_DESC;
	            break;
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(sql);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            categories.add(
	                    mapResultSet(resultSet));
	        }

	    } catch (SQLException exception) {

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return categories;
	}
	
}
