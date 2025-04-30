// beheert beslissingen in het systeem,
// gekoppeld aan berichten en user stories.
//Markeert bestaande berichten in de database als een officiële beslissing.
//Haalt beslissingen op die horen bij een specifieke User Story.
//Haalt alle beslissingen van de huidige dag op voor rapportages of overzicht.
package Service;
import Model.Beslissing;
import Model.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Deze klasse beheert alles rondom beslissingen (beslissingen markeren en ophalen)

public class BeslissingService {

    // Markeer een bericht als beslissing
    public void markeerBerichtAlsBeslissing(int berichtID) {
        String query = "UPDATE Bericht SET isBeslissing = TRUE WHERE BerichtID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, berichtID);
            stmt.executeUpdate();
            System.out.println("Bericht gemarkeerd als beslissing.");
        } catch (SQLException e) {
            System.err.println("Fout bij markeren van beslissing: " + e.getMessage());
        }
    }

    // Haal alle beslissingen op voor een specifieke User Story
    public List<Beslissing> getBeslissingenPerUserStory(int userStoryID) {
        List<Beslissing> beslissingen = new ArrayList<>();
        String query = "SELECT b.BerichtID, b.Tekst, b.Datum, us.UserStoryID, us.Titel " +
                "FROM Bericht b " +
                "JOIN UserStory us ON b.UserStory_ID = us.UserStoryID " +
                "WHERE b.isBeslissing = TRUE AND b.UserStory_ID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                beslissingen.add(new Beslissing(
                        rs.getInt("BerichtID"),
                        rs.getString("Tekst"),
                        rs.getInt("UserStoryID"),
                        rs.getString("Titel"),
                        rs.getDate("Datum").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van beslissingen: " + e.getMessage());
        }

        return beslissingen;
    }

    // Haal alle beslissingen van vandaag op
    public List<Beslissing> getAlleBeslissingenVandaag() {
        List<Beslissing> beslissingen = new ArrayList<>();
        String query = "SELECT b.BerichtID, b.Tekst, b.Datum, us.UserStoryID, us.Titel " +
                "FROM Bericht b " +
                "JOIN UserStory us ON b.UserStory_ID = us.UserStoryID " +
                "WHERE b.isBeslissing = TRUE AND b.Datum = CURDATE()";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                beslissingen.add(new Beslissing(
                        rs.getInt("BerichtID"),
                        rs.getString("Tekst"),
                        rs.getInt("UserStoryID"),
                        rs.getString("Titel"),
                        rs.getDate("Datum").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van beslissingen van vandaag: " + e.getMessage());
        }

        return beslissingen;
    }
}
