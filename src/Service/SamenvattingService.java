package Service;

import Model.DatabaseConnector;
import Model.Samenvatting;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class SamenvattingService {

    // genereer een lijst van samenvattingen voor vandaag
    public List<Samenvatting> generateDailySummary() {
        Map<Integer, Samenvatting> samenvattingen = new HashMap<>();
        LocalDate vandaag = LocalDate.now();

        try (Connection conn = DatabaseConnector.connect()) {

            // 1. Berichten ophalen van vandaag
            String messageQuery = "SELECT b.UserStory_ID, us.Titel AS UserStoryTitel, b.Tekst " +
                    "FROM Bericht b " +
                    "JOIN UserStory us ON b.UserStory_ID = us.UserStoryID " +
                    "WHERE b.Datum = ?";
            try (PreparedStatement stmt = conn.prepareStatement(messageQuery)) {
                stmt.setDate(1, Date.valueOf(vandaag));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String tekst = rs.getString("Tekst");

                    samenvattingen
                            .computeIfAbsent(userStoryID,
                                    id -> new Samenvatting(0, id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), vandaag))
                            .getBerichten()
                            .add(tekst);
                }
            }

            // 2. Threads ophalen van vandaag
            String threadQuery = "SELECT t.UserStory_ID, us.Titel AS UserStoryTitel, t.Titel AS ThreadTitel " +
                    "FROM Model.Thread t " +
                    "JOIN UserStory us ON t.UserStory_ID = us.UserStoryID " +
                    "WHERE t.Datum = ?";
            try (PreparedStatement stmt = conn.prepareStatement(threadQuery)) {
                stmt.setDate(1, Date.valueOf(vandaag));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String threadTitel = rs.getString("ThreadTitel");

                    samenvattingen
                            .computeIfAbsent(userStoryID,
                                    id -> new Samenvatting(0, id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), vandaag))
                            .getThreads()
                            .add(threadTitel);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van samenvatting: " + e.getMessage());
        }

        return new ArrayList<>(samenvattingen.values());
    }

    // Genereer samenvattingen van alle data alle datums
    public List<Samenvatting> generateSummaryAllDates() {
        Map<Integer, Samenvatting> samenvattingen = new HashMap<>();

        try (Connection conn = DatabaseConnector.connect()) {

            // Berichten ophalen alle data
            String messageQuery = "SELECT b.UserStory_ID, us.Titel AS UserStoryTitel, b.Tekst, b.Datum " +
                    "FROM Bericht b " +
                    "JOIN UserStory us ON b.UserStory_ID = us.UserStoryID";
            try (PreparedStatement stmt = conn.prepareStatement(messageQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String tekst = rs.getString("Tekst");
                    LocalDate datum = rs.getDate("Datum").toLocalDate();

                    samenvattingen
                            .computeIfAbsent(userStoryID,
                                    id -> new Samenvatting(0, id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), datum))
                            .getBerichten()
                            .add(tekst);
                }
            }

            // Threads ophalen alle data
            String threadQuery = "SELECT t.UserStory_ID, us.Titel AS UserStoryTitel, t.Titel AS ThreadTitel, t.Datum " +
                    "FROM Model.Thread t " +
                    "JOIN UserStory us ON t.UserStory_ID = us.UserStoryID";
            try (PreparedStatement stmt = conn.prepareStatement(threadQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String threadTitel = rs.getString("ThreadTitel");
                    LocalDate datum = rs.getDate("Datum").toLocalDate();

                    samenvattingen
                            .computeIfAbsent(userStoryID,
                                    id -> new Samenvatting(0, id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), datum))
                            .getThreads()
                            .add(threadTitel);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van samenvatting (alle data): " + e.getMessage());
        }

        return new ArrayList<>(samenvattingen.values());
    }

    // rapport printen van de gegenereerde samenvattingen
    public void printSamenvattingen(List<Samenvatting> samenvattingen) {
        for (Samenvatting s : samenvattingen) {
            System.out.println("\nSamenvatting voor User Story #" + s.getUserStoryID() + " - " + s.getUserStoryTitel());
            System.out.println("Datum: " + s.getDatum());
            System.out.println("--- Berichten ---");
            for (String bericht : s.getBerichten()) {
                System.out.println("- " + bericht);
            }
            System.out.println("--- Threads ---");
            for (String thread : s.getThreads()) {
                System.out.println("- " + thread);
            }
            System.out.println("-------------------------------------------");
        }
    }
}
