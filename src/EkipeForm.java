import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EkipeForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JButton shraniButton;
    private int ekipaId;

    public EkipeForm(int ekipaId) {
        this.ekipaId = ekipaId;

        window = new JFrame("Ekipe Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(600, 400)); // Nastavimo velikost okna
        window.setBounds(10, 10, 600, 400); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Onemogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja

        mainTitle = new JLabel("Ekipe Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 36)); // Nastavimo velikost in obliko pisave
        mainTitle.setForeground(Color.DARK_GRAY); // Nastavitev barve besedila
        mainTitle.setBounds(10, 20, 580, 50); // Nastavimo pozicijo in velikost
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER); // Nastavimo poravnavo besedila
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime ekipe:"); // Ustvarimo nov label
        imeLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Nastavimo velikost in obliko pisave
        imeLabel.setBounds(50, 100, 120, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setFont(new Font("Arial", Font.PLAIN, 18)); // Nastavimo velikost in obliko pisave
        imeField.setBounds(180, 100, 300, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Nastavimo velikost in obliko pisave
        shraniButton.setForeground(Color.GREEN); // Nastavimo barvo besedila
        shraniButton.setFocusPainted(false); // Onemogočimo obrobo fokusa
        shraniButton.setBounds(250, 200, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shraniEkipo();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.pack();
        window.setVisible(true); // Naredimo okno vidno

        if (ekipaId != 0) {
            try {
                Baza db = Baza.getInstance();
                String query = "SELECT * FROM \"Ekipe\" WHERE id = " + ekipaId + ";";
                ResultSet resultSet = db.executeQuery(query);
                if (resultSet.next()) {
                    imeField.setText(resultSet.getString("ime"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void shraniEkipo() {
        String ime = imeField.getText();

        if (ime.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Baza db = Baza.getInstance();

            if (ekipaId <= 0) {
                String query = "SELECT insert_ekipa('" + ime + "');";
                db.executeQuery(query);
            } else {
                String query = "SELECT update_ekipa(" + ekipaId + ", '" + ime + "');";
                db.executeQuery(query);
            }
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri shranjevanju ekipe.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
