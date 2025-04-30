package GUI;

import Model.Beslissing;
import Service.BeslissingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BeslisbordScherm extends JFrame {

    private JTextArea beslissingenVeld;
    private JTextField userStoryIDVeld;

    public BeslisbordScherm() {
        setTitle("Beslisbord - Vandaag genomen beslissingen");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel hoofdPanel = new JPanel(new BorderLayout(10, 10));
        hoofdPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titel
        JLabel titelLabel = new JLabel("Beslissingen van vandaag", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        hoofdPanel.add(titelLabel, BorderLayout.NORTH);

        // Tekstveld met beslissingen
        beslissingenVeld = new JTextArea();
        beslissingenVeld.setEditable(false);
        beslissingenVeld.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(beslissingenVeld);
        hoofdPanel.add(scrollPane, BorderLayout.CENTER);

        // Onderaan: filter per user story
        JPanel onderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        onderPanel.add(new JLabel("Filter op User Story ID:"));
        userStoryIDVeld = new JTextField(5);
        onderPanel.add(userStoryIDVeld);

        JButton filterKnop = new JButton("Toon");
        filterKnop.addActionListener(e -> toonBeslissingenVoorUserStory());
        onderPanel.add(filterKnop);

        JButton allesKnop = new JButton("Toon alles van vandaag");
        allesKnop.addActionListener(e -> toonAlleBeslissingenVandaag());
        onderPanel.add(allesKnop);

        hoofdPanel.add(onderPanel, BorderLayout.SOUTH);

        add(hoofdPanel);

        // Laad standaard de beslissingen van vandaag
        toonAlleBeslissingenVandaag();
    }

    private void toonAlleBeslissingenVandaag() {
        BeslissingService service = new BeslissingService();
        List<Beslissing> beslissingen = service.getAlleBeslissingenVandaag();
        vulBeslissingVeld(beslissingen);
    }

    private void toonBeslissingenVoorUserStory() {
        try {
            int id = Integer.parseInt(userStoryIDVeld.getText().trim());
            BeslissingService service = new BeslissingService();
            List<Beslissing> beslissingen = service.getBeslissingenPerUserStory(id);
            vulBeslissingVeld(beslissingen);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Voer een geldig User Story ID in.", "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vulBeslissingVeld(List<Beslissing> beslissingen) {
        beslissingenVeld.setText("");
        if (beslissingen.isEmpty()) {
            beslissingenVeld.setText("Geen beslissingen gevonden.");
        } else {
            for (Beslissing b : beslissingen) {
                beslissingenVeld.append(b.toString() + "\n\n");
            }
        }
    }
}
