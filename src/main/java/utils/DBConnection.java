package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.DBConfig;

public class DBConnection {

    static {
        try {
            Class.forName(DBConfig.DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy SQL Server JDBC Driver", e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        System.out.println("URL = " + DBConfig.URL);
        System.out.println("USER = " + DBConfig.USERNAME);

        return DriverManager.getConnection(
                DBConfig.URL,
                DBConfig.USERNAME,
                DBConfig.PASSWORD
        );
    }
}