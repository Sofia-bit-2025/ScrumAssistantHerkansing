package Service;

import Model.DatabaseConnector;
import Model.Thread;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//Threads aanmaken, ophalen, sluiten en juiste antwoorden instellen.
//1- en nieuwe  ("thread") starten.
//2- alle thread van een sprint ophalen en laten zien.
//3-een antwoord als het juiste antwoord markeren.
//4- een thread afsluiten als het klaar is.



//Een nieuwe discussie of vraag starten binnen een sprint,
// die eventueel hoort bij een Epic, User Story of Taak.
public class ThreadService {
    public void createThread(String titel, int sprintID, int epicID, int userStoryID, int taakID, int gebruikerID) throws SQLException {
        String sql = "INSERT INTO Model.Thread (Status, Titel, Datum, Sprint_ID, Epic_ID, UserStory_ID, Taak_ID, Maker) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Connectie opzetten en query klaarzetten
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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




    //Alle threads ophalen die horen bij een specifieke sprint
    //Welke discussies zijn er gestart binnen sprint X
    // Methode om alle threads op te halen die bij een bepaalde sprint horen, inclusief de maker
    public List<Thread> getThreadsBySprint(int sprintID) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT * FROM Model.Thread WHERE Sprint_ID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sprintID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ThreadID");
                boolean status = rs.getBoolean("Status");
                String titel = rs.getString("Titel");
                Date datum = rs.getDate("Datum");
                int epicID = rs.getInt("Epic_ID");
                int userStoryID = rs.getInt("UserStory_ID");
                int taakID = rs.getInt("Taak_ID");
                int makerID = rs.getInt("Maker");
                int juisteAntwoordID = rs.getInt("Juiste_Antwoord");

                threads.add(new Thread(id, titel, datum, status, epicID, userStoryID, taakID, makerID, juisteAntwoordID));
            }
        }
        return threads;
    }

    // 🔵 🔵 🔵  NIEUWE METHODE 🔵 🔵 🔵

    // Haal alle threads op van een specifieke User Story
    public List<Thread> getThreadsByUserStory(int userStoryID) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String query = "SELECT * FROM Model.Thread WHERE UserStory_ID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ThreadID");
                boolean status = rs.getBoolean("Status");
                String titel = rs.getString("Titel");
                Date datum = rs.getDate("Datum");
                int epicID = rs.getInt("Epic_ID");
                int userStory_ID = rs.getInt("UserStory_ID");
                int taakID = rs.getInt("Taak_ID");
                int makerID = rs.getInt("Maker");
                int juisteAntwoordID = rs.getInt("Juiste_Antwoord");

                threads.add(new Thread(id, titel, datum, status, epicID, userStoryID, taakID, makerID, juisteAntwoordID));

            }
        }
        return threads;
    }

    public void displayThreadsByUserStory(int userStoryID) {
        ThreadService threadService = new ThreadService();
        try {
            List<Thread> threads = threadService.getThreadsByUserStory(userStoryID);

            if (threads.isEmpty()) {
                System.out.println("Geen gesprekken gevonden voor User Story ID: " + userStoryID);
            } else {
                System.out.println("Gesprekken gekoppeld aan User Story #" + userStoryID + ":");
                for (Thread thread : threads) {
                    System.out.println(thread);  // 👈 hier wordt toString() automatisch gebruikt
                }
            }
        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van threads: " + e.getMessage());
        }
    }





    //Een specifiek bericht aanwijzen als het juiste antwoord binnen een thread
    //in een discussie aangeven welk bericht het officiële antwoord is
    public void setJuisteAntwoord(int threadID, int berichtID) throws SQLException {
        String query = "UPDATE Model.Thread SET Juiste_Antwoord = ? WHERE ThreadID = ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, berichtID);
            stmt.setInt(2, threadID);
            stmt.executeUpdate();
        }
    }



    //Een thread sluiten door zijn status in de database op 'inactief' te zetten.
//deze discussie is nu officieel afgelopen. Mensen kunnen er niet meer op reageren
    public void sluitThread(int threadID) throws SQLException {
        String query = "UPDATE thread SET status = 0 WHERE ThreadID = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, threadID);
            stmt.executeUpdate();
        }
    }



    //Controleren of een thread gesloten is
    public boolean isThreadGesloten(int threadID) throws SQLException {
        String query = "SELECT status FROM thread WHERE ThreadID = ?";
        try (Connection conn = DatabaseConnector.connect();
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