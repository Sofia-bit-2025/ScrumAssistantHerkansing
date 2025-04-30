package GUI;

import Model.Gebruiker;
import Service.MessageService;

import javax.swing.*;
import java.awt.*;

public class TeamTalkScherm extends JFrame {

    private JTextArea tekstVeld;
    private JButton verzendKnop;
    private Gebruiker gebruiker;

    public TeamTalkScherm(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;

        setTitle("TeamTalk - Stuur een bericht");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel hoofdPanel = new JPanel(new BorderLayout(10, 10));
        hoofdPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel instructieLabel = new JLabel("Typ je bericht aan het team:");
        hoofdPanel.add(instructieLabel, BorderLayout.NORTH);

        tekstVeld = new JTextArea();
        tekstVeld.setLineWrap(true);
        tekstVeld.setWrapStyleWord(true);
        hoofdPanel.add(new JScrollPane(tekstVeld), BorderLayout.CENTER);

        verzendKnop = new JButton("Verzend");
        verzendKnop.addActionListener(e -> verzendBericht());
        hoofdPanel.add(verzendKnop, BorderLayout.SOUTH);

        add(hoofdPanel);
    }

    private void verzendBericht() {
        String tekst = tekstVeld.getText().trim();
        if (tekst.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Voer een bericht in voordat je verzendt.", "Leeg bericht", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MessageService service = new MessageService();
        int gebruikerID = gebruiker.getGebruikerID();

        // Hier wordt het bericht verzonden zonder specifieke Epic/UserStory/Taak koppeling
        service.sendMessage(gebruikerID, 1, tekst, 0, 0, 0);

        JOptionPane.showMessageDialog(this, "Bericht verzonden!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        tekstVeld.setText("");
    }
}
