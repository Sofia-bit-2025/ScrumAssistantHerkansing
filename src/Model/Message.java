package Model;

public class Message {
    private int berichtID;
    private String tekst;
    private String naamAfzender;
    private int gebruikerID;
    private int threadID;
    private int epicID;
    private int userStoryID;
    private int taakID;

    public Message(int berichtID, String tekst, String naamAfzender, int gebruikerID, int threadID, int epicID, int userStoryID, int taakID) {
        this.berichtID = berichtID;
        this.tekst = tekst;
        this.naamAfzender = naamAfzender;
        this.gebruikerID = gebruikerID;
        this.threadID = threadID;
        this.epicID = epicID;
        this.userStoryID = userStoryID;
        this.taakID = taakID;
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
}