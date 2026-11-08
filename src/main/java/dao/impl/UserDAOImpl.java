package dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.BaseDAO;
import dao.UserDAO;
import model.User;

public class UserDAOImpl extends BaseDAO implements UserDAO {

    /*
     * ==========================
     * SQL QUERY
     * ==========================
     */

    private static final String FIND_ALL =
            "SELECT * FROM users ORDER BY user_id DESC";

    private static final String FIND_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String FIND_BY_PHONE =
            "SELECT * FROM users WHERE phone = ?";

    private static final String FIND_BY_ROLE =
            "SELECT * FROM users WHERE role = ? ORDER BY full_name";

    private static final String INSERT =
            "INSERT INTO users("
            + "username,"
            + "password_hash,"
            + "full_name,"
            + "email,"
            + "phone,"
            + "role,"
            + "status"
            + ") VALUES(?,?,?,?,?,?,?)";

    private static final String UPDATE =
            "UPDATE users SET "
            + "username=?,"
            + "password_hash=?,"
            + "full_name=?,"
            + "email=?,"
            + "phone=?,"
            + "role=?,"
            + "status=? "
            + "WHERE user_id=?";

    private static final String DELETE =
            "DELETE FROM users WHERE user_id=?";

    private static final String COUNT =
            "SELECT COUNT(*) FROM users";

    private static final String LOGIN =
            "SELECT * FROM users "
            + "WHERE username=? "
            + "AND password_hash=? "
            + "AND status='ACTIVE'";

    private static final String PAGINATION =
            "SELECT * FROM users "
            + "ORDER BY user_id DESC "
            + "OFFSET ? ROWS "
            + "FETCH NEXT ? ROWS ONLY";

    private static final String EXISTS_USERNAME =
            "SELECT COUNT(*) FROM users WHERE username = ?";
    
    private static final String EXISTS_EMAIL =
            "SELECT COUNT(*) " +
            "FROM users " +
            "WHERE LOWER(LTRIM(RTRIM(email))) = " +
            "LOWER(LTRIM(RTRIM(?)))";
    
    private static final String EXISTS_PHONE =
            "SELECT COUNT(*) FROM users WHERE phone = ?";
    
    private static final String SORT =
            "SELECT * FROM users ORDER BY %s %s";
    
    private static final String FIND_CUSTOMERS =
            "SELECT u.* " +
            "FROM users u " +
            "WHERE UPPER(LTRIM(RTRIM(u.role))) = 'CUSTOMER' " +
            "ORDER BY u.user_id DESC";
    
    private static final String SEARCH_CUSTOMERS =
            "SELECT u.* " +
            "FROM users u " +
            "WHERE UPPER(LTRIM(RTRIM(u.role))) = 'CUSTOMER' " +
            "AND (" +
            "u.username LIKE ? " +
            "OR u.full_name LIKE ? " +
            "OR u.email LIKE ? " +
            "OR u.phone LIKE ?" +
            ") " +
            "ORDER BY u.user_id DESC";
    
    private static final String SEARCH_CUSTOMERS_BY_STATUS =
            "SELECT u.* " +
            "FROM users u " +
            "WHERE UPPER(LTRIM(RTRIM(u.role))) = 'CUSTOMER' " +
            "AND (" +
            "u.username LIKE ? " +
            "OR u.full_name LIKE ? " +
            "OR u.email LIKE ? " +
            "OR u.phone LIKE ?" +
            ") " +
            "AND UPPER(LTRIM(RTRIM(u.status))) = " +
            "UPPER(LTRIM(RTRIM(?))) " +
            "ORDER BY u.user_id DESC";
    
    private static final String FIND_CUSTOMERS_BY_STATUS =
            "SELECT u.* " +
            "FROM users u " +
            "WHERE UPPER(LTRIM(RTRIM(u.role))) = 'CUSTOMER' " +
            "AND UPPER(LTRIM(RTRIM(u.status))) = " +
            "UPPER(LTRIM(RTRIM(?))) " +
            "ORDER BY u.user_id DESC";
    
    private static final String UPDATE_CUSTOMER =
            "UPDATE users " +
            "SET full_name=?, " +
            "email=?, " +
            "phone=? " +
            "WHERE user_id=? " +
            "AND UPPER(LTRIM(RTRIM(role)))='CUSTOMER'";
    
    private static final String EXISTS_EMAIL_EXCEPT_USER =
            "SELECT COUNT(*) " +
            "FROM users " +
            "WHERE LOWER(LTRIM(RTRIM(email))) = " +
            "LOWER(LTRIM(RTRIM(?))) " +
            "AND user_id <> ?";
    
    private static final String UPDATE_CUSTOMER_STATUS =
            "UPDATE users " +
            "SET status=? " +
            "WHERE user_id=? " +
            "AND UPPER(LTRIM(RTRIM(role)))='CUSTOMER'";
    
    /*
     * ==========================
     * MAPPING RESULTSET
     * ==========================
     */

    private User mapResultSet(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));

        return user;
    }

    @Override
    public List<User> findAll() {

        java.util.List<User> users = new java.util.ArrayList<>();

        try {

            connection = getConnection();
            preparedStatement = connection.prepareStatement(FIND_ALL);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(mapResultSet(resultSet));
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return users;
    }

    @Override
    public User findById(Integer id) {

        User user = null;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(FIND_BY_ID);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = mapResultSet(resultSet);
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return user;
    }

    @Override
    public boolean insert(User user) {

        boolean inserted = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(INSERT);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setString(7, user.getStatus());

            inserted = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return inserted;
    }

    @Override
    public boolean update(User user) {

        boolean updated = false;

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(UPDATE);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setString(7, user.getStatus());
            preparedStatement.setInt(8, user.getUserId());

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
    public List<User> search(String keyword) {

        List<User> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM users
            WHERE username LIKE ?
               OR full_name LIKE ?
               OR email LIKE ?
               OR phone LIKE ?
            ORDER BY user_id DESC
            """;

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User u = new User();

                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                u.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(u);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return list;
    }

    @Override
    public java.util.List<User> findByPage(int page, int pageSize) {

        java.util.List<User> users = new java.util.ArrayList<>();

        try {

            connection = getConnection();

            preparedStatement = connection.prepareStatement(PAGINATION);

            int offset = (page - 1) * pageSize;

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, pageSize);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                users.add(mapResultSet(resultSet));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            closeResources();

        }

        return users;
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
	public User login(String username, String passwordHash) {

	    User user = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(LOGIN);

	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, passwordHash);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            user = mapResultSet(resultSet);
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return user;
	}

	@Override
	public User findByUsername(String username) {

	    String sql = """
	            SELECT *
	            FROM users
	            WHERE username = ?
	            """;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(sql);

	        preparedStatement.setString(1, username);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {

	            User user = new User();

	            user.setUserId(resultSet.getInt("user_id"));
	            user.setUsername(resultSet.getString("username"));
	            user.setPasswordHash(resultSet.getString("password_hash"));
	            user.setFullName(resultSet.getString("full_name"));
	            user.setEmail(resultSet.getString("email"));
	            user.setPhone(resultSet.getString("phone"));
	            user.setRole(resultSet.getString("role"));
	            user.setStatus(resultSet.getString("status"));
	            user.setCreatedAt(resultSet.getTimestamp("created_at"));

	            return user;
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return null;
	}

	@Override
	public User findByEmail(String email) {

	    String sql =
	            "SELECT * FROM users WHERE email=?";

	    try {

	        Connection con =
	                getConnection();

	        PreparedStatement ps =
	                con.prepareStatement(sql);

	        ps.setString(1, email);

	        ResultSet rs =
	                ps.executeQuery();

	        if (rs.next()) {

	            User user =
	                    new User();

	            user.setUserId(
	                    rs.getInt("user_id"));

	            user.setUsername(
	                    rs.getString("username"));

	            user.setEmail(
	                    rs.getString("email"));

	            return user;
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return null;
	}

	@Override
	public User findByPhone(String phone) {

	    User user = null;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_PHONE);

	        preparedStatement.setString(1, phone);

	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            user = mapResultSet(resultSet);
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return user;
	}

	@Override
	public java.util.List<User> findByRole(String role) {

	    java.util.List<User> users = new java.util.ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(FIND_BY_ROLE);

	        preparedStatement.setString(1, role);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            users.add(mapResultSet(resultSet));

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return users;
	}

	@Override
	public boolean existsUsername(String username) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_USERNAME);

	        preparedStatement.setString(1, username);

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
	public boolean existsEmail(String email) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_EMAIL);

	        preparedStatement.setString(1, email);

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
	public boolean existsPhone(String phone) {

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement = connection.prepareStatement(EXISTS_PHONE);

	        preparedStatement.setString(1, phone);

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
	public boolean updatePassword(String email, String passwordHash) {

	    String sql =
	            "UPDATE users SET password_hash = ? WHERE email = ?";

	    try {

	        Connection conn = getConnection();

	        preparedStatement = conn.prepareStatement(sql);

	        preparedStatement.setString(1, passwordHash);
	        preparedStatement.setString(2, email);

	        return preparedStatement.executeUpdate() > 0;

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();

	    }

	    return false;
	}
	
	@Override
	public List<User> filter(String role, String status) {

	    List<User> list = new ArrayList<>();

	    StringBuilder sql = new StringBuilder(
	        "SELECT * FROM users WHERE 1=1 "
	    );

	    List<Object> params = new ArrayList<>();

	    if (role != null && !role.isBlank()) {
	        sql.append("AND role = ? ");
	        params.add(role);
	    }

	    if (status != null && !status.isBlank()) {
	        sql.append("AND status = ? ");
	        params.add(status);
	    }

	    sql.append("ORDER BY user_id DESC");

	    try (
	            Connection con = getConnection();
	            PreparedStatement ps =
	                    con.prepareStatement(sql.toString())) {

	        for (int i = 0; i < params.size(); i++) {
	            ps.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            User u = new User();

	            u.setUserId(rs.getInt("user_id"));
	            u.setUsername(rs.getString("username"));
	            u.setFullName(rs.getString("full_name"));
	            u.setEmail(rs.getString("email"));
	            u.setPhone(rs.getString("phone"));
	            u.setRole(rs.getString("role"));
	            u.setStatus(rs.getString("status"));
	            u.setCreatedAt(rs.getTimestamp("created_at"));

	            list.add(u);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
	
	@Override
	public List<User> sort(String sortBy, String order) {

	    List<User> list = new ArrayList<>();

	    List<String> allowColumns = Arrays.asList(
	            "user_id",
	            "username",
	            "full_name",
	            "email",
	            "role",
	            "status",
	            "created_at"
	    );

	    if (!allowColumns.contains(sortBy)) {
	        sortBy = "user_id";
	    }

	    if (!"DESC".equalsIgnoreCase(order)) {
	        order = "ASC";
	    }

	    String sql = String.format(SORT, sortBy, order);

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
	public List<User> findCustomers() {

	    List<User> customers = new ArrayList<>();

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(FIND_CUSTOMERS);

	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            customers.add(mapResultSet(resultSet));
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return customers;
	}
	
	@Override
	public List<User> searchCustomers(String keyword) {

	    List<User> customers = new ArrayList<>();

	    if (keyword == null || keyword.isBlank()) {
	        return findCustomers();
	    }

	    String searchValue =
	            "%" + keyword.trim() + "%";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        SEARCH_CUSTOMERS);

	        preparedStatement.setString(1, searchValue);
	        preparedStatement.setString(2, searchValue);
	        preparedStatement.setString(3, searchValue);
	        preparedStatement.setString(4, searchValue);

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            customers.add(
	                    mapResultSet(resultSet));
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return customers;
	}
	
	@Override
	public List<User> searchCustomers(
	        String keyword,
	        String status) {

	    if (keyword == null || keyword.isBlank()) {

	        if (status == null || status.isBlank()) {
	            return findCustomers();
	        }

	        return findCustomersByStatus(status);
	    }

	    if (status == null || status.isBlank()) {
	        return searchCustomers(keyword);
	    }

	    List<User> customers = new ArrayList<>();

	    String searchValue =
	            "%" + keyword.trim() + "%";

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        SEARCH_CUSTOMERS_BY_STATUS);

	        preparedStatement.setString(1, searchValue);
	        preparedStatement.setString(2, searchValue);
	        preparedStatement.setString(3, searchValue);
	        preparedStatement.setString(4, searchValue);
	        preparedStatement.setString(5, status.trim());

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            customers.add(
	                    mapResultSet(resultSet));
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return customers;
	}
	
	@Override
	public List<User> findCustomersByStatus(
	        String status) {

	    List<User> customers = new ArrayList<>();

	    if (status == null || status.isBlank()) {
	        return findCustomers();
	    }

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        FIND_CUSTOMERS_BY_STATUS);

	        preparedStatement.setString(
	                1,
	                status.trim());

	        resultSet =
	                preparedStatement.executeQuery();

	        while (resultSet.next()) {

	            customers.add(
	                    mapResultSet(resultSet));
	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return customers;
	}
	
	@Override
	public boolean updateCustomer(
	        Integer userId,
	        String fullName,
	        String email,
	        String phone) {

	    if (userId == null || userId <= 0) {
	        return false;
	    }

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        UPDATE_CUSTOMER);

	        preparedStatement.setString(1, fullName);
	        preparedStatement.setString(2, email);
	        preparedStatement.setString(3, phone);
	        preparedStatement.setInt(4, userId);

	        updated =
	                preparedStatement.executeUpdate() > 0;

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return updated;
	}
	
	@Override
	public boolean existsEmailExceptUser(
	        String email,
	        Integer userId) {

	    if (email == null
	            || email.isBlank()
	            || userId == null) {

	        return false;
	    }

	    boolean exists = false;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        EXISTS_EMAIL_EXCEPT_USER);

	        preparedStatement.setString(
	                1,
	                email.trim());

	        preparedStatement.setInt(
	                2,
	                userId);

	        resultSet =
	                preparedStatement.executeQuery();

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
	public boolean updateCustomerStatus(
	        Integer userId,
	        String status) {

	    if (userId == null
	            || userId <= 0
	            || status == null
	            || status.isBlank()) {

	        return false;
	    }

	    boolean updated = false;

	    try {

	        connection = getConnection();

	        preparedStatement =
	                connection.prepareStatement(
	                        UPDATE_CUSTOMER_STATUS);

	        preparedStatement.setString(
	                1,
	                status.trim().toUpperCase());

	        preparedStatement.setInt(
	                2,
	                userId);

	        int affectedRows =
	                preparedStatement.executeUpdate();

	        System.out.println(
	                "UPDATE CUSTOMER STATUS: userId="
	                        + userId
	                        + ", status="
	                        + status
	                        + ", affectedRows="
	                        + affectedRows);

	        updated = affectedRows > 0;

	    } catch (SQLException exception) {

	        System.err.println(
	                "Lỗi cập nhật trạng thái khách hàng: "
	                        + exception.getMessage());

	        exception.printStackTrace();

	    } finally {

	        closeResources();
	    }

	    return updated;
	}
	
}