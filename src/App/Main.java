package App;

import Model.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.connect();
            System.out.println("Verbonden met MySQL!");
        } catch (SQLException e) {
            System.out.println("❌ Fout bij verbinding: " + e.getMessage());
        }
    }
}
