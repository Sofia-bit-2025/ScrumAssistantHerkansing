package GUI;

import Model.Gebruiker;
import Model.Thread;
import Service.ThreadService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DebatScherm extends JFrame {

    private Gebruiker gebruiker;
    private ThreadService threadService = new ThreadService();
    private JTextArea outputArea;

    public DebatScherm(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
        setTitle("Start een debat - Thread Overzicht");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titel = new JLabel("Actieve Debatten (Threads)", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton laadKnop = new JButton("Laad threads van vandaag");
        laadKnop.addActionListener(e -> laadThreadsVanVandaag());
        panel.add(laadKnop, BorderLayout.SOUTH);

        add(panel);
    }

    private void laadThreadsVanVandaag() {
        outputArea.setText("");
        try {
            // Je kunt hier kiezen op basis van Sprint, User Story, etc.
            List<Thread> threads = threadService.getThreadsByUserStory(gebruiker.getGebruikerID()); // voorbeeld
            if (threads.isEmpty()) {
                outputArea.append("Er zijn vandaag geen debatten gevonden voor jouw User Stories.\n");
            } else {
                for (Thread t : threads) {
                    outputArea.append(t.toString());
                    outputArea.append("\n-----------------------------\n");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fout bij ophalen van threads: " + e.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
        }
    }
}

