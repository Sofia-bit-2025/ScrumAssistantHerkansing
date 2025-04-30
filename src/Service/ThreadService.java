//beheert de threads binnen het systeem,
// gekoppeld aan sprints, epics, user stories of taken.
//Nieuwe thread aanmaken en in de database opslaan.
//Threads ophalen per sprint of per user story.
//Juiste antwoord instellen binnen een thread.
//Thread sluiten en status controleren of een thread nog actief is.
package Service;
import Model.DatabaseConnector;
import Model.Thread;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThreadService {

    public void createThread(String titel, int sprintID, int epicID, int userStoryID, int taakID, int gebruikerID) throws SQLException {
        String sql = "INSERT INTO Thread (Status, Titel, Datum, Sprint_ID, Epic_ID, UserStory_ID, Taak_ID, Maker) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setString(2, titel);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setInt(4, sprintID);
            stmt.setObject(5, epicID > 0 ? epicID : null, Types.INTEGER);
            stmt.setObject(6, userStoryID > 0 ? userStoryID : null, Types.INTEGER);
            stmt.setObject(7, taakID > 0 ? taakID : null, Types.INTEGER);
            stmt.setInt(8, gebruikerID);
            stmt.executeUpdate();
            System.out.println("Thread succesvol aangemaakt.");
        }
    }

    public List<Thread> getThreadsBySprint(int sprintID) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT * FROM Thread WHERE Sprint_ID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sprintID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                threads.add(mapThread(rs));
            }
        }
        return threads;
    }

    public List<Thread> getThreadsByUserStory(int userStoryID) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT * FROM Thread WHERE UserStory_ID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                threads.add(mapThread(rs));
            }
        }
        return threads;
    }

    public List<Thread> getThreadsByUserStories(List<Integer> userStoryIDs) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        if (userStoryIDs == null || userStoryIDs.isEmpty()) return threads;

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < userStoryIDs.size(); i++) {
            placeholders.append("?");
            if (i < userStoryIDs.size() - 1) placeholders.append(",");
        }

        String query = "SELECT * FROM Thread WHERE UserStory_ID IN (" + placeholders + ")";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < userStoryIDs.size(); i++) {
                stmt.setInt(i + 1, userStoryIDs.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                threads.add(mapThread(rs));
            }
        }
        return threads;
    }


    public void displayThreadsByUserStories(List<Integer> userStoryIDs) {
        if (userStoryIDs == null || userStoryIDs.isEmpty()) {
            System.out.println(" Geen user story-ID's opgegeven.");
            return;
        }

        try {
            List<Thread> threads = getThreadsByUserStories(userStoryIDs);

            if (threads.isEmpty()) {
                System.out.println("Geen gesprekken gevonden voor de geselecteerde user stories: " + userStoryIDs);
            } else {
                System.out.println(" Gesprekken gekoppeld aan user stories: " + userStoryIDs);
                for (Thread thread : threads) {
                    System.out.println(thread);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van threads: " + e.getMessage());
        }
    }


    public void displayThreadsByUserStory(int userStoryID) {
        try {
            List<Thread> threads = getThreadsByUserStory(userStoryID);
            if (threads.isEmpty()) {
                System.out.println("Geen gesprekken gevonden voor User Story ID: " + userStoryID);
            } else {
                System.out.println("Gesprekken gekoppeld aan User Story #" + userStoryID + ":");
                for (Thread thread : threads) {
                    System.out.println(thread);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van threads: " + e.getMessage());
        }
    }

    public void setJuisteAntwoord(int threadID, int berichtID) throws SQLException {
        String query = "UPDATE Thread SET Juiste_Antwoord = ? WHERE ThreadID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, berichtID);
            stmt.setInt(2, threadID);
            stmt.executeUpdate();
        }
    }

    public void sluitThread(int threadID) throws SQLException {
        String query = "UPDATE Thread SET Status = 0 WHERE ThreadID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, threadID);
            stmt.executeUpdate();
        }
    }

    public boolean isThreadGesloten(int threadID) throws SQLException {
        String query = "SELECT Status FROM Thread WHERE ThreadID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, threadID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && !rs.getBoolean("Status");
        }
    }

    private Thread mapThread(ResultSet rs) throws SQLException {
        return new Thread(
                rs.getInt("ThreadID"),
                rs.getString("Titel"),
                rs.getDate("Datum").toLocalDate(),
                rs.getBoolean("Status"),
                rs.getInt("Epic_ID"),
                rs.getInt("UserStory_ID"),
                rs.getInt("Taak_ID"),
                rs.getInt("Maker"),
                rs.getInt("Juiste_Antwoord")
        );
    }
}