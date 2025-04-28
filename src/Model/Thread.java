package Model;

import java.sql.Date;


 //Deze klasse stelt een Thread (discussie) voor in het systeem.
 //Een Thread hoort bij een Epic, User Story of Taak en heeft een maker.
 // Eigenschappen zijn: titel, startdatum, status, koppelingen en een juist antwoord.

public class Thread {
    private int threadID;          // Unieke ID van de thread
    private String titel;          // Titel van de thread
    private Date datum;            // Datum waarop de thread is aangemaakt
    private boolean status;        // Status van de thread: actief (true) of gesloten (false)
    private int epicID;            // Epic waaraan deze thread gekoppeld is
    private int userStoryID;       // User Story waaraan deze thread gekoppeld is
    private int taakID;            // Taak waaraan deze thread gekoppeld is
    private int makerID;           // ID van de gebruiker die de thread heeft aangemaakt
    private int juisteAntwoordID;  // ID van het bericht dat als juiste antwoord is gekozen


    // Constructor om een nieuwe Thread aan te maken.
    public Thread(int threadID, String titel, Date datum, boolean status, int epicID, int userStoryID, int taakID, int makerID, int juisteAntwoordID) {
        this.threadID = threadID;
        this.titel = titel;
        this.datum = datum;
        this.status = status;
        this.epicID = epicID;
        this.userStoryID = userStoryID;
        this.taakID = taakID;
        this.makerID = makerID;
        this.juisteAntwoordID = juisteAntwoordID;
    }


    //Geeft het ID van de gebruiker die deze thread heeft aangemaakt.
    public int getMakerID() {
        return makerID;
    }


    //Geeft het ID van het juiste antwoord als dat bestaat.
    public int getJuisteAntwoordID() {
        return juisteAntwoordID;
    }


    //Geeft het unieke ID van deze thread.
    public int getThreadID() {
        return threadID;
    }



   //een mooie tekstweergave van de thread.
    @Override
    public String toString() {
        return "Thread #" + threadID + " - \"" + titel + "\" [" + datum + "]"
                + "\nStatus: " + (status ? "Actief" : "Inactief")
                + (epicID > 0 ? "\n - Epic ID: " + epicID : "")
                + (userStoryID > 0 ? "\n - User Story ID: " + userStoryID : "")
                + (taakID > 0 ? "\n - Taak ID: " + taakID : "")
                + "\n";
    }
}
