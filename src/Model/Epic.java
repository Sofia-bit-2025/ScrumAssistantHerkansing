//De klasse Epic stelt een grote, overkoepelende functionaliteit binnen een project voor.
//Houdt de kerngegevens vast van een Epic: het unieke ID, de titel en de beschrijving.
//Biedt toegang tot deze eigenschappen en maakt eenvoudige weergave mogelijk via toString().

package Model;
public class Epic {
    private int epicID;
    private String titel;
    private String beschrijving;

    public Epic(int epicID, String titel, String beschrijving) {
        this.epicID = epicID;
        this.titel = titel;
        this.beschrijving = beschrijving;
    }

    public int getEpicID() {
        return epicID;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    @Override
    public String toString() {
        return "Epic #" + epicID + " | " + titel + " - " + beschrijving;
    }
}
