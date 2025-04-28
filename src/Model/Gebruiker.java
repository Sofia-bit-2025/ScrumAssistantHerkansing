package Model;
// deze klasse is bedoeld om een gebruiker voor te stellen
//gegevens van een gebruiker bij elkaar houden

public class Gebruiker {
    private int gebruikerID;
    private String naam;
    private String rol;

    // dit object bevat de gegevens van een gebruiker
    public Gebruiker(int gebruikerID, String naam, String rol) {
        this.gebruikerID = gebruikerID;
        this.naam = naam;
        this.rol = rol;
    }

    public int getGebruikerID() {
        return gebruikerID;
    }

    public String getNaam() {
        return naam;
    }

    public String getRol() {
        return rol;
    }
}