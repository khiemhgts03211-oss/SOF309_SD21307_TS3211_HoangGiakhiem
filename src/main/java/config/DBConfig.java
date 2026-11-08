package config;

public class DBConfig {

    public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=MovieTicketManagement;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static final String USERNAME = "sa";

    public static final String PASSWORD = System.getenv().getOrDefault("MOVIE_DB_PASSWORD", "");

    private DBConfig() {
    }
}
