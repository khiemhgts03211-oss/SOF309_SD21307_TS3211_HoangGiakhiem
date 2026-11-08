package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import utils.DBConnection;

public abstract class BaseDAO {

    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    /**
     * Mở kết nối Database
     */
    protected Connection getConnection() throws SQLException {
        connection = DBConnection.getConnection();
        return connection;
    }

    /**
     * Đóng ResultSet
     */
    protected void closeResultSet() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng PreparedStatement
     */
    protected void closeStatement() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng Connection
     */
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng toàn bộ tài nguyên
     */
    protected void closeResources() {
        closeResultSet();
        closeStatement();
        closeConnection();
    }
}