package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/scrum_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Verbonden met database: " + conn.getCatalog());  // Debug
        return conn;
    }
}
