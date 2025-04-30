//Test klasse voor notificatie
//handmatig notificaties toe te voegen en op te vragen
//checken of het notificatiesysteem correct werkt
package Test;
import Model.Notificatie;
import Model.Notificatie.TypeUpdate;
import Service.NotificatieService;
import java.util.List;
import java.util.Scanner;



//een menu om handmatig notificaties toe te voegen of op te vragen
//zonder een GUI
public class NotificatieTest {

    public static void main(String[] args) {
        NotificatieService notificatieService = new NotificatieService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n== Notificatie Test Menu ==");
            System.out.println("1. Nieuwe notificatie toevoegen");
            System.out.println("2. Notificaties ophalen voor gebruiker");
            System.out.println("3. Stoppen");
            System.out.print("Maak een keuze: ");

            int keuze = leesInt(scanner);

            switch (keuze) {
                case 1 -> voegNotificatieToe(notificatieService, scanner);
                case 2 -> toonNotificatiesVoorGebruiker(notificatieService, scanner);
                case 3 -> {
                    System.out.println("Testprogramma afgesloten.");
                    return;
                }
                default -> System.out.println(" Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }



    //de gebruiker laten handmatig een nieuwe notificatie toevoegen
    private static void voegNotificatieToe(NotificatieService service, Scanner scanner) {
        System.out.print("Voer notificatietekst in: ");
        String tekst = scanner.nextLine();

        System.out.println("Kies type update:");
        for (TypeUpdate type : TypeUpdate.values()) {
            System.out.println("- " + type);
        }

        TypeUpdate gekozenType;
        while (true) {
            System.out.print("TypeUpdate: ");
            String invoer = scanner.nextLine().trim().toUpperCase();
            try {
                gekozenType = TypeUpdate.valueOf(invoer);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ongeldig type. Kies uit de bovenstaande lijst.");
            }
        }



        //gegevens van de gebruiker op te vragen
        System.out.print("GebruikerID: ");
        int gebruikerID = leesInt(scanner);

        System.out.print("UserStoryID (of 0 als niet van toepassing): ");
        Integer userStoryID = optioneelInt(scanner);

        System.out.print("EpicID (of 0 als niet van toepassing): ");
        Integer epicID = optioneelInt(scanner);

        System.out.print("TaakID (of 0 als niet van toepassing): ");
        Integer taakID = optioneelInt(scanner);

        service.createNotificatie(tekst, gekozenType, gebruikerID, userStoryID, epicID, taakID);
    }




    //alle notificaties ophalen voor gebruiker
    private static void toonNotificatiesVoorGebruiker(NotificatieService service, Scanner scanner) {
        System.out.print("Voer gebruikerID in: ");
        int gebruikerID = leesInt(scanner);

        List<Notificatie> notificaties = service.getNotificatiesVoorGebruiker(gebruikerID);

        if (notificaties.isEmpty()) {
            System.out.println("Geen notificaties gevonden voor gebruikerID: " + gebruikerID);
        } else {
            System.out.println("\n📢 Notificaties voor gebruiker " + gebruikerID + ":");
            for (Notificatie n : notificaties) {
                System.out.println(n);
            }
        }
    }




    //controle een geldig geheel getal invoeren (als gebruiker)
    private static int leesInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Voer een geldig getal in: ");
            }
        }
    }
    //nul 0 gezien wordt als niet van toepassing
    private static Integer optioneelInt(Scanner scanner) {
        int waarde = leesInt(scanner);
        return waarde == 0 ? null : waarde;
    }
}
