//De klasse Message vertegenwoordigt een individueel bericht binnen een communicatieplatform.
//Houdt alle eigenschappen van een bericht vast: ID, tekst, afzender, gekoppelde entiteiten
// (thread, epic, user story, taak), datum en of het een beslissing is.
//Biedt toegang tot deze eigenschappen en maakt een eenvoudige weergave via toString() mogelijk.
package Model;
import java.time.LocalDate;

public class Message {
    private int berichtID;
    private String tekst;
    private String naamAfzender;
    private int gebruikerID;
    private int threadID;
    private int epicID;
    private int userStoryID;
    private int taakID;
    private LocalDate datum;
    private boolean isBeslissing;

    public Message(int berichtID, String tekst, String naamAfzender, int gebruikerID,
                   int threadID, int epicID, int userStoryID, int taakID,
                   LocalDate datum, boolean isBeslissing) {
        this.berichtID = berichtID;
        this.tekst = tekst;
        this.naamAfzender = naamAfzender;
        this.gebruikerID = gebruikerID;
        this.threadID = threadID;
        this.epicID = epicID;
        this.userStoryID = userStoryID;
        this.taakID = taakID;
        this.datum = datum;
        this.isBeslissing = isBeslissing;
    }

    public int getBerichtID() {
        return berichtID;
    }

    public String getTekst() {
        return tekst;
    }

    public String getNaamAfzender() {
        return naamAfzender;
    }

    public int getGebruikerID() {
        return gebruikerID;
    }

    public int getThreadID() {
        return threadID;
    }

    public int getEpicID() {
        return epicID;
    }

    public int getUserStoryID() {
        return userStoryID;
    }

    public int getTaakID() {
        return taakID;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public boolean isBeslissing() {
        return isBeslissing;
    }

    @Override
    public String toString() {
        return "Bericht #" + berichtID + ": " + tekst + " [" + datum + "]";
    }
}
