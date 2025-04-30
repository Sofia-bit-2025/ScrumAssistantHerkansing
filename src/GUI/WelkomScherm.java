package GUI;

import javax.swing.*;
import java.awt.*;

public class WelkomScherm extends JFrame {

    public WelkomScherm() {
        setTitle("Welkom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel hoofdPanel = new JPanel(new BorderLayout(10, 10));
        hoofdPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welkomLabel = new JLabel("Welkom bij Scrum Assistant", SwingConstants.CENTER);
        welkomLabel.setFont(new Font("Arial", Font.BOLD, 18));
        hoofdPanel.add(welkomLabel, BorderLayout.NORTH);

        JButton loginKnop = new JButton("Login");
        JButton nieuwAccountKnop = new JButton("Nieuw account");

        // Alleen correcte action listeners:
        loginKnop.addActionListener(e -> openLoginScherm());
        nieuwAccountKnop.addActionListener(e -> new RegistratieScherm());

        JPanel knopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        knopPanel.add(loginKnop);
        knopPanel.add(nieuwAccountKnop);

        hoofdPanel.add(knopPanel, BorderLayout.CENTER);
        add(hoofdPanel);
    }

    private void openLoginScherm() {
        new LoginScherm(); // Open een apart LoginScherm
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelkomScherm::new);
    }
}
