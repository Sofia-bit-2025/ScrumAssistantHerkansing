package GUI;

import Model.Authenticator;

import javax.swing.*;
import java.awt.*;

public class RegistratieScherm extends JFrame {
    private JTextField voornaamVeld;
    private JTextField achternaamVeld;
    private JTextField emailVeld;
    private JComboBox<String> rolVeld;
    private JButton registreerKnop;

    public RegistratieScherm() {
        setTitle("Nieuw Account Registreren");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Alleen dit venster sluiten
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(new JLabel("Voornaam:"));
        voornaamVeld = new JTextField();
        panel.add(voornaamVeld);

        panel.add(new JLabel("Achternaam:"));
        achternaamVeld = new JTextField();
        panel.add(achternaamVeld);

        panel.add(new JLabel("E-mail:"));
        emailVeld = new JTextField();
        panel.add(emailVeld);

        panel.add(new JLabel("Rol:"));
        rolVeld = new JComboBox<>(new String[] {
                "Scrum Master", "Developer", "Tester", "Product Owner", "UX Designer"
        });
        panel.add(rolVeld);

        registreerKnop = new JButton("Registreer");
        registreerKnop.addActionListener(e -> registreerGebruiker());
        panel.add(new JLabel()); // lege plek
        panel.add(registreerKnop);

        add(panel);
    }

    private void registreerGebruiker() {
        String voornaam = voornaamVeld.getText().trim();
        String achternaam = achternaamVeld.getText().trim();
        String email = emailVeld.getText().trim();
        String rol = (String) rolVeld.getSelectedItem();

        if (voornaam.isEmpty() || achternaam.isEmpty() || email.isEmpty() || rol == null) {
            JOptionPane.showMessageDialog(this, "Vul alle velden in.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Authenticator authenticator = new Authenticator();
            authenticator.register(voornaam, achternaam, email, rol);
            JOptionPane.showMessageDialog(this, "Registratie succesvol!");
            dispose(); // Sluit registratievenster na registratie
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fout bij registratie: " + ex.getMessage(), "Fout", JOptionPane.ERROR_MESSAGE);
        }


    }


}
