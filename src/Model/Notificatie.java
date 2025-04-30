//De Notificatie klasse zorgt ervoor dat er een melding wordt aangemaakt
// zodra er iets belangrijks gebeurt in het systeem.
// bijv. een update aan een taak, een user story of een epic.
// notificatie laat zien wat er is gebeurd, wanneer het gebeurde,
// wie het heeft gedaan, en waarmee het te maken heeft.
//  op die manier blijf je als gebruiker altijd op de hoogte van de laatste wijzigingen.

package Model;
import java.time.LocalDate;
import java.util.Objects;

//constructor zonder koppelingen
//dus ook zonder dat je het aan een taak, epic of user story moet koppellen.
public class Notificatie {
    //vaste lijst van keuzes
    //wat voor soort update een notificatie heeft
    //herkennen wat de inhoud of context van een notificatie is
    public enum TypeUpdate {
        EPIC_UPDATE,
        TAAK,
        NIEUW_BERICHT,
        BESLISSING,
        THREAD_UPDATE,
        SPRINT_UPDATE,
        REMINDER,
        TEAM_UPDATE,//iemand is toegevoegd
        SYSTEEM_UPDATE,
        USERSTORY_UPDATE,
        BEVEILIGING,
        DEV_UPDATE,//technische updates
        UI_FEEDBACK,//over de gebruikersinterface
        UX_VERBETERING
    }




    private final int notificatieID;//notificaties individueel te herkennen
    private String tekst;//De inhoud
    private LocalDate datum;
    private TypeUpdate typeUpdate;//Soort update
    private int gebruikerID;//Wie heeft de notificatie geplaast
    private Integer userStoryID;//Koppeling naar een specifieke user story.
    private Integer epicID;//Koppeling naar een epic.
    private Integer taakID;//Koppeling naar een taak.





   //constructor zonder koppelingen aan een specifieke user story, epic of taak.
    //een algemene notificatie aanmaken.
    public Notificatie(int notificatieID, String tekst, LocalDate datum, TypeUpdate typeUpdate, int gebruikerID) {
        this(notificatieID, tekst, datum, typeUpdate, gebruikerID, null, null, null);
    }





//de volledige constructor van de Notificatie klasse
    //een specifieke update registreren
    public Notificatie(int notificatieID, String tekst, LocalDate datum, TypeUpdate typeUpdate, int gebruikerID,
                       Integer userStoryID, Integer epicID, Integer taakID) {
        if (tekst == null || tekst.isBlank()) throw new IllegalArgumentException("Tekst mag niet leeg zijn");
        if (datum == null) throw new IllegalArgumentException("Datum mag niet null zijn");
        if (typeUpdate == null) throw new IllegalArgumentException("TypeUpdate mag niet null zijn");

        this.notificatieID = notificatieID;
        this.tekst = tekst;
        this.datum = datum;
        this.typeUpdate = typeUpdate;
        this.gebruikerID = gebruikerID;
        this.userStoryID = userStoryID;
        this.epicID = epicID;
        this.taakID = taakID;
    }





    // getters

    public int getNotificatieID() {
        return notificatieID;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public TypeUpdate getTypeUpdate() {
        return typeUpdate;
    }

    public int getGebruikerID() {
        return gebruikerID;
    }

    public Integer getUserStoryID() {
        return userStoryID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public Integer getTaakID() {
        return taakID;
    }





    // Setters

    public void setTekst(String tekst) {
        if (tekst == null || tekst.isBlank()) throw new IllegalArgumentException("Tekst mag niet leeg zijn");
        this.tekst = tekst;
    }

    public void setDatum(LocalDate datum) {
        if (datum == null) throw new IllegalArgumentException("Datum mag niet null zijn");
        this.datum = datum;
    }

    public void setTypeUpdate(TypeUpdate typeUpdate) {
        if (typeUpdate == null) throw new IllegalArgumentException("TypeUpdate mag niet null zijn");
        this.typeUpdate = typeUpdate;
    }

    public void setGebruikerID(int gebruikerID) {
        this.gebruikerID = gebruikerID;
    }

    public void setUserStoryID(Integer userStoryID) {
        this.userStoryID = userStoryID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }

    public void setTaakID(Integer taakID) {
        this.taakID = taakID;
    }






//hoe een notificatie eruitziet
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", datum, typeUpdate, tekst);
    }



//vergelijkt of twee notificatie objecten hetzelfde zijn, op basis van hun notificatie id
    //alleen het id telt.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificatie)) return false;
        Notificatie that = (Notificatie) o;
        return notificatieID == that.notificatieID;
    }



    //om notificatie objecten correct te gebruiken in hash-based collecties
    // twee objecten die als gelijk worden beschouwd via equals ook dezelfde hash hebben.
    @Override
    public int hashCode() {
        return Objects.hash(notificatieID);
    }
}
