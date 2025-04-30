//De klasse Thread stelt een discussie of gesprek voor
// dat gekoppeld kan zijn aan een Epic, User Story of Taak.
//Houdt gegevens vast over het ID, de titel, de status (actief/inactief),
// de datum van aanmaak, de maker, de koppelingen (Epic, User Story, Taak)
// en een eventueel gemarkeerd juist antwoord.
//Geeft een volledige beschrijving van een thread via getters en een nette toString().
package Model;
import java.time.LocalDate;

public class Thread {
    private int threadID;
    private String titel;
    private LocalDate datum;
    private boolean status;
    private int epicID;
    private int userStoryID;
    private int taakID;
    private int makerID;
    private int juisteAntwoordID;

    public Thread(int threadID, String titel, LocalDate datum, boolean status,
                  int epicID, int userStoryID, int taakID, int makerID, int juisteAntwoordID) {
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

    public int getThreadID() {
        return threadID;
    }

    public String getTitel() {
        return titel;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public boolean isActief() {
        return status;
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

    public int getMakerID() {
        return makerID;
    }

    public int getJuisteAntwoordID() {
        return juisteAntwoordID;
    }

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
