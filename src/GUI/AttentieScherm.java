//Swing GUI klasse om alle notificaties in het systeem overzichtelijk weergeven
// overzicht van alle  meldingen inclusief wie wat heeft gedaan
// en waarop -> epic user story taak
package GUI;
import Model.Gebruiker;
import Service.NotificatieService;
import javax.swing.*;
import java.awt.*;
import java.util.List;



//een venster (JFrame) met 3 velden ->1-gebruiker 2-notificatieVeld 3-notificatieService
public class AttentieScherm extends JFrame {

    private final Gebruiker gebruiker;//voor wie de meldingen bedoeld is
    private final JTextArea notificatieVeld;//tekstvak waarin alle notificaties getoond worden.
    private final NotificatieService notificatieService;//verbinding met service



    //constructor
    public AttentieScherm(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        this.notificatieService = new NotificatieService();
        this.notificatieVeld = new JTextArea();

        configureFrame();//algemene vensterinstelling
        initComponents();// GUI onderdelen  aaanmaken en toevoegen aan het venster
        laadMeldingen();//meldingen ophalen

        setVisible(true);//het venster zichtbaar maken
    }


    //de basisinstellingen instellen
    private void configureFrame() {
        setTitle("Meldingen voor " + gebruiker.getVoornaam());
        setSize(600, 500);//de breedte en hoogte van het venster.
        setLocationRelativeTo(null);//Centreer het venster op het scherm
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//Sluit alleen dit venster  laat de rest draaien
    }



    //het opbouwen van UI
    //nieuw paneel
    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));//ruimte tussen componenten
        //een lege marge rondom hele paneel
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));




        // Titel
        JLabel titel = new JLabel("Alle Attenties en Notificaties", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titel, BorderLayout.NORTH);




        // Notificatieveld
        notificatieVeld.setEditable(false);
        notificatieVeld.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificatieVeld.setLineWrap(true);
        notificatieVeld.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notificatieVeld);
        panel.add(scrollPane, BorderLayout.CENTER);




        // Verversknop
        JButton verversKnop = new JButton("Ververs meldingen");
        verversKnop.addActionListener(e -> laadMeldingen());
        panel.add(verversKnop, BorderLayout.SOUTH);

        add(panel);
    }




    //alle meldingen ophalen uit de database
    //in het venster tonen
    //het nieuwste overzicht laten zien
    private void laadMeldingen() {
        List<String> meldingen = notificatieService.getAlleNotificatiesMetDetails();

        if (meldingen.isEmpty()) {
            notificatieVeld.setText("Er zijn momenteel geen notificaties.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String melding : meldingen) {
                sb.append(melding).append("\n\n");
            }
            notificatieVeld.setText(sb.toString());
            notificatieVeld.setCaretPosition(0); // Spring naar boven
        }
    }
}


