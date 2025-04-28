package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//deze kalsse regelt het registreren en inlogen van de gebruikers
//heeft verbinding met de databse
//beheert het aanmaken en aanmelden van gebruikers via de database

public class Authenticator {

    //een gebruiker opslaan in de database  via insert
    public void register(String naam, String rol) throws SQLException {
        String query = "INSERT INTO Model.Gebruiker (Naam, Rol) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, naam);
            stmt.setString(2, rol);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int gebruikerID = generatedKeys.getInt(1);
                System.out.println("Je gebruikersID is: " + gebruikerID);
            }

        }

    }
    //controleren of de gebruiker bestaat in de database via select
    public Gebruiker login(String naam, int gebruikerID) throws SQLException {
        String query = "SELECT * FROM Model.Gebruiker WHERE Naam = ? AND GebruikerID = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, naam);
            stmt.setInt(2, gebruikerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Gebruiker(
                        rs.getInt("GebruikerID"),
                        rs.getString("Naam"),
                        rs.getString("Rol")
                );
            }
        }
        return null; //een volledig Gebruiker objec teruggeven als iemand inlogt
    }
}