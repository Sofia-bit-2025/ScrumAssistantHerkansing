//De klasse Beslissing vertegenwoordigt een belangrijke beslissing
// die is genomen binnen een User Story.
//Houdt gegevens vast over een beslissing: bericht-ID, tekst van de beslissing,
// gekoppelde User Story (ID en titel) en
// de datum waarop de beslissing is genomen.
//Maakt het mogelijk om beslissingen gestructureerd op te slaan,
// te tonen en te gebruiken in rapportages.
package Model;
import java.time.LocalDate;

public class Beslissing {
    private int berichtID;
    private String tekst;
    private int userStoryID;
    private String userStoryTitel;
    private LocalDate datum;

    public Beslissing(int berichtID, String tekst, int userStoryID, String userStoryTitel, LocalDate datum) {
        this.berichtID = berichtID;
        this.tekst = tekst;
        this.userStoryID = userStoryID;
        this.userStoryTitel = userStoryTitel;
        this.datum = datum;
    }

    public int getBerichtID() {
        return berichtID;
    }

    public String getTekst() {
        return tekst;
    }

    public int getUserStoryID() {
        return userStoryID;
    }

    public String getUserStoryTitel() {
        return userStoryTitel;
    }

    public LocalDate getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return "[Beslissing] " + tekst + " (User Story: " + userStoryTitel + ", Datum: " + datum + ")";
    }
}
