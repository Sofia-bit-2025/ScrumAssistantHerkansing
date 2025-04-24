package Service;

import java.sql.*;
import java.time.LocalDate;
public class MessageService {

    // Methode om berichten weer te geven voor een specifieke sprint
    // Voor queries met één parameter
    private void displayMessages(String query, int id) {
        try (Connection connection = Account.connect();
             PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, id);

            processMessageResultSet(stmt.executeQuery(), id, -1);

        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van berichten: " + e.getMessage());
        }
    }

    // Voor queries met twee parameters
    private void displayMessages(String query, int id1, int id2, int juisteAntwoordID) {
        try (Connection connection = Account.connect();
             PreparedStatement stmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, id1);
            stmt.setInt(2, id2);

            processMessageResultSet(stmt.executeQuery(), id1, juisteAntwoordID);

        } catch (SQLException e) {
            System.err.println("Fout bij het ophalen van berichten: " + e.getMessage());
        }
    }

    // Gescheiden logica voor resultaatverwerking
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

            String naam = rs.getString("Naam");
            String rol = rs.getString("Rol");
            String epicTitel = rs.getString("EpicTitel");
            String epicBeschrijving = rs.getString("EpicBeschrijving");
            String userStoryTitel = rs.getString("UserStoryTitel");
            String userStoryBeschrijving = rs.getString("UserStoryBeschrijving");
            String taakTitel = rs.getString("TaakTitel");
            String taakBeschrijving = rs.getString("TaakBeschrijving");
            String tekst = rs.getString("Tekst");
            int BerichtID = rs.getInt("berichtID");
            Date datum = rs.getDate("Datum");

            System.out.println("Naam: " + naam);
            System.out.println("Rol: " + rol);
            if (epicTitel != null || userStoryTitel != null || taakTitel != null) {
                if (epicTitel != null) {
                    System.out.println("Epic titel: " + epicTitel);
                    System.out.println("Epic beschrijving: " + epicBeschrijving);
                }
                if (userStoryTitel != null) {
                    System.out.println("User Story titel: " + userStoryTitel);
                    System.out.println("User Story beschrijving: " + userStoryBeschrijving);
                }
                if (taakTitel != null) {
                    System.out.println("Taak titel: " + taakTitel);
                    System.out.println("Taak beschrijving: " + taakBeschrijving);
                }
            }
            System.out.println("Tekst: " + tekst);
            System.out.println("BerichtID: " + BerichtID);
            System.out.println("Datum: " + datum);
            if (isJuisteAntwoord) {
                System.out.println("********** EINDE JUISTE ANTWOORD **********\n");
            } else {
                System.out.println("------------------------------------");
            }
        }
    }


    // Methode om een bericht te versturen voor een specifieke sprint en optioneel te koppelen aan Scrum-elementen
    public void sendMessage(int gebruikerID, int sprintID, String tekst, int epicID, int userStoryID, int taakID) {
        String messageInsert = "INSERT INTO Bericht (Tekst, Datum, AfzenderID, epic_id, UserStory_ID, taak_id) VALUES (?, ?, ?, ?, ?, ?)";
        String sprintLinkInsert = "INSERT INTO Sprint_Bericht_Verbinding (BerichtID, SprintID) VALUES (?, ?)";

        try (Connection connection = Account.connect()) {
            connection.setAutoCommit(false); // Start een transactie

            try (PreparedStatement stmt = connection.prepareStatement(messageInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, tekst);
                stmt.setDate(2, Date.valueOf(LocalDate.now()));
                stmt.setInt(3, gebruikerID);

                // Koppel Scrum-elementen, of zet null als ze niet opgegeven zijn
                if (epicID != 0) {
                    stmt.setInt(4, epicID);
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }

                if (userStoryID != 0) {
                    stmt.setInt(5, userStoryID);
                } else {
                    stmt.setNull(5, java.sql.Types.INTEGER);
                }

                if (taakID != 0) {
                    stmt.setInt(6, taakID);
                } else {
                    stmt.setNull(6, java.sql.Types.INTEGER);
                }

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int berichtID = generatedKeys.getInt(1);

                            // Koppel het bericht aan de sprint
                            try (PreparedStatement sprintStmt = connection.prepareStatement(sprintLinkInsert)) {
                                sprintStmt.setInt(1, berichtID);
                                sprintStmt.setInt(2, sprintID);
                                sprintStmt.executeUpdate();
                            }
                        }
                    }
                }

                connection.commit();
                System.out.println("Bericht succesvol verzonden inclusief Scrum-elementen.");
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Fout bij het verzenden van bericht: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Fout bij het verbinden met de database: " + e.getMessage());
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


    // Methode om berichten weer te geven in een specifieke thread en sorteren als er een juist antwoord is
    public void displayMessagesInThread(int threadID) {
        String juisteAntwoordQuery = "SELECT Juiste_Antwoord FROM Model.Thread WHERE ThreadID = ?";
        int juisteAntwoordID = -1;

        try (Connection conn = Account.connect();
             PreparedStatement stmt = conn.prepareStatement(juisteAntwoordQuery)) {
            stmt.setInt(1, threadID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                juisteAntwoordID = rs.getInt("Juiste_Antwoord");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String query =
                "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, " +
                        "e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                        "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, " +
                        "t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                        "FROM Bericht b " +
                        "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                        "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                        "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                        "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                        "WHERE b.Thread_ID = ? " +
                        "ORDER BY (b.BerichtID = (SELECT Juiste_Antwoord FROM Model.Thread WHERE ThreadID = ?)) DESC, b.Datum DESC";
        displayMessages(query, threadID, threadID, juisteAntwoordID);
    }


    // Methode om een bericht naar een thread te sturen
    public void sendMessageToThread(int gebruikerID, int threadID, String tekst, int epicID, int userStoryID, int taakID) throws SQLException {
        String sql = "INSERT INTO Bericht (Tekst, Datum, AfzenderID, Thread_ID, Epic_ID, UserStory_ID, Taak_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Account.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tekst);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, gebruikerID);
            stmt.setInt(4, threadID);
            if (epicID > 0) {
                stmt.setInt(5, epicID);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            if (userStoryID > 0) {
                stmt.setInt(6, userStoryID);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            if (taakID > 0) {
                stmt.setInt(7, taakID);
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();
            System.out.println("Bericht succesvol geplaatst in thread " + threadID);
        }
    }

    public void searchMessageContent(String zoek, int SprintID, Gebruiker gebruiker){

        boolean isScrumMaster = gebruiker.getRol().equalsIgnoreCase("scrummaster");

        // true : false
        String joins =
                "JOIN Sprint_Bericht_Verbinding sbv ON b.BerichtID = sbv.BerichtID " +
                        (isScrumMaster ? "" : "JOIN sprint_teamleden spt ON sbv.SprintID = spt.SprintID ");
        String whereClause =
                "WHERE sbv.SprintID = ?" +
                        (isScrumMaster ? "" : " AND spt.GebruikerID = " + gebruiker.getGebruikerID()) +
                        " AND b.Tekst LIKE \"%" + zoek + "%\"";

        String sprintsSelecten =
                "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                        "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                        "FROM Bericht b " +
                        "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                        "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                        "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                        "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                        joins +
                        whereClause;


        displayMessages(sprintsSelecten, SprintID);

    }

    public void searchMessageTitel(Gebruiker gebruiker, String zoek, int SprintID){
            boolean isScrumMaster = gebruiker.getRol().equalsIgnoreCase("scrummaster");


            String joins =
                    "JOIN Sprint_Bericht_Verbinding sbv ON b.BerichtID = sbv.BerichtID " +
                            (isScrumMaster ? "" : "JOIN sprint_teamleden spt ON sbv.SprintID = spt.SprintID ");


            String whereClause =
                    "WHERE sbv.SprintID = ?" +
                            (isScrumMaster ? "" : " AND spt.GebruikerID = " + gebruiker.getGebruikerID()) +
                            " AND (us.Titel LIKE \"%" + zoek + "%\" " +
                            "OR t.Titel LIKE \"%" + zoek + "%\" " +
                            "OR e.Titel LIKE \"%" + zoek + "%\")";

            String sprintsSelecten =
                    "SELECT b.Tekst, b.Datum, g.Naam, g.Rol, e.Titel AS EpicTitel, e.Beschrijving AS EpicBeschrijving, " +
                            "us.Titel AS UserStoryTitel, us.Beschrijving AS UserStoryBeschrijving, t.Titel AS TaakTitel, t.Beschrijving AS TaakBeschrijving, b.BerichtID " +
                            "FROM Bericht b " +
                            "JOIN Model.Gebruiker g ON b.AfzenderID = g.GebruikerID " +
                            "LEFT JOIN Epics e ON b.Epic_ID = e.EpicID " +
                            "LEFT JOIN UserStories us ON b.UserStory_ID = us.UserStoryID " +
                            "LEFT JOIN Taken t ON b.Taak_ID = t.TaakID " +
                            joins +
                            whereClause;

            displayMessages(sprintsSelecten, SprintID);
    }
}