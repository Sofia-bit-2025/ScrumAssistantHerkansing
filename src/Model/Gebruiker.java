package Model;

public class Gebruiker {
    private int gebruikerID;
    private String naam;
    private String rol;

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