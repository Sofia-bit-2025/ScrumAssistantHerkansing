//SprintService regelt alle database-interacties
// rondom Sprints en de teamleden die aan sprints gekoppeld zijn.
//Actieve sprints ophalen (per gebruiker of algemeen).
//Nieuwe sprints toevoegen aan de database.
//Gebruikers koppelen aan een sprint of loskoppelen van een sprint.
//Alle gebruikers uit een sprint verwijderen.
package Service;
import Model.DatabaseConnector;
import Model.Gebruiker;
import Model.Sprint;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SprintService {

    // Haalt actieve sprints op voor een specifieke gebruiker
    public List<Sprint> getActiveSprintsForUser(Gebruiker gebruiker) throws SQLException {
        List<Sprint> sprints = new ArrayList<>();
        String sql = """
                SELECT s.SprintID, s.Naam, s.Status, s.Datum
                FROM Sprint s
                JOIN Sprint_Teamleden st ON s.SprintID = st.SprintID
                WHERE s.Status = 1 AND st.GebruikerID = ?
                """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gebruiker.getGebruikerID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sprints.add(new Sprint(
                        rs.getInt("SprintID"),
                        rs.getString("Naam"),
                        rs.getBoolean("Status"),
                        rs.getDate("Datum").toLocalDate()
                ));
            }
        }
        return sprints;
    }

    // Haalt alle actieve sprints op
    public List<Sprint> getActiveSprints() throws SQLException {
        List<Sprint> sprints = new ArrayList<>();
        String sql = "SELECT SprintID, Naam, Status, Datum FROM Sprint WHERE Status = 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sprints.add(new Sprint(
                        rs.getInt("SprintID"),
                        rs.getString("Naam"),
                        rs.getBoolean("Status"),
                        rs.getDate("Datum").toLocalDate()
                ));
            }
        }
        return sprints;
    }

    // Voegt een nieuwe sprint toe aan de database
    public void addSprint(String naamSprint, LocalDate datum) throws SQLException {
        String query = "INSERT INTO Sprint (Naam, Datum, Status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, naamSprint);
            stmt.setDate(2, Date.valueOf(datum != null ? datum : LocalDate.now()));
            stmt.setBoolean(3, true);
            stmt.executeUpdate();
        }
    }

    // Voegt een gebruiker toe aan een sprint
    public void userAddSprint(int sprintID, int gebruikerID) throws SQLException {
        String sql = "INSERT INTO Sprint_Teamleden (SprintID, GebruikerID) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sprintID);
            stmt.setInt(2, gebruikerID);
            stmt.executeUpdate();
            System.out.println("Gebruiker toegevoegd aan sprint.");
        }
    }

    // Verwijdert een gebruiker uit een sprint
    public void userDeleteSprint(int sprintID, int gebruikerID) throws SQLException {
        String sql = "DELETE FROM Sprint_Teamleden WHERE SprintID = ? AND GebruikerID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sprintID);
            stmt.setInt(2, gebruikerID);
            stmt.executeUpdate();
            System.out.println("Gebruiker verwijderd uit sprint.");
        }
    }

    // Verwijdert alle gebruikers van een sprint
    public void userDeleteAllSprint(int sprintID) throws SQLException {
        String sql = "DELETE FROM Sprint_Teamleden WHERE SprintID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sprintID);
            stmt.executeUpdate();
            System.out.println("Alle gebruikers verwijderd uit sprint.");
        }
    }
}