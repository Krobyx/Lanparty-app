import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KrajiForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JLabel postnaStevilkaLabel;
    private JTextField postnaStevilkaField;
    private JButton shraniButton;
    private int krajId;

    public KrajiForm(int krajId) {
        this.krajId = krajId;

        window = new JFrame("Kraji Obrazec"); // Create a new window
        window.setPreferredSize(new Dimension(600, 400)); // Set window size
        window.setBounds(10, 10, 600, 400); // Set window position and size
        window.setLayout(null); // Use null layout
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set action on close
        window.setLocationRelativeTo(null); // Center the window
        window.setResizable(false); // Disable resizing

        container = window.getContentPane(); // Create a new container
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY); // Set background color

        mainTitle = new JLabel("Kraji Obrazec"); // Create a new label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 36)); // Set font size and style
        mainTitle.setForeground(Color.DARK_GRAY); // Set text color
        mainTitle.setBounds(10, 20, 580, 50); // Set position and size
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER); // Set horizontal alignment
        container.add(mainTitle); // Add label to container

        imeLabel = new JLabel("Ime kraja:"); // Create a new label
        imeLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        imeLabel.setBounds(50, 100, 120, 40); // Set position and size
        container.add(imeLabel); // Add label to container

        imeField = new JTextField(); // Create a new text field
        imeField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        imeField.setBounds(180, 100, 300, 40); // Set position and size
        container.add(imeField); // Add text field to container

        postnaStevilkaLabel = new JLabel("Poštna številka:"); // Create a new label
        postnaStevilkaLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        postnaStevilkaLabel.setBounds(50, 150, 150, 40); // Set position and size
        container.add(postnaStevilkaLabel); // Add label to container

        postnaStevilkaField = new JTextField(); // Create a new text field
        postnaStevilkaField.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        postnaStevilkaField.setBounds(220, 150, 300, 40); // Set position and size
        container.add(postnaStevilkaField); // Add text field to container

        shraniButton = new JButton("Shrani"); // Create a new button
        shraniButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size and style
        shraniButton.setForeground(Color.GREEN); // Set text color
        shraniButton.setFocusPainted(false); // Disable focus border
        shraniButton.setBounds(250, 250, 100, 40); // Set position and size
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shraniKraj();
            }
        });
        container.add(shraniButton); // Add button to container

        window.pack();
        window.setVisible(true); // Make window visible

        if (krajId != 0) {
            try {
                Baza db = Baza.getInstance();
                String query = "SELECT * FROM \"Kraji\" WHERE id = " + krajId + ";";
                ResultSet resultSet = db.executeQuery(query);
                if (resultSet.next()) {
                    imeField.setText(resultSet.getString("ime"));
                    postnaStevilkaField.setText(resultSet.getString("postna_st"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void shraniKraj() {
        String ime = imeField.getText();
        String postnaStevilka = postnaStevilkaField.getText();

        if (ime.isEmpty() || postnaStevilka.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Baza db = Baza.getInstance();

            if (krajId <= 0) {
                String query = "SELECT insert_kraj('" + ime + "', '" + postnaStevilka + "');";
                db.executeQuery(query);
            } else {
                String query = "SELECT update_kraj(" + krajId + ", '" + ime + "', '" + postnaStevilka + "');";
                db.executeQuery(query);
            }
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri shranjevanju kraja.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
