package GUI;

import Model.Authenticator;
import Model.Gebruiker;

import javax.swing.*;
import java.awt.*;

public class LoginScherm extends JFrame {

    private JTextField voornaamVeld;
    private JTextField achternaamVeld;
    private JTextField emailVeld;
    private JComboBox<String> rolBox;

    public LoginScherm() {
        setTitle("Login");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        rolBox = new JComboBox<>(new String[]{"Scrum Master", "Developer", "Product Owner", "Tester", "UX Designer"});
        panel.add(rolBox);

        JButton loginKnop = new JButton("Login");
        panel.add(loginKnop);

        loginKnop.addActionListener(e -> login());

        add(panel);
    }

    private void login() {
        String voornaam = voornaamVeld.getText().trim();
        String achternaam = achternaamVeld.getText().trim();
        String email = emailVeld.getText().trim();
        String rol = (String) rolBox.getSelectedItem();

        if (voornaam.isEmpty() || achternaam.isEmpty() || email.isEmpty() || rol == null) {
            JOptionPane.showMessageDialog(this, "Vul alle velden in.", "Fout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (java.sql.Connection conn = Model.DatabaseConnector.connect()) {
            System.out.println("Verbinding succesvol! Je bent verbonden met: " + conn.getCatalog());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verbindingsfout: " + e.getMessage(), "Database", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Authenticator auth = new Authenticator();
            Gebruiker gebruiker = auth.loginMetEmail(voornaam, achternaam, email, rol);
            JOptionPane.showMessageDialog(this, "Welkom " + gebruiker.getVoornaam() + " (" + gebruiker.getRol() + ")");
            dispose();
            new HoofdMenuScherm(gebruiker);
            dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Fout: " + ex.getMessage(), "Login mislukt", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScherm::new);
    }
}
