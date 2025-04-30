//De klasse Taak stelt een concrete taak voor die gekoppeld is
// aan een specifieke User Story binnen een Scrum-project.
//Houdt gegevens vast over het ID, de titel, de beschrijving en de gekoppelde User Story van een taak.
//Geeft taakdetails gestructureerd terug via getters en een leesbare toString().
package Model;
public class Taak {
    private int taakID;
    private String titel;
    private String beschrijving;
    private int userStoryID;

    public Taak(int taakID, String titel, String beschrijving, int userStoryID) {
        this.taakID = taakID;
        this.titel = titel;
        this.beschrijving = beschrijving;
        this.userStoryID = userStoryID;
    }

    public int getTaakID() {
        return taakID;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public int getUserStoryID() {
        return userStoryID;
    }

    @Override
    public String toString() {
        return "Taak #" + taakID + " | " + titel + " (User Story ID: " + userStoryID + ")";
    }
}
