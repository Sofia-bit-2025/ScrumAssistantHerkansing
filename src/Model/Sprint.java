package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Sprint {
    private int sprintID;
    private String naam;
    private boolean status;
    private Date datum;

    public Sprint(int sprintID, String naam, boolean status, Date datum) {
        this.sprintID = sprintID;
        this.naam = naam;
        this.status = status;
        this.datum = datum;
    }

    public int getSprintID() {
        return sprintID;
    }

    public String getNaam() {
        return naam;
    }

    public boolean isActive() {
        return status;
    }

    public Date getDatum() {
        return datum;
    }

    public long getDaysLeft() {
        LocalDate startDate = datum.toLocalDate();
        LocalDate endDate = startDate.plusWeeks(2);  // Model.Sprint lasts 2 weeks
        if (ChronoUnit.DAYS.between(LocalDate.now(), endDate) <= 0) {
            this.status = false;
            try {
                sprintSluiten(this.sprintID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public static void sprintSluiten(int sprintID) throws SQLException {
        String sql = "UPDATE `scrumassistant`.`sprint` SET `Status` = 0 WHERE `SprintID` = " + sprintID;
        try (Connection conn = Account.connect()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
    }
    @Override
    public String toString() {
        return "Model.Sprint #" + sprintID + " | Naam: " + naam + " | Datum: " + datum + " | Actief: " + (status ? "Ja" : "Nee") + " | Tijd over: " + getDaysLeft() + " dagen";
    }
}