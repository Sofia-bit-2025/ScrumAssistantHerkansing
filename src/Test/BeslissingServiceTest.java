
// via de console  de werking van beslissingen te testen en controleren.
//Gebruiker kan een bericht als beslissing markeren.
//Gebruiker kan alle beslissingen zien voor een specifieke user story.
//Gebruiker kan alle beslissingen van vandaag bekijken.
//Eenvoudig testen en demonstreren van de BeslissingService
package Test;
import Service.BeslissingService;
import Model.Beslissing;
import java.util.List;
import java.util.Scanner;

//  beslissingen markeren en ophalen via de console.
public class BeslissingServiceTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BeslissingService service = new BeslissingService();

        while (true) {
            System.out.println("\n--- BESLISSING TEST MENU ---");
            System.out.println("1. Markeer een bericht als beslissing");
            System.out.println("2. Toon beslissingen voor een User Story");
            System.out.println("3. Toon beslissingen van vandaag");
            System.out.println("0. Afsluiten");
            System.out.print("Maak een keuze: ");
            String keuze = scanner.nextLine();

            switch (keuze) {
                case "1":
                    System.out.print("Voer het BerichtID in dat je wilt markeren als beslissing: ");
                    int berichtID = Integer.parseInt(scanner.nextLine());
                    service.markeerBerichtAlsBeslissing(berichtID);
                    break;

                case "2":
                    System.out.print("Voer het UserStoryID in: ");
                    int userStoryID = Integer.parseInt(scanner.nextLine());
                    List<Beslissing> beslissingen = service.getBeslissingenPerUserStory(userStoryID);
                    if (beslissingen.isEmpty()) {
                        System.out.println("Geen beslissingen gevonden.");
                    } else {
                        for (Beslissing b : beslissingen) {
                            System.out.println(b);
                        }
                    }
                    break;

                case "3":
                    List<Beslissing> vandaag = service.getAlleBeslissingenVandaag();
                    if (vandaag.isEmpty()) {
                        System.out.println("Vandaag zijn er geen beslissingen genomen.");
                    } else {
                        for (Beslissing b : vandaag) {
                            System.out.println(b);
                        }
                    }
                    break;

                case "0":
                    System.out.println("Test afgesloten.");
                    return;

                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
}
