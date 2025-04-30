//het aanmaken, ophalen en tonen van notificaties in de applicatie

package Service;
import Model.DatabaseConnector;
import Model.Notificatie;
import Model.Notificatie.TypeUpdate;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



//een nieuwe notificatie toevoegen  zonder koppelingen -> dus géén user story, epic of taak erbij
public class NotificatieService {

    public void createNotificatie(String tekst, TypeUpdate typeUpdate, int gebruikerID) {
        String sql = "INSERT INTO Notificatie (tekst, datum, type_update, gebruiker_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();//verbinding met de database
             PreparedStatement stmt = conn.prepareStatement(sql)) {//Bereidt een SQL query voor
            //gegevens in de query zetten.
            stmt.setString(1, tekst);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setString(3, typeUpdate.name());
            stmt.setInt(4, gebruikerID);

            stmt.executeUpdate();
            System.out.println("Notificatie succesvol aangemaakt: " + tekst);

        } catch (SQLException e) {
            System.err.println("Fout bij aanmaken van notificatie: " + tekst);
            e.printStackTrace();
        }
    }




    //een uitgebreide notificatie toevoegen aan de database
    // die gekoppeld is aan een user story, epic  taak.
    public void createNotificatie(String tekst, TypeUpdate typeUpdate, int gebruikerID,
                                  Integer userStoryID, Integer epicID, Integer taakID) {
        String sql = "INSERT INTO Notificatie (tekst, datum, type_update, gebruiker_id, userstory_id, epic_id, taak_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();//verbinding met de database.
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tekst);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setString(3, typeUpdate.name());
            stmt.setInt(4, gebruikerID);
            stmt.setObject(5, userStoryID, Types.INTEGER);
            stmt.setObject(6, epicID, Types.INTEGER);
            stmt.setObject(7, taakID, Types.INTEGER);

            stmt.executeUpdate();
            System.out.println("Uitgebreide notificatie aangemaakt: " + tekst);

        } catch (SQLException e) {
            System.err.println("Fout bij aanmaken van uitgebreide notificatie: " + tekst);
            e.printStackTrace();
        }
    }





    //haal alle notificaties op van één specifieke gebruiker uit de database,
    // gesorteerd van nieuw naar oud.
    public List<Notificatie> getNotificatiesVoorGebruiker(int gebruikerID) {
        List<Notificatie> notificaties = new ArrayList<>();
        String sql = "SELECT * FROM Notificatie WHERE gebruiker_id = ? ORDER BY datum DESC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gebruikerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TypeUpdate type = parseTypeUpdate(rs.getString("type_update"));

                Notificatie notificatie = new Notificatie(
                        rs.getInt("notificatie_id"),
                        rs.getString("tekst"),
                        rs.getDate("datum").toLocalDate(),
                        type,
                        rs.getInt("gebruiker_id"),
                        getNullableInt(rs, "userstory_id"),
                        getNullableInt(rs, "epic_id"),
                        getNullableInt(rs, "taak_id")
                );
                notificaties.add(notificatie);
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van notificaties:");
            e.printStackTrace();
        }

        return notificaties;
    }





    //haal alle notificaties op, inclusief wie ze heeft aangemaakt
    // en toont ze in een leesbare tekstvorm.
    public List<String> getAlleNotificatiesMetGebruikersInfo() {
        List<String> meldingen = new ArrayList<>();
        String sql = """
            SELECT N.tekst, N.datum, N.type_update, G.voornaam, G.achternaam
            FROM Notificatie N
            JOIN Gebruiker G ON N.gebruiker_id = G.gebruiker_id
            ORDER BY N.datum DESC
        """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (rs.next()) {
                String datum = rs.getDate("datum").toLocalDate().format(formatter);
                String type = rs.getString("type_update");
                String tekst = rs.getString("tekst");
                String voornaam = rs.getString("voornaam");
                String achternaam = rs.getString("achternaam");

                meldingen.add(String.format("[%s] %s door %s %s: %s",
                        datum, type, voornaam, achternaam, tekst));
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van meldingen met gebruikersinfo:");
            e.printStackTrace();
        }

        return meldingen;
    }





    //haalt alle notificaties op inclusief extra details over de gekoppelde user story epic taak
    // en de naam van de gebruiker die de melding heeft aangemaakt
    //in één overzicht zien wie wat heeft gedaan.
    public List<String> getAlleNotificatiesMetDetails() {
        List<String> meldingen = new ArrayList<>();
        String sql = """
            SELECT N.tekst, N.datum, N.type_update,
                   G.voornaam, G.achternaam,
                   US.titel AS userstory_titel,
                   E.titel AS epic_titel,
                   T.titel AS taak_titel
            FROM Notificatie N
            JOIN Gebruiker G ON N.gebruiker_id = G.gebruiker_id
            LEFT JOIN UserStory US ON N.userstory_id = US.UserStoryID
            LEFT JOIN Epic E ON N.epic_id = E.EpicID
            LEFT JOIN Taak T ON N.taak_id = T.TaakID
            ORDER BY N.datum DESC
        """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (rs.next()) {
                String datum = rs.getDate("datum").toLocalDate().format(formatter);
                String type = rs.getString("type_update");
                String tekst = rs.getString("tekst");
                String voornaam = rs.getString("voornaam");
                String achternaam = rs.getString("achternaam");

                StringBuilder melding = new StringBuilder();
                melding.append(String.format("[%s] %s door %s %s:\n%s", datum, type, voornaam, achternaam, tekst));

                if (rs.getString("userstory_titel") != null)
                    melding.append("\n↳ User Story: ").append(rs.getString("userstory_titel"));
                if (rs.getString("epic_titel") != null)
                    melding.append("\n↳ Epic: ").append(rs.getString("epic_titel"));
                if (rs.getString("taak_titel") != null)
                    melding.append("\n↳ Taak: ").append(rs.getString("taak_titel"));

                meldingen.add(melding.toString());
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van meldingen met details:");
            e.printStackTrace();
        }

        return meldingen;
    }





    //alle notificaties die door een specifieke gebruiker zijn aangemaakt inc.koppelingen
    public List<String> getNotificatiesVoorGebruikerMetDetails(int gebruikerID) {
        List<String> meldingen = new ArrayList<>();
        String sql = """
            SELECT N.tekst, N.datum, N.type_update,
                   G.voornaam, G.achternaam,
                   US.titel AS userstory_titel,
                   E.titel AS epic_titel,
                   T.titel AS taak_titel
            FROM Notificatie N
            JOIN Gebruiker G ON N.gebruiker_id = G.gebruiker_id
            LEFT JOIN UserStory US ON N.userstory_id = US.UserStoryID
            LEFT JOIN Epic E ON N.epic_id = E.EpicID
            LEFT JOIN Taak T ON N.taak_id = T.TaakID
            WHERE N.gebruiker_id = ?
            ORDER BY N.datum DESC
        """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gebruikerID);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (rs.next()) {
                String datum = rs.getDate("datum").toLocalDate().format(formatter);
                String type = rs.getString("type_update");
                String tekst = rs.getString("tekst");
                String voornaam = rs.getString("voornaam");
                String achternaam = rs.getString("achternaam");

                StringBuilder melding = new StringBuilder();
                melding.append(String.format("[%s] %s door %s %s:\n%s", datum, type, voornaam, achternaam, tekst));

                if (rs.getString("userstory_titel") != null)
                    melding.append("\n↳ User Story: ").append(rs.getString("userstory_titel"));
                if (rs.getString("epic_titel") != null)
                    melding.append("\n↳ Epic: ").append(rs.getString("epic_titel"));
                if (rs.getString("taak_titel") != null)
                    melding.append("\n↳ Taak: ").append(rs.getString("taak_titel"));

                meldingen.add(melding.toString());
            }

        } catch (SQLException e) {
            System.err.println("Fout bij ophalen van meldingen voor gebruiker:");
            e.printStackTrace();
        }

        return meldingen;
    }






    // tekst uit de database omzetten  naar het juiste TypeUpdate (enumtype)
    //Voorkomt verkeerd gespelde waarde
    private TypeUpdate parseTypeUpdate(String raw) {
        try {
            return TypeUpdate.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TypeUpdate.SYSTEEM_UPDATE;
        }
    }



    //voorkomen verwarring tussen 0 als echte waarde en geen waarde in app.
    private Integer getNullableInt(ResultSet rs, String kolom) throws SQLException {
        int value = rs.getInt(kolom);
        return rs.wasNull() ? null : value;
    }
}
