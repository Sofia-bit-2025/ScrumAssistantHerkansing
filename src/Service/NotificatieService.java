package Service;

import Model.DatabaseConnector;
import Model.Notificatie;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Deze klasse beheert alles rondom Notificaties:
// - Melding aanmaken
// - Melding ophalen voor een gebruiker




//Een nieuwe notificatie opslaan in de database
//Een nieuwe notificatie melding aanmaken en opslaan in de database.
//Met deze methode worden de gebruikers geinformeerd
//dat er iets belangrijks is gebeurd, zoals een update van een taak, user story epic
public class NotificatieService {
    public void createNotificatie(String tekst, String typeUpdate, int gebruikerID) {
        //SQL-opdracht klaarmaken
        String sql = "INSERT INTO Notificatie (Tekst, Datum, TypeUpdate, GebruikerID) VALUES (?, ?, ?, ?)";
        //Verbinding maken en voorbereiden
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            //De gegevens invullen
            stmt.setString(1, tekst);
            stmt.setDate(2, Date.valueOf(LocalDate.now())); // Vandaag als datum
            stmt.setString(3, typeUpdate); // Bijvoorbeeld: "Taak", "UserStory", "Epic"
            stmt.setInt(4, gebruikerID);
            //De opdracht uitvoeren
            stmt.executeUpdate();
            //Resultaat tonen of fout afvangen
            System.out.println("Notificatie succesvol aangemaakt.");

        } catch (SQLException e) {
            System.err.println("Fout bij het aanmaken van notificatie: " + e.getMessage());
        }
    }






    // Alle notificaties ophalen voor een specifieke gebruiker
    //een overzicht kunt tonen van meldingen.
    //De notificaties worden gesorteerd op datum, nieuwste eerst

    //Lijst klaarzetten om notificaties op te slaan
    public List<Notificatie> getNotificatiesVoorGebruiker(int gebruikerID) {
        List<Notificatie> notificaties = new ArrayList<>();
        //SQL-opdracht klaarmaken
        String sql = "SELECT * FROM Notificatie WHERE GebruikerID = ? ORDER BY Datum DESC";




        //Verbinding maken met de database
        // de opgevraagde notificaties daadwerkelijk ophalen voor de opgegeven gebruikerID
        try (Connection conn = DatabaseConnector.connect();//Verbinding maken met de database
             PreparedStatement stmt = conn.prepareStatement(sql)) {//SQL-query voorbereiden

            stmt.setInt(1, gebruikerID);//Waarde invullen in de query
            ResultSet rs = stmt.executeQuery();//Query uitvoeren






            //Alle gevonden notificaties uit het resultaat (ResultSet rs) stap voor stap uitlezen
            // en omzetten naar Notificatie-objecten die in de lijst notificaties zijn bewaart.
            while (rs.next()) {//Door het resultaat heen lopen

                //Gegevens van de notificatie ophalen
                int notificatieID = rs.getInt("NotificatieID");
                String tekst = rs.getString("Tekst");
                Date datum = rs.getDate("Datum");
                String typeUpdate = rs.getString("TypeUpdate");

                //Nieuw Notificatie-object maken
                Notificatie notificatie = new Notificatie(notificatieID, tekst, datum, typeUpdate, gebruikerID);
                notificaties.add(notificatie);//Notificatie toevoegen aan de lijst
            }

        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van notificaties: " + e.getMessage());
        }

        return notificaties;//Na de loop: lijst teruggeven
    }
}
