package Service;

import Model.DatabaseConnector;
import Model.Taak;
import Model.Notificatie.TypeUpdate;
import Service.NotificatieService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaakService {

    public void addTaak(String titel, String beschrijving, int userStoryID, int scrumMasterID) throws SQLException {
        String sql = "INSERT INTO Taak (Titel, Beschrijving, UserStoryID) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, titel);
            stmt.setString(2, beschrijving);
            stmt.setInt(3, userStoryID);
            stmt.executeUpdate();

            // Haal de gegenereerde taak-ID op (optioneel, als je dit wil loggen/koppelen)
            ResultSet rs = stmt.getGeneratedKeys();
            int taakID = -1;
            if (rs.next()) {
                taakID = rs.getInt(1);
            }

            // Notificatie aanmaken
            NotificatieService notificatieService = new NotificatieService();
            String tekst = "Nieuwe taak toegevoegd aan user story " + userStoryID + ": " + titel;
            notificatieService.createNotificatie(tekst, TypeUpdate.TAAK, scrumMasterID, userStoryID, null, taakID);

        } catch (SQLException e) {
            System.err.println("Fout bij toevoegen van taak: " + e.getMessage());
            throw e;
        }
    }

    public List<Taak> getTakenByUserStory(int userStoryID) throws SQLException {
        List<Taak> taken = new ArrayList<>();
        String sql = "SELECT * FROM Taak WHERE UserStoryID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                taken.add(new Taak(
                        rs.getInt("TaakID"),
                        rs.getString("Titel"),
                        rs.getString("Beschrijving"),
                        rs.getInt("UserStoryID")
                ));
            }
        }

        return taken;
    }
}
