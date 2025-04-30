package Model;

import java.sql.*;

public class Authenticator {

    // Registreer een nieuwe gebruiker in de database
    public void register(String voornaam, String achternaam, String email, String rol) {
        if (voornaam == null || voornaam.isBlank() ||
                achternaam == null || achternaam.isBlank() ||
                email == null || email.isBlank() ||
                rol == null || rol.isBlank()) {
            throw new IllegalArgumentException("Alle velden zijn verplicht.");
        }

        String query = "INSERT INTO Gebruiker (Voornaam, Achternaam, Email, Rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, voornaam);
            stmt.setString(2, achternaam);
            stmt.setString(3, email);
            stmt.setString(4, rol);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int gebruikerID = keys.getInt(1);
                System.out.println("Registratie succesvol! Gebruiker ID: " + gebruikerID);
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Registratie succesvol!\nJouw ID is: " + gebruikerID +
                                "\nNaam: " + voornaam + " " + achternaam +
                                "\nEmail: " + email +
                                "\nRol: " + rol);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fout bij registratie: " + e.getMessage(), e);
        }
    }

    // Login via alleen email
    public Gebruiker login(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email mag niet leeg zijn.");
        }

        String query = "SELECT * FROM Gebruiker WHERE Email = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Gebruiker(
                        rs.getInt("gebruiker_id"),
                        rs.getString("voornaam"),
                        rs.getString("achternaam"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getTimestamp("datum_registratie").toLocalDateTime()
                );
            } else {
                throw new RuntimeException("Geen gebruiker gevonden met dit e-mailadres.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fout bij inloggen: " + e.getMessage(), e);
        }
    }

    // Login met combinatie van voornaam, e-mail en rol
    public Gebruiker loginMetEmail(String voornaam, String achternaam, String email, String rol) {
        if (voornaam == null || voornaam.trim().isEmpty() ||
                achternaam == null || achternaam.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("Voornaam, achternaam, e-mail en rol zijn verplicht.");
        }

        String query = "SELECT * FROM Gebruiker WHERE voornaam = ? AND achternaam = ? AND email = ? AND rol = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, voornaam);
            stmt.setString(2, achternaam);
            stmt.setString(3, email);
            stmt.setString(4, rol);  // Deze regel moet echt aanwezig zijn!

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Gebruiker(
                        rs.getInt("gebruiker_id"),
                        rs.getString("voornaam"),
                        rs.getString("achternaam"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getTimestamp("datum_registratie").toLocalDateTime()
                );
            } else {
                throw new RuntimeException("Geen gebruiker gevonden met deze gegevens.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fout bij inloggen met e-mail: " + e.getMessage(), e);
        }
    }

}
