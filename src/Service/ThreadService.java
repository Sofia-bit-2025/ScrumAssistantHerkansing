package Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThreadService {

    // Methode om een thread aan te maken
    public void createThread(String titel, int sprintID, int epicID, int userStoryID, int taakID, int gebruikerID) throws SQLException {
        String sql = "INSERT INTO Model.Thread (Status, Titel, Datum, Sprint_ID, Epic_ID, UserStory_ID, Taak_ID, Maker) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Connectie opzetten en query klaarzetten
        try (Connection conn = Account.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true); // status staat op true = thread is actief
            stmt.setString(2, titel);
            stmt.setDate(3, Date.valueOf(LocalDate.now())); // datum van vandaag erin zetten
            stmt.setInt(4, sprintID);

            // Alleen koppelen als er daadwerkelijk een ID is doorgegeven (> 0), anders NULL
            if (epicID > 0) stmt.setInt(5, epicID); else stmt.setNull(5, java.sql.Types.INTEGER);
            if (userStoryID > 0) stmt.setInt(6, userStoryID); else stmt.setNull(6, java.sql.Types.INTEGER);
            if (taakID > 0) stmt.setInt(7, taakID); else stmt.setNull(7, java.sql.Types.INTEGER);

            // Voeg de maker toe
            stmt.setInt(8, gebruikerID); // koppel de maker (gebruikerID)

            stmt.executeUpdate(); // uitvoeren die handel
            System.out.println("Model.Thread succesvol aangemaakt."); // even een check
        }
    }

    // Methode om alle threads op te halen die bij een bepaalde sprint horen, inclusief de maker
    public List<Thread> getThreadsBySprint(int sprintID) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT * FROM Model.Thread WHERE Sprint_ID = ?";

        try (Connection conn = Account.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sprintID); // ophalen op basis van sprint ID
            ResultSet rs = stmt.executeQuery(); // uitvoeren en resultaat opslaan

            // Voor elke rij in het resultaat een Model.Thread-object maken en aan de lijst toevoegen
            while (rs.next()) {
                int id = rs.getInt("ThreadID");
                boolean status = rs.getBoolean("Status");
                String titel = rs.getString("Titel");
                Date datum = rs.getDate("Datum");
                int epicID = rs.getInt("Epic_ID");
                int userStoryID = rs.getInt("UserStory_ID");
                int taakID = rs.getInt("Taak_ID");
                int maker = rs.getInt("Maker"); // Ophalen van MakerID
                int juist_antwoord = rs.getInt("Juiste_Antwoord");

                // Voeg de maker toe aan het Model.Thread-object
                threads.add(new Thread(id, titel, datum, status, epicID, userStoryID, taakID, maker, juist_antwoord)); // Toevoegen van makerID
            }
        }
        return threads; // klaar, lijst teruggeven
    }

    public void setJuisteAntwoord(int threadID, int berichtID) throws SQLException {
        String query = "UPDATE Model.Thread SET Juiste_Antwoord = ? WHERE ThreadID = ?";

        try (Connection conn = Account.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, berichtID);
            stmt.setInt(2, threadID);
            stmt.executeUpdate();
        }
    }

    public void sluitThread(int threadID) throws SQLException {
        String query = "UPDATE thread SET status = 0 WHERE ThreadID = ?";

        try (Connection conn = Account.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, threadID);
            stmt.executeUpdate();
        }
    }

    public boolean isThreadGesloten(int threadID) throws SQLException {
        String query = "SELECT status FROM thread WHERE ThreadID = ?";
        try (Connection conn = Account.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, threadID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("status") == 0;
            }
        }
        return false;
    }

}