//gesprekken uit threads en berichten te filteren op basis van een User Story
//nagaan welke communicatie gekoppeld is aan een bepaalde user story.
package Test;
import Model.Message;
import Model.Thread;
import Service.MessageService;
import Service.ThreadService;
import java.sql.*;
import java.util.List;
import java.util.Scanner;


// gebruiker een User Story id te laten invoeren
//vervolgens filteren op relevante gesprekken uit threads en bericht
public class FilterGesprekTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ThreadService threadService = new ThreadService();
        MessageService messageService = new MessageService();

        while (true) {
            System.out.print("\nVoer een User Story ID in om te filteren (of 0 om te stoppen): ");
            int userStoryID = Integer.parseInt(scanner.nextLine());

            if (userStoryID == 0) {
                System.out.println("Programma afgesloten.");
                break;
            }


            //Informatie op te halen over de opgegeven user story
            //alle threads te tonen die gekoppeld zijn aan die user story
            toonUserStoryInfo(userStoryID);

            System.out.println("\n--- Threads gekoppeld aan deze User Story ---");
            List<Thread> threads = null;
            try {
                threads = threadService.getThreadsByUserStory(userStoryID);
                if (threads.isEmpty()) {
                    System.out.println("Geen threads gevonden.");
                } else {
                    for (Thread thread : threads) {
                        System.out.printf("[#%d] %s (%s)\n",
                                thread.getThreadID(), thread.getTitel(), thread.getDatum());
                    }
                }
            } catch (SQLException e) {
                System.err.println("Fout bij ophalen van threads: " + e.getMessage());
            }




            //alle losse berichten te tonen die gekoppeld zijn aan een  User Story
            System.out.println("\n--- Berichten gekoppeld aan deze User Story ---");
            List<Message> berichten = messageService.getMessagesByUserStoryIDs(List.of(userStoryID));
            if (berichten.isEmpty()) {
                System.out.println("Geen berichten gevonden.");
            } else {
                for (Message msg : berichten) {
                    System.out.printf("[%s] %s\n", msg.getDatum(), msg.getTekst());
                }
            }
        }
    }






    // Laat zien bij welke epic de user story hoort
    private static void toonUserStoryInfo(int userStoryID) {
        String query = "SELECT US.Titel AS UserStoryTitel, US.Beschrijving, E.Titel AS EpicTitel " +
                "FROM UserStory US " +
                "LEFT JOIN Epic E ON US.EpicID = E.EpicID " +
                "WHERE US.UserStoryID = ?";






        //Database connection
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/scrum_db", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("User Story: " + rs.getString("UserStoryTitel"));
                System.out.println("Beschrijving: " + rs.getString("Beschrijving"));
                System.out.println("hoort bij Epic: " + rs.getString("EpicTitel"));
            } else {
                System.out.println("User Story niet gevonden.");
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van User Story info: " + e.getMessage());
        }
    }
}


