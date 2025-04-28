package Service;

import Model.Beslissing;
import Model.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Bepalen welke berichten een echte beslissing zijn
//Beslissingen ophalen uit de database:Per User Story of alle beslissingen van vandaag.




//Deze methode markeert een bestaand bericht als een beslissing in de database.
//In de database heeft elk bericht een kolom isBeslissing
//Het zorgt dat beslissingen apart herkenbaar zijn.
public class BeslissingService {
    // Markeer een bericht als beslissing
    public void markeerBerichtAlsBeslissing(int berichtID) {
        String query = "UPDATE Bericht SET isBeslissing = TRUE WHERE BerichtID = ?";

        try (Connection conn = DatabaseConnector.connect();//Verbindt met de database
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, berichtID);
            stmt.executeUpdate();
            System.out.println("Bericht gemarkeerd als beslissing.");
        } catch (SQLException e) {
            System.err.println("Fout bij markeren van beslissing: " + e.getMessage());
        }
    }




    //Deze methode haalt alle berichten op die als beslissing zijn gemarkeerd
    //die horen bij één specifieke User Story.
    //bijv:Welke beslissingen zijn er al genomen over die specifieke User Story?
    // Haal beslissingen op voor een specifieke User Story
    //Je kunt per User Story snel alle genomen beslissingen bekijken.
    public List<Beslissing> getBeslissingenPerUserStory(int userStoryID) {
        List<Beslissing> beslissingen = new ArrayList<>();
        //SQL-query
        String query = "SELECT b.BerichtID, b.Tekst, b.Datum, us.UserStoryID, us.Titel " +
                "FROM Bericht b " +
                "JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "WHERE b.isBeslissing = TRUE AND b.UserStory_ID = ?";




        //zorgt ervoor dat de SQL query (beslissingen per User Story ophalen)uitgevoerd wordt
        //de resultaten in Beslissing objecten worden gestopt.
        try (Connection conn = DatabaseConnector.connect();//Verbinding maken met de database
             PreparedStatement stmt = conn.prepareStatement(query)) {//Voorbereiden van de query
            stmt.setInt(1, userStoryID);//Waarde invullen voor het vraagteken
            ResultSet rs = stmt.executeQuery();//Query uitvoeren

            while (rs.next()) {//Resultaten verwerken//Zolang er nog resultaten zijn
                beslissingen.add(new Beslissing(
                        rs.getInt("BerichtID"),
                        rs.getString("Tekst"),
                        rs.getInt("UserStoryID"),
                        rs.getString("Titel"),
                        rs.getDate("Datum")
                ));
            }
        } catch (SQLException e) {//Foutafhandeling
            System.err.println("Fout bij ophalen van beslissingen: " + e.getMessage());
        }

        //De lijst van beslissingen teruggeven
        return beslissingen;
    }




    //Deze methode haalt alle berichten die vandaag als ‘beslissing’ zijn gemarkeerd
    // uit de database, ongeacht welke User Story.

    // Haal alle beslissingen op van vandaag
    public List<Beslissing> getAlleBeslissingenVandaag() {//Nieuwe lege lijst maken
        List<Beslissing> beslissingen = new ArrayList<>();
        //SQL-query klaarmaken
        String query = "SELECT b.BerichtID, b.Tekst, b.Datum, us.UserStoryID, us.Titel " +
                "FROM Bericht b " +
                "JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "WHERE b.isBeslissing = TRUE AND b.Datum = CURDATE()";





        //Deze code voert de SQL-query uit
        //maakt van elk gevonden resultaat een Beslissing-object.
        //Daarna verzamelt hij al die beslissingen in een lijst en stuurt die lijst terug.
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                beslissingen.add(new Beslissing(
                        rs.getInt("BerichtID"),
                        rs.getString("Tekst"),
                        rs.getInt("UserStoryID"),
                        rs.getString("Titel"),
                        rs.getDate("Datum")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van beslissingen van vandaag: " + e.getMessage());
        }

        return beslissingen;
    }
}
