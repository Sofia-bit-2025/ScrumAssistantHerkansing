package Model;

import java.sql.Date;

public class Thread {
    private int id;
    private String titel;
    private Date datum;
    private boolean status;
    private int epicID;
    private int userStoryID;
    private int taakID;
    private int makerID; // New field for the maker
    private int juiste_antwoord;


    // Constructor with makerID
    public Thread(int id, String titel, Date datum, boolean status, int epicID, int userStoryID, int taakID, int makerID, int juiste_antwoord) {
        this.id = id;
        this.titel = titel;
        this.datum = datum;
        this.status = status;
        this.epicID = epicID;
        this.userStoryID = userStoryID;
        this.taakID = taakID;
        this.makerID = makerID; // Set the maker ID
        this.juiste_antwoord = juiste_antwoord;
    }

    // Getter for makerID
    public int getMakerID() {
        return makerID;
    }

    public int getJuiste_antwoord() {
        return juiste_antwoord;
    }

    // formatting van threads dus als (thread) word aangeroepen komt het er zo uit
    public String toString() {
        return "Model.Thread #" + id + " - \"" + titel + "\" [" + datum + "]"
                + "\nStatus: " + (status ? "Actief" : "Inactief")
                + (epicID > 0 ? "\n - Epic ID: " + epicID : "")
                + (userStoryID > 0 ? "\n - User Story ID: " + userStoryID : "")
                + (taakID > 0 ? "\n - Taak ID: " + taakID : "")
                + "\n";
    }

    public int getID() {
        return id;
    }
}