package GUI;

import Model.Gebruiker;
import Model.Message;
import Model.Thread;
import Model.DatabaseConnector;
import Service.MessageService;
import Service.ThreadService;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class FilterBerichtGUI extends JFrame {

    private final JTextArea outputVeld = new JTextArea();
    private final JTextField invoerVeld = new JTextField();
    private final ThreadService threadService = new ThreadService();
    private final MessageService messageService = new MessageService();

    public FilterBerichtGUI(Gebruiker gebruiker) {
        setTitle("Filter berichten per User Story");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initUI();
        setVisible(true);
    }

    private void initUI() {
        // Zoekveld + knop
        JButton zoekKnop = new JButton("Zoek");
        zoekKnop.addActionListener(e -> zoek());

        JPanel invoerPanel = new JPanel(new BorderLayout(5, 5));
        invoerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        invoerPanel.add(new JLabel("User Story ID:"), BorderLayout.WEST);
        invoerPanel.add(invoerVeld, BorderLayout.CENTER);
        invoerPanel.add(zoekKnop, BorderLayout.EAST);

        // Output
        outputVeld.setEditable(false);
        outputVeld.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputVeld);

        add(invoerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void zoek() {
        outputVeld.setText(""); // Leegmaken

        int userStoryID;
        try {
            userStoryID = Integer.parseInt(invoerVeld.getText().trim());
        } catch (NumberFormatException e) {
            outputVeld.setText("Ongeldig ID. Voer een getal in.");
            return;
        }

        outputVeld.append("User Story ID: " + userStoryID + "\n\n");
        outputVeld.append(getUserStoryInfo(userStoryID));

        try {
            // Threads ophalen
            outputVeld.append("--- Threads ---\n");
            List<Thread> threads = threadService.getThreadsByUserStory(userStoryID);
            if (threads.isEmpty()) {
                outputVeld.append("Geen threads gevonden.\n");
            } else {
                for (Thread thread : threads) {
                    outputVeld.append(String.format("[#%d] %s (%s)\n",
                            thread.getThreadID(), thread.getTitel(), thread.getDatum()));
                }
            }

            // Berichten ophalen
            outputVeld.append("\n--- Berichten ---\n");
            List<Message> berichten = messageService.getMessagesByUserStoryIDs(List.of(userStoryID));
            if (berichten.isEmpty()) {
                outputVeld.append("Geen berichten gevonden.\n");
            } else {
                for (Message msg : berichten) {
                    outputVeld.append(String.format("[%s] %s\n", msg.getDatum(), msg.getTekst()));
                }
            }

        } catch (Exception e) {
            outputVeld.append("Fout bij ophalen van data: " + e.getMessage());
        }
    }

    private String getUserStoryInfo(int userStoryID) {
        StringBuilder info = new StringBuilder();
        String query = """
            SELECT US.Titel AS UserStoryTitel, US.Beschrijving, E.Titel AS EpicTitel
            FROM UserStory US
            LEFT JOIN Epic E ON US.EpicID = E.EpicID
            WHERE US.UserStoryID = ?
        """;

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userStoryID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                info.append("User Story: ").append(rs.getString("UserStoryTitel")).append("\n");
                info.append("Beschrijving: ").append(rs.getString("Beschrijving")).append("\n");
                info.append("hoort bij Epic: ").append(rs.getString("EpicTitel")).append("\n\n");
            } else {
                info.append("User Story niet gevonden.\n\n");
            }

        } catch (SQLException e) {
            info.append("Fout bij ophalen van User Story info: ").append(e.getMessage()).append("\n\n");
        }

        return info.toString();
    }
}
