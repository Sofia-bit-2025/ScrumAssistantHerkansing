package Service;

import Model.DatabaseConnector;
import Model.Gebruiker;
import Model.Sprint;
import Model.Notificatie;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//alles regelt wat te maken heeft met Sprints, Epics, User Stories en Taken in de database
//zorgt voor communicatie tussen je programma en de database.
//Alles wat te maken heeft met:
//1- Sprints beheren
//2- Epics aanmaken
//3- User Stories aanmaken
//4- Taken aanmaken
//5- Gebruikers toevoegen/verwijderen aan Sprint
// Gebeurt hier centraal via deze klasse.




//Deze methode haalt uit de database alle actieve (lopende) sprints op
// waarvoor een bepaalde gebruiker is ingeschreven
//Laat mij de sprints zien waar ik als teamlid bij hoor en die nog actief zijn
public class SprintService {
    public List<Sprint> getActiveSprintsForUser(Gebruiker gebruiker) throws SQLException {
        List<Sprint> sprints = new ArrayList<>();
        String sql =
                "SELECT * " +
                        "FROM sprint AS sp " +
                        "JOIN sprint_teamleden AS spt on sp.SprintID = spt.SprintID " +
                        "JOIN gebruiker AS g on spt.GebruikerID = g.GebruikerID " +
                        "WHERE Status = 1 AND spt.GebruikerID = " + gebruiker.getGebruikerID();// Active sprints only
        try (Connection conn = DatabaseConnector.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int sprintID = rs.getInt("SprintID");
                String naam = rs.getString("Naam");
                boolean status = rs.getBoolean("Status");
                Date datum = rs.getDate("Datum");
                sprints.add(new Sprint(sprintID, naam, status, datum));
            }
        }
        return sprints;
    }




//Deze methode haalt uit de database alle actieve sprints op,
// ongeacht welke gebruiker eraan meedoet
    //Geef mij een lijst van alle actieve sprints in het hele systeem
    public List<Sprint> getActiveSprints() throws SQLException {
        List<Sprint> sprints = new ArrayList<>();
        String sql = "SELECT * FROM sprint WHERE Status = 1"; // Active sprints only
        try (Connection conn = DatabaseConnector.connect(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int sprintID = rs.getInt("SprintID");
                String naam = rs.getString("Naam");
                boolean status = rs.getBoolean("Status");
                Date datum = rs.getDate("Datum");
                sprints.add(new Sprint(sprintID, naam, status, datum));
            }
        }
        return sprints;
    }




    // maakt een nieuwe Sprint aan en zet die in de database.
    // als je  een nieuw sprintplan wilt starten voor een Scrum team.
    public void addSprint(String naamSprint, Date datum) throws SQLException {
        String query = "INSERT INTO Model.Sprint (Naam, Datum, Status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, naamSprint);
            // Set the current date if datum is not passed explicitly
            if (datum == null) {
                datum = Date.valueOf(LocalDate.now());
            }
            stmt.setDate(2, datum);
            stmt.setInt(3,1);
            stmt.executeUpdate();
        }
    }




    // voegt een nieuwe Epic toe aan de database
    //Maak een nieuwe Epic aan met een titel en beschrijving
    public void addEpic(String title, String description,int gebruikerID) throws SQLException {
        System.out.println("Saving Epic to database...");
        String sql = "INSERT INTO Epics (titel, beschrijving) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.executeUpdate();

            NotificatieService notificatieService = new NotificatieService();
            notificatieService.createNotificatie("Nieuwe Epic: " + title, "Epic", gebruikerID);

        }
    }



//voegt een nieuwe User Story toe aan de database, gekoppeld aan een bestaande Epic
    //Maak een nieuwe User Story en koppel hem aan de juiste Epic
    public void addUserStory(String title, String description, int epicID,int gebruikerID) throws SQLException {
        System.out.println("Saving User Story to database...");
        String sql = "INSERT INTO UserStories (titel, beschrijving, epic_ID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, epicID);
            stmt.executeUpdate();

            NotificatieService notificatieService = new NotificatieService();
            notificatieService.createNotificatie("Nieuwe User Story: " + title, "UserStory", gebruikerID);

        }
    }



    //voegt een nieuwe Taak toe aan de database, gekoppeld aan een bestaande User Story
    //Maak een nieuwe concrete taak aan en koppel die aan een User Story.
    public void addTask(String title, String description, int userStoryID,int gebruikerID) throws SQLException {
        System.out.println("Saving taak to database...");
        String sql = "INSERT INTO Taken (titel, beschrijving, userStory_ID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, userStoryID);
            stmt.executeUpdate();

            NotificatieService notificatieService = new NotificatieService();
            notificatieService.createNotificatie("Nieuwe Taak: " + title, "Taak", gebruikerID);

        }
    }

    //Voegt een gebruiker toe aan een sprint in de database
    public void userAddSprint(int SprintID, int gebruikersID) throws SQLException {
        String sql = "INSERT INTO `scrumassistant`.`sprint_teamleden`(`SprintID`,`GebruikerID`)VALUES(?,?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SprintID);
            stmt.setInt(2, gebruikersID);
            stmt.executeUpdate();
            System.out.print("Model.Gebruiker toegevoegd aan sprint");
        }
    }
    //Verwijdert een gebruiker uit een sprint in de database
    public void userDeleteSprint(int SprintID, int gebruikersID) throws SQLException {
        String sql = "DELETE FROM sprint_teamleden WHERE GebruikerID = ? AND SprintID = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gebruikersID);
            stmt.setInt(2, SprintID);
            stmt.executeUpdate();
            System.out.print("Model.Gebruiker verwijderd van sprint\n");
        }
    }



    //verwijdert alle gebruikers die gekoppeld zijn aan een bepaalde sprint
    //Verwijder iedereen die gekoppeld is aan Sprint X
    public void userDeleteAllSprint(int SprintID) throws SQLException {
        String sql = "DELETE FROM sprint_teamleden WHERE SprintID = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, SprintID);
            stmt.executeUpdate();
            System.out.print("Iedereen verwijdered van de sprint\n");
        }
    }
}