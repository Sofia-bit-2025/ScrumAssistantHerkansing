//De klasse Samenvatting vertegenwoordigt een dagelijkse verzameling van berichten
// en threads die gekoppeld zijn aan één specifieke User Story.
//Houdt gegevens bij over de User Story, alle verzamelde berichten,
// threads en de datum van de samenvatting.
//Biedt gestructureerde toegang tot deze informatie
// om bijvoorbeeld rapportages of dagoverzichten te maken.
package Model;
import java.time.LocalDate;
import java.util.List;
//een dagoverzicht  van communicatie die hoort bij een specifieke User Story
//wordt gebruikt voor het structureren en opslaan van informatie
//Een gestructureerde representatie maken van:
//welke berichten en threads er op een dag zijn verzameld,
//bij welke User Story dit hoort,
//en op welke datum dit gebeurde.


public class Samenvatting {
    private int samenvattingID;
    private int userStoryID;
    private String userStoryTitel;
    private List<String> berichten;
    private List<String> threads;
    private LocalDate datum;

    public Samenvatting(int samenvattingID, int userStoryID, String userStoryTitel,
                        List<String> berichten, List<String> threads, LocalDate datum) {
        this.samenvattingID = samenvattingID;
        this.userStoryID = userStoryID;
        this.userStoryTitel = userStoryTitel;
        this.berichten = berichten;
        this.threads = threads;
        this.datum = datum;
    }

    public int getSamenvattingID() {
        return samenvattingID;
    }

    public int getUserStoryID() {
        return userStoryID;
    }

    public String getUserStoryTitel() {
        return userStoryTitel;
    }

    public List<String> getBerichten() {
        return berichten;
    }

    public List<String> getThreads() {
        return threads;
    }

    public LocalDate getDatum() {
        return datum;
    }
}
