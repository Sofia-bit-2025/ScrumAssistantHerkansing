package Service;

import Model.DatabaseConnector;
import Model.UserStory;
import Service.NotificatieService;
import Model.Notificatie.TypeUpdate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserStoryService {

    public void addUserStory(String titel, String beschrijving, int epicID, int scrumMasterID) throws SQLException {
        String sql = "INSERT INTO UserStory (Titel, Beschrijving, EpicID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titel);
            stmt.setString(2, beschrijving);
            stmt.setInt(3, epicID);
            stmt.executeUpdate();

            // notificatie aanmaken
            NotificatieService notificatieService = new NotificatieService();
            String tekst = "Nieuwe user story toegevoegd aan epic " + epicID + ": " + titel;
            notificatieService.createNotificatie(tekst, TypeUpdate.USERSTORY_UPDATE, scrumMasterID);
        }
    }

    public List<UserStory> getUserStoriesByEpic(int epicID) throws SQLException {
        List<UserStory> stories = new ArrayList<>();
        String sql = "SELECT * FROM UserStory WHERE EpicID = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, epicID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stories.add(new UserStory(
                        rs.getInt("UserStoryID"),
                        rs.getString("Titel"),
                        rs.getString("Beschrijving"),
                        rs.getInt("EpicID")
                ));
            }
        }
        return stories;
    }
}
