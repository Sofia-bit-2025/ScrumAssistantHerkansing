package Service;

import Model.DatabaseConnector;
import Model.Epic;
import Service.NotificatieService;
import Model.Notificatie.TypeUpdate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EpicService {

    public void addEpic(String titel, String beschrijving, int scrumMasterID) throws SQLException {
        String sql = "INSERT INTO Epic (Titel, Beschrijving) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titel);
            stmt.setString(2, beschrijving);
            stmt.executeUpdate();

            // Notificatie aanmaken
            NotificatieService notificatieService = new NotificatieService();
            String tekst = "Nieuwe epic toegevoegd: " + titel;
            notificatieService.createNotificatie(tekst, TypeUpdate.EPIC_UPDATE, scrumMasterID);
        }
    }

    public List<Epic> getAllEpics() throws SQLException {
        List<Epic> epics = new ArrayList<>();
        String sql = "SELECT * FROM Epic";
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                epics.add(new Epic(
                        rs.getInt("EpicID"),
                        rs.getString("Titel"),
                        rs.getString("Beschrijving")
                ));
            }
        }
        return epics;
    }
}
