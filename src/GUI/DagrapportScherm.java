package GUI;

import Model.Samenvatting;
import Service.SamenvattingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DagrapportScherm extends JFrame {

    public DagrapportScherm() {
        setTitle("Dagrapport - Samenvattingen");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea rapportVeld = new JTextArea();
        rapportVeld.setEditable(false);
        rapportVeld.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(rapportVeld);
        add(scrollPane);

        SamenvattingService service = new SamenvattingService();
        // Alle samenvattingen ophalen, niet alleen van vandaag
        List<Samenvatting> samenvattingen = service.generateSummaryAllDates();
        System.out.println("Aantal samenvattingen gevonden: " + samenvattingen.size());

        StringBuilder rapportText = new StringBuilder();

        if (samenvattingen.isEmpty()) {
            rapportText.append("Er zijn nog geen samenvattingen beschikbaar.\n");
        } else {
            for (Samenvatting s : samenvattingen) {
                rapportText.append("User Story: ").append(s.getUserStoryTitel())
                        .append(" (ID: ").append(s.getUserStoryID()).append(")\n");
                rapportText.append("Datum: ").append(s.getDatum()).append("\n\n");

                rapportText.append("➤ Berichten:\n");
                for (String b : s.getBerichten()) {
                    rapportText.append("   - ").append(b).append("\n");
                }

                rapportText.append("\n➤ Threads:\n");
                for (String t : s.getThreads()) {
                    rapportText.append("   - ").append(t).append("\n");
                }

                rapportText.append("\n-----------------------------\n\n");
            }
        }

        rapportVeld.setText(rapportText.toString());
        setVisible(true);
    }
}
