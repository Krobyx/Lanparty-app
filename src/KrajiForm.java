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

        window = new JFrame("Kraji Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Kraji Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime kraja:"); // Ustvarimo nov label
        imeLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        postnaStevilkaLabel = new JLabel("Poštna številka:"); // Ustvarimo nov label
        postnaStevilkaLabel.setBounds(10, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(postnaStevilkaLabel); // Dodamo label v container

        postnaStevilkaField = new JTextField(); // Ustvarimo nov textfield
        postnaStevilkaField.setBounds(220, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(postnaStevilkaField); // Dodamo textfield v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setBounds(10, 450, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shraniKraj();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno

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
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
