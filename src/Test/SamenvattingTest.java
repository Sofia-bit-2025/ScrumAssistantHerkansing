
//  berichten en threads ophalen en te bekijken via de console.
//Ophalen van alle samenvattingen van vandaag via SamenvattingService.
//Netjes tonen van samenvattingen per User Story.
//Testen en controleren of de SamenvattingService goed werkt.
package Test;
import Service.SamenvattingService;
import Model.Samenvatting;
import java.util.List;
import java.util.Scanner;


public class SamenvattingTest {

    public static void main(String[] args) {
        SamenvattingService samenvattingService = new SamenvattingService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Samenvatting Test Menu ===");
            System.out.println("1. Toon alle samenvattingen van vandaag");
            System.out.println("2. Stoppen");
            System.out.print("Maak een keuze: ");

            int keuze = scanner.nextInt();
            scanner.nextLine(); // buffer leegmaken

            switch (keuze) {
                case 1:
                    List<Samenvatting> samenvattingen = samenvattingService.generateDailySummary();
                    if (samenvattingen.isEmpty()) {
                        System.out.println("Er zijn vandaag geen samenvattingen beschikbaar.");
                    } else {
                        samenvattingService.printSamenvattingen(samenvattingen);
                    }
                    break;

                case 2:
                    System.out.println("Programma afgesloten.");
                    return;

                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
}
