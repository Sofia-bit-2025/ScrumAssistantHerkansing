// stelt een gebruikersverhaal voor binnen een Epic.
//Houdt gegevens vast over de unieke ID, de titel, de beschrijving en de koppeling naar een Epic.
//Biedt toegang tot deze gegevens via getters en geeft een nette beschrijving via toString().
package Model;
public class UserStory {
    private int userStoryID;
    private String titel;
    private String beschrijving;
    private int epicID;

    public UserStory(int userStoryID, String titel, String beschrijving, int epicID) {
        this.userStoryID = userStoryID;
        this.titel = titel;
        this.beschrijving = beschrijving;
        this.epicID = epicID;
    }

    public int getUserStoryID() {
        return userStoryID;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "User Story #" + userStoryID + " | " + titel + " (Epic ID: " + epicID + ")";
    }
}
