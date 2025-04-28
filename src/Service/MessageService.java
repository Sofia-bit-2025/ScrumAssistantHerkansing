package Service;

import Model.DatabaseConnector;
import Model.Gebruiker;
import Service.NotificatieService;
import java.sql.*;
import java.time.LocalDate;

public class MessageService {

    // ------------- PRIVATE HULPMETHODES -------------

    private void displayMessages(String query, int id) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, id);
            processMessageResultSet(stmt.executeQuery(), id, -1);
        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van berichten: " + e.getMessage());
        }
    }

    private void displayMessages(String query, int id1, int id2, int juisteAntwoordID) {
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, id1);
            stmt.setInt(2, id2);
            processMessageResultSet(stmt.executeQuery(), id1, juisteAntwoordID);
        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van berichten: " + e.getMessage());
        }
    }

    private void processMessageResultSet(ResultSet rs, int id, int juisteAntwoordID) throws SQLException {
        if (!rs.next()) {
            System.out.println("Geen berichten gevonden voor ID: " + id);
            return;
        }
        rs.beforeFirst();
        while (rs.next()) {
            int berichtID = rs.getInt("berichtID");
            boolean isJuisteAntwoord = berichtID == juisteAntwoordID;
            if (isJuisteAntwoord) {
                System.out.println("\n********** [JUISTE ANTWOORD] **********");
            }
            System.out.println("Naam: " + rs.getString("Naam"));
            System.out.println("Rol: " + rs.getString("Rol"));
            if (rs.getString("EpicTitel") != null) {
                System.out.println("Epic titel: " + rs.getString("EpicTitel"));
                System.out.println("Epic beschrijving: " + rs.getString("EpicBeschrijving"));
            }
            if (rs.getString("UserStoryTitel") != null) {
                System.out.println("User Story titel: " + rs.getString("UserStoryTitel"));
                System.out.println("User Story beschrijving: " + rs.getString("UserStoryBeschrijving"));
            }
            if (rs.getString("TaakTitel") != null) {
                System.out.println("Taak titel: " + rs.getString("TaakTitel"));
                System.out.println("Taak beschrijving: " + rs.getString("TaakBeschrijving"));
            }
            System.out.println("Tekst: " + rs.getString("Tekst"));
            System.out.println("BerichtID: " + berichtID);
            System.out.println("Datum: " + rs.getDate("Datum"));
            if (isJuisteAntwoord) {
                System.out.println("********** EINDE JUISTE ANTWOORD **********\n");
            } else {
                System.out.println("------------------------------------");
            }
        }
    }

    // ------------- PUBLIEKE METHODES -------------

    public void sendMessage(int gebruikerID, int sprintID, String tekst, int epicID, int userStoryID, int taakID) {
        String messageInsert = "INSERT INTO Bericht (Tekst, Datum, AfzenderID, Epic_ID, UserStory_ID, Taak_ID) VALUES (?, ?, ?, ?, ?, ?)";
        String sprintLinkInsert = "INSERT INTO Sprint_Bericht_Verbinding (BerichtID, SprintID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnector.connect()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(messageInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, tekst);
                stmt.setDate(2, Date.valueOf(LocalDate.now()));
                stmt.setInt(3, gebruikerID);
                if (epicID > 0) stmt.setInt(4, epicID); else stmt.setNull(4, java.sql.Types.INTEGER);
                if (userStoryID > 0) stmt.setInt(5, userStoryID); else stmt.setNull(5, java.sql.Types.INTEGER);
                if (taakID > 0) stmt.setInt(6, taakID); else stmt.setNull(6, java.sql.Types.INTEGER);
                stmt.executeUpdate();
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int berichtID = generatedKeys.getInt(1);
                    try (PreparedStatement sprintStmt = connection.prepareStatement(sprintLinkInsert)) {
                        sprintStmt.setInt(1, berichtID);
                        sprintStmt.setInt(2, sprintID);
                        sprintStmt.executeUpdate();
                    }
                }
                connection.commit();
                System.out.println("Bericht succesvol verzonden.");
                if (tekst.toLowerCase().contains("afgerond") || tekst.toLowerCase().contains("bijgewerkt")) {
                    NotificatieService notificatieService = new NotificatieService();
                    String typeUpdate = (epicID != 0) ? "Epic" : (userStoryID != 0) ? "UserStory" : "Taak";
                    notificatieService.createNotificatie("Update: " + tekst, typeUpdate, gebruikerID);
                }
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Fout bij verzenden bericht: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Fout bij verbinding database: " + e.getMessage());
        }
    }

    public void sendMessageToThread(int gebruikerID, int threadID, String tekst, int epicID, int userStoryID, int taakID) throws SQLException {
        String sql = "INSERT INTO Bericht (Tekst, Datum, AfzenderID, Thread_ID, Epic_ID, UserStory_ID, Taak_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tekst);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, gebruikerID);
            stmt.setInt(4, threadID);
            if (epicID > 0) stmt.setInt(5, epicID); else stmt.setNull(5, java.sql.Types.INTEGER);
            if (userStoryID > 0) stmt.setInt(6, userStoryID); else stmt.setNull(6, java.sql.Types.INTEGER);
            if (taakID > 0) stmt.setInt(7, taakID); else stmt.setNull(7, java.sql.Types.INTEGER);
            stmt.executeUpdate();
            System.out.println("Bericht succesvol geplaatst in thread.");
        }
    }

    public void displayMessagesForSprint(int sprintID) {
        String query = "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                "FROM Bericht b " +
                "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                "JOIN Sprint_Bericht_Verbinding sbv ON b.BerichtID = sbv.BerichtID " +
                "WHERE sbv.SprintID = ?";
        displayMessages(query, sprintID);
    }

    public void displayMessagesInThread(int threadID) {
        String juisteAntwoordQuery = "SELECT Juiste_Antwoord FROM Model.Thread WHERE ThreadID = ?";
        int juisteAntwoordID = -1;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(juisteAntwoordQuery)) {
            stmt.setInt(1, threadID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                juisteAntwoordID = rs.getInt("Juiste_Antwoord");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String query = "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                "FROM Bericht b " +
                "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                "WHERE b.Thread_ID = ? " +
                "ORDER BY (b.BerichtID = (SELECT Juiste_Antwoord FROM Model.Thread WHERE ThreadID = ?)) DESC, b.Datum DESC";
        displayMessages(query, threadID, threadID, juisteAntwoordID);
    }

    public void displayMessagesForUserStory(int userStoryID) {
        String query = "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                "FROM Bericht b " +
                "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                "WHERE b.UserStory_ID = ?";
        displayMessages(query, userStoryID);
    }

    public void searchMessageContent(String zoek, int sprintID, Gebruiker gebruiker) {
        boolean isScrumMaster = gebruiker.getRol().equalsIgnoreCase("scrummaster");
        String joins = "JOIN Sprint_Bericht_Verbinding sbv ON b.BerichtID = sbv.BerichtID " +
                (isScrumMaster ? "" : "JOIN sprint_teamleden spt ON sbv.SprintID = spt.SprintID ");
        String where = "WHERE sbv.SprintID = ?" +
                (isScrumMaster ? "" : " AND spt.GebruikerID = " + gebruiker.getGebruikerID()) +
                " AND b.Tekst LIKE \"%" + zoek + "%\"";
        String query = "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel, e.Beschrijving, us.Titel, us.Beschrijving, t.Titel, t.Beschrijving, b.BerichtID " +
                "FROM Bericht b " +
                "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                joins + where;
        displayMessages(query, sprintID);
    }

    public void searchMessageTitel(Gebruiker gebruiker, String zoek, int sprintID) {
        boolean isScrumMaster = gebruiker.getRol().equalsIgnoreCase("scrummaster");
        String joins = "JOIN Sprint_Bericht_Verbinding sbv ON b.BerichtID = sbv.BerichtID " +
                (isScrumMaster ? "" : "JOIN sprint_teamleden spt ON sbv.SprintID = spt.SprintID ");
        String where = "WHERE sbv.SprintID = ?" +
                (isScrumMaster ? "" : " AND spt.GebruikerID = " + gebruiker.getGebruikerID()) +
                " AND (us.Titel LIKE \"%" + zoek + "%\" OR t.Titel LIKE \"%" + zoek + "%\" OR e.Titel LIKE \"%" + zoek + "%\")";
        String query = "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel, e.Beschrijving, us.Titel, us.Beschrijving, t.Titel, t.Beschrijving, b.BerichtID " +
                "FROM Bericht b " +
                "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                joins + where;
        displayMessages(query, sprintID);
    }
}
