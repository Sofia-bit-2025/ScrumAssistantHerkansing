package Model;

import java.time.LocalDateTime;



public class Gebruiker {
    private int gebruikerID;
    private String voornaam;
    private String achternaam;
    private String email;
    private String rol;
    private LocalDateTime datumRegistratie;

    public Gebruiker(int gebruikerID, String voornaam, String achternaam, String email, String rol, LocalDateTime datumRegistratie) {
        this.gebruikerID = gebruikerID;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.email = email;
        this.rol = rol;
        this.datumRegistratie = datumRegistratie;
    }

    public int getGebruikerID() {
        return gebruikerID;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public LocalDateTime getDatumRegistratie() {
        return datumRegistratie;
    }

    @Override
    public String toString() {
        return voornaam + " " + achternaam + " (" + rol + "), geregistreerd op: " + datumRegistratie;
    }
}
