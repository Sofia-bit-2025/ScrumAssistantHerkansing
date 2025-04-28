package Service;

import Model.DatabaseConnector;
import Model.Samenvatting;
import java.time.LocalDate;
import java.sql.*;
import java.util.*;
import java.util.Date;

//Alle berichten en threads van vandaag per User Story op te halen
//groeperen in Samenvatting objecten
//een overzicht van alles wat vandaag gebeurd is per User Story.
//1-Dagelijkse samenvattingen maken
//2-Resultaat geven als lijst van Samenvatting objecten
//3-print-functie voor rapportage



//Een dagelijkse samenvatting maken:
// alle berichten en threads van vandaag ophalen en per User Story groeperen.
public class SamenvattingService {
    // Methode om alle samenvattingen te maken van vandaag
    public List<Samenvatting> generateDailySummary() {
        //De key is het UserStoryID
        //de value is een bijbehorende Samenvatting.
        Map<Integer, Samenvatting> samenvattingen = new HashMap<>();//Een lege HashMap wordt aangemaakt.
        //Deze datum wordt straks gebruikt om alle berichten en threads
        // van vandaag te selecteren uit de database.
        Date vandaag = java.sql.Date.valueOf(LocalDate.now());



        //Verbinding maken met de database.
        //SQL-query klaarmaken om alle berichten van vandaag op te halen
        //met de bijbehorende User Story Titel.
        //Alleen berichten die vandaag zijn geplaatst worden meegenomen
        try (Connection conn = DatabaseConnector.connect()) {
            // 1. Berichten ophalen van vandaag
            String messageQuery = "SELECT b.UserStory_ID, us.Titel AS UserStoryTitel, b.Tekst " +
                    "FROM Bericht b " +
                    "JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                    "WHERE b.Datum = ?";



            //De messageQuery uitvoeren.
            //Alle berichten van vandaag uit de database lezen.
            //De berichten groeperen per User Story in een Samenvatting-object.
            //Voeg de tekst van het bericht toe aan de lijst van berichten van die User Story
            try (PreparedStatement stmt = conn.prepareStatement(messageQuery)) {
                stmt.setDate(1, (java.sql.Date) vandaag);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String tekst = rs.getString("Tekst");

                    samenvattingen
                            .computeIfAbsent(userStoryID, id -> new Samenvatting(id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), vandaag))
                            .getBerichten()
                            .add(tekst);
                }
            }






            //Een SQL-query voorbereiden om alle Threads van vandaag op te halen uit de database.
            // 2. Threads ophalen van vandaag
            //Die SQL-opdracht gaat alle Threads ophalen die vandaag zijn gemaakt,
            // samen met de User Story waar ze bij horen.
            String threadQuery = "SELECT t.UserStory_ID, us.Titel AS UserStoryTitel, t.Titel AS ThreadTitel " +
                    "FROM Model.Thread t " +
                    "JOIN UserStories us ON t.UserStory_ID = us.UserStoryID " +
                    "WHERE t.Datum = ?";



            //haalt alle Threads van vandaag op.
            //Per User Story worden deze Threads toegevoegd aan een Samenvatting.
            //één overzicht van alle berichten + alle threads per User Story.
            try (PreparedStatement stmt = conn.prepareStatement(threadQuery)) {
                stmt.setDate(1, (java.sql.Date) vandaag);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int userStoryID = rs.getInt("UserStory_ID");
                    String userStoryTitel = rs.getString("UserStoryTitel");
                    String threadTitel = rs.getString("ThreadTitel");

                    samenvattingen
                            .computeIfAbsent(userStoryID, id -> new Samenvatting(id, userStoryTitel, new ArrayList<>(), new ArrayList<>(), vandaag))
                            .getThreads()
                            .add(threadTitel);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van samenvatting: " + e.getMessage());
        }

        return new ArrayList<>(samenvattingen.values());
    }







    //Deze methode presenteert de gegenereerde samenvattingen op een leesbare manier aan de
    //Om op het scherm (console) een net en overzichtelijk rapport te laten zien van alles
    // wat vandaag gebeurd is per User Story.handig voor:Dagelijkse updates en Teambesprekingen
    // Extra methode om samenvattingen mooi te printen
    public void printSamenvattingen(List<Samenvatting> samenvattingen) {
        for (Samenvatting s : samenvattingen) {
            System.out.println("\n Samenvatting voor User Story #" + s.getUserStoryID() + " - " + s.getUserStoryTitel());
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
