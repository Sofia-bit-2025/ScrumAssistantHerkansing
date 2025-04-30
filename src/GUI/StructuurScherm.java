package GUI;

import Model.Epic;
import Model.UserStory;
import Model.Taak;
import Service.EpicService;
import Service.UserStoryService;
import Service.TaakService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StructuurScherm extends JFrame {

    private JComboBox<Epic> epicCombo;
    private JComboBox<UserStory> userStoryCombo;
    private JTextArea outputGebied;

    public StructuurScherm() {
        setTitle("Structuur: Epic - User Story - Taak");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel hoofdPanel = new JPanel(new BorderLayout(10, 10));
        hoofdPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: selecties
        JPanel selectiePanel = new JPanel(new GridLayout(2, 2, 10, 10));

        selectiePanel.add(new JLabel("Kies Epic:"));
        epicCombo = new JComboBox<>();
        selectiePanel.add(epicCombo);

        selectiePanel.add(new JLabel("Kies User Story:"));
        userStoryCombo = new JComboBox<>();
        selectiePanel.add(userStoryCombo);

        hoofdPanel.add(selectiePanel, BorderLayout.NORTH);

        // Midden: output
        outputGebied = new JTextArea();
        outputGebied.setEditable(false);
        hoofdPanel.add(new JScrollPane(outputGebied), BorderLayout.CENTER);

        // Laad epics
        try {
            EpicService epicService = new EpicService();
            List<Epic> epics = epicService.getAllEpics();
            for (Epic e : epics) {
                epicCombo.addItem(e);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fout bij laden van epics: " + e.getMessage());
        }

        epicCombo.addActionListener(e -> laadUserStories());
        userStoryCombo.addActionListener(e -> laadTaken());

        add(hoofdPanel);
    }

    private void laadUserStories() {
        userStoryCombo.removeAllItems();
        Epic geselecteerdEpic = (Epic) epicCombo.getSelectedItem();
        if (geselecteerdEpic == null) return;

        try {
            UserStoryService usService = new UserStoryService();
            List<UserStory> stories = usService.getUserStoriesByEpic(geselecteerdEpic.getEpicID());
            for (UserStory us : stories) {
                userStoryCombo.addItem(us);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fout bij laden van user stories: " + e.getMessage());
        }
    }

    private void laadTaken() {
        outputGebied.setText("");
        UserStory geselecteerdeUS = (UserStory) userStoryCombo.getSelectedItem();
        if (geselecteerdeUS == null) return;

        try {
            TaakService taakService = new TaakService();
            List<Taak> taken = taakService.getTakenByUserStory(geselecteerdeUS.getUserStoryID());

            outputGebied.append("Taken gekoppeld aan: " + geselecteerdeUS.getTitel() + "\n\n");
            for (Taak taak : taken) {
                outputGebied.append(taak + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fout bij laden van taken: " + e.getMessage());
        }
    }
}
