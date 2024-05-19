import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Prijava {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Prijava() {
        window = new JFrame("Prijava"); // Create a new window
        window.setPreferredSize(new Dimension(600, 400)); // Set window size
        window.setBounds(10, 10, 600, 400); // Set window position and size
        window.setLayout(new BorderLayout()); // Set window layout
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set window close operation
        window.setLocationRelativeTo(null); // Set window position to center
        window.setResizable(false); // Enable window resizing

        container = window.getContentPane(); // Create a new container
        container.setLayout(null); // Set panel layout

        // Set the same background color and font settings as EkipeForm
        container.setBackground(Color.LIGHT_GRAY); // Set background color

        mainTitle = new JLabel("Prijava"); // Create a new label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 36)); // Set font size and style
        mainTitle.setForeground(Color.DARK_GRAY); // Set text color
        mainTitle.setBounds(10, 20, 580, 50); // Set position and size
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER); // Set horizontal alignment
        container.add(mainTitle); // Add label to container

        emailLabel = new JLabel("Elektronski naslov"); // Create a new label
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        emailLabel.setBounds(50, 100, 200, 40); // Set position and size
        container.add(emailLabel); // Add label to container

        emailField = new JTextField(); // Create a new text field
        emailField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        emailField.setBounds(200, 100, 300, 40); // Set position and size
        container.add(emailField); // Add text field to container

        passwordLabel = new JLabel("Geslo:"); // Create a new label
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        passwordLabel.setBounds(50, 150, 200, 40); // Set position and size
        container.add(passwordLabel); // Add label to container

        passwordField = new JPasswordField(); // Create a new password field
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        passwordField.setBounds(200, 150, 300, 40); // Set position and size
        container.add(passwordField); // Add password field to container

        loginButton = new JButton("Prijavi se"); // Create a new button
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        loginButton.setForeground(Color.GREEN); // Set text color
        loginButton.setFocusPainted(false); // Disable focus painting
        loginButton.setBounds(200, 200, 150, 40); // Set position and size
        loginButton.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed();
            }
        }); // Add action listener to button
        container.add(loginButton); // Add button to container

        registerButton = new JButton("Pojdi na registracijo"); // Create a new button
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        registerButton.setBounds(200, 250, 250, 40); // Set position and size
        registerButton.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });  // Add action listener to button
        container.add(registerButton); // Add button to container
    }

    private void loginButtonActionPerformed() {
        String email = emailField.getText(); // Preberemo vrednost iz textfielda
        String password = new String(passwordField.getPassword()); // Preberemo vrednost iz textfielda
        try {
            Baza database = Baza.getInstance(); // Ustvarimo povezavo na bazo
            ResultSet result = database.executeQuery("SELECT * FROM \"Uporabniki\" WHERE email = '" + email + "' AND geslo = '" + password + "';"); // Izvedemo poizvedbo
            if (result.next()) { // Če je uporabnik najden
                JOptionPane.showMessageDialog(window, "Uspešno ste se prijavili!"); // Izpišemo sporočilo
                Shramba.getInstance().uporabnikId = result.getInt("id"); // Shranimo id uporabnika
                Shramba.getInstance().uporabnikEkipaId = result.getInt("ekipa_id") == 0 ? -1 : result.getInt("ekipa_id"); // Shranimo id ekipe
                Home home = new Home(); // Ustvarimo novo okno
                home.show(); // Pokažemo novo okno
                window.dispose(); // Zapremo trenutno okno
            } else { // Če uporabnik ni najden
                JOptionPane.showMessageDialog(window, "Napačen elektronski naslov ime ali geslo!"); // Izpišemo sporočilo
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Napaka pri prijavi!"); // Izpišemo sporočilo
        }
    }

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Registracija registracija = new Registracija(); // Ustvarimo novo okno
        registracija.show(); // Pokažemo novo okno
        window.dispose(); // Zapremo trenutno okno
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
