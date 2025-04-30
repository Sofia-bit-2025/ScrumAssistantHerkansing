package GUI;

import Model.Gebruiker;

import javax.swing.*;
import java.awt.*;

public class HoofdMenuScherm extends JFrame {

    public HoofdMenuScherm(Gebruiker gebruiker) {
        setTitle("Hoofdmenu - Welkom " + gebruiker.getVoornaam());
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents(gebruiker);
        setVisible(true);
    }

    private void initComponents(Gebruiker gebruiker) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welkomLabel = new JLabel("Welkom, " + gebruiker.getVoornaam() + "!", SwingConstants.CENTER);
        welkomLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(welkomLabel, BorderLayout.NORTH);

        JPanel knopPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        String[] knoppen = {
                "Beslisbord",
                "Dagrapport",
                "Attentie",
                "TeamTalk",
                "Start een debat",
                "Epic - User Story - Taak",
                "Filter berichten per User Story"
        };

        for (String label : knoppen) {
            JButton knop = new JButton(label);
            knop.setPreferredSize(new Dimension(300, 40));

            knop.addActionListener(e -> {
                switch (label) {
                    case "Dagrapport":
                        new DagrapportScherm();
                        break;
                    case "Beslisbord":
                        new BeslisbordScherm();
                        break;
                    case "Attentie":
                        new AttentieScherm(gebruiker);
                        break;
                    case "TeamTalk":
                        new TeamTalkScherm(gebruiker);
                        break;
                    case "Start een debat":
                        new DebatScherm(gebruiker);
                        break;
                    case "Epic - User Story - Taak":
                        new StructuurScherm();
                        break;
                    case "Filter berichten per User Story":
                        new FilterBerichtGUI(gebruiker); // nieuw scherm
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Functie: " + label + " is (nog) niet geïmplementeerd.");
                        break;
                }
            });

            knopPanel.add(knop);
        }

        panel.add(knopPanel, BorderLayout.CENTER);
        add(panel);
    }
}
