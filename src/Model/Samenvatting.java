package Model;

import java.util.List;
import java.util.Date;

//een compleet dagelijks overzicht maken van alle communicatie die hoort bij één specifieke User Story.
//verzamelt alle berichten en alle threads bewaart wanneer die communicatie is gebeurd
//1-Groeperen van informatie per User Story
//2-Structuur bieden voor een dagsamenvatting
//3-Ondersteuning voor rapportages

public class Samenvatting {
    private int userStoryID;         // Op welke User Story hoort deze samenvatting
    private String userStoryTitel;   // Titel van de User Story
    private List<String> berichten;  // Alle teksten uit berichten (Message)
    private List<String> threads;    // Alle titels uit Threads
    private Date datum;              // Datum van samenvatting (bijvoorbeeld vandaag)

    // Constructor
    public Samenvatting(int userStoryID, String userStoryTitel, List<String> berichten, List<String> threads, Date datum) {
        this.userStoryID = userStoryID;
        this.userStoryTitel = userStoryTitel;
        this.berichten = berichten;
        this.threads = threads;
        this.datum = datum;
    }

    // Getters en eventueel Setters
    public int getUserStoryID() {
        return userStoryID;
    }

    public String getUserStoryTitel() {
        return userStoryTitel;
    }

    public List<String> getBerichten() {
        return berichten;
    }

    public List<String> getThreads() {
        return threads;
    }

    public Date getDatum() {
        return datum;
    }
}
