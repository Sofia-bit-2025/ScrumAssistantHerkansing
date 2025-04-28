package Model;

import java.util.Date;
//doel: Belangrijke beslissingen vast te leggen, apart van gewone berichten of threads.
//Specifieke belangrijke uitkomsten bijhouden
//Beslissingen koppelen aan de juiste User Story
//Snel terugvinden van belangrijke afspraken en besluiten
//1-Opslaan van een beslissing
//2-Overzicht creëren
//3-Belangrijke informatie filteren
//4-Rapportage

public class Beslissing {
    private int berichtID;         // ID van het originele bericht
    private String tekst;           // De tekst van het bericht
    private int userStoryID;        // Bijbehorende User Story ID
    private String userStoryTitel;  // Titel van de User Story
    private Date datum;             // Datum van het bericht

    // Constructor
    public Beslissing(int berichtID, String tekst, int userStoryID, String userStoryTitel, Date datum) {
        this.berichtID = berichtID;
        this.tekst = tekst;
        this.userStoryID = userStoryID;
        this.userStoryTitel = userStoryTitel;
        this.datum = datum;
    }

    // Getters
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

    public Date getDatum() {
        return datum;
    }


    @Override
    public String toString() {
        return "[Beslissing] " + tekst + " (User Story: " + userStoryTitel + ", Datum: " + datum + ")";
    }
}
