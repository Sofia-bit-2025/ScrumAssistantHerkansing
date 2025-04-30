//stelt een scrum sprint voor in het systeem
// en beheert de basisgegevens en tijdslogica van die sprint.
//Houdt informatie bij over de naam, status, startdatum en looptijd van een sprint.
//Kan berekenen hoeveel dagen er nog over zijn tot het einde van de sprint.
//Bepaalt of een sprint verlopen is
package Model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Sprint {
    private int sprintID;
    private String naam;
    private boolean actief;
    private LocalDate startDatum;

    public Sprint(int sprintID, String naam, boolean actief, LocalDate startDatum) {
        this.sprintID = sprintID;
        this.naam = naam;
        this.actief = actief;
        this.startDatum = startDatum;
    }

    public int getSprintID() {
        return sprintID;
    }

    public String getNaam() {
        return naam;
    }

    public boolean isActief() {
        return actief;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public long getDaysLeft() {
        LocalDate eindDatum = startDatum.plusWeeks(2);
        long dagenOver = ChronoUnit.DAYS.between(LocalDate.now(), eindDatum);
        return Math.max(dagenOver, 0);
    }

    public boolean isVerlopen() {
        return getDaysLeft() == 0;
    }

    @Override
    public String toString() {
        return "Sprint #" + sprintID + " | " + naam +
                " | Start: " + startDatum +
                " | Actief: " + (actief ? "Ja" : "Nee") +
                " | Dagen over: " + getDaysLeft();
    }
}
