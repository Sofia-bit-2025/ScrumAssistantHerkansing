package Model;

import java.sql.Date;

// Deze klasse vertegenwoordigt een melding (notificatie) in het systeem.
// Doel: belangrijke updates (bijvoorbeeld wijziging van een Taak/UserStory/Epic) bijhouden en tonen aan de Scrum Master.

public class Notificatie {
    private int notificatieID;   // Unieke ID van de melding
    private String tekst;        // Inhoud/samenvatting van de melding ("Taak 'Login' afgerond")
    private Date datum;          // Datum waarop de melding is aangemaakt
    private String typeUpdate;   // Type wijziging: "Taak", "UserStory", "Epic"
    private int gebruikerID;     // Voor welke gebruiker (meestal Scrum Master) is deze melding bedoeld?

    // Constructor (om nieuwe Notificatie-objecten te maken)
    public Notificatie(int notificatieID, String tekst, Date datum, String typeUpdate, int gebruikerID) {
        this.notificatieID = notificatieID;
        this.tekst = tekst;
        this.datum = datum;
        this.typeUpdate = typeUpdate;
        this.gebruikerID = gebruikerID;
    }

    // Getters om bij de gegevens te kunnen

    public int getNotificatieID() {
        return notificatieID;
    }

    public String getTekst() {
        return tekst;
    }

    public Date getDatum() {
        return datum;
    }

    public String getTypeUpdate() {
        return typeUpdate;
    }

    public int getGebruikerID() {
        return gebruikerID;
    }
}
