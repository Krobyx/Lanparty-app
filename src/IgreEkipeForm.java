import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SimpleTimeZone;

public class IgreEkipeForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JButton addButton;
    private int ekipaId;

    public IgreEkipeForm(int ekipaId) {
        this.ekipaId = ekipaId;
        System.out.println(ekipaId);

        window = new JFrame("Dodaj igro k ekipi"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Dodaj igro k ekipi"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime igre:"); // Ustvarimo nov label
        imeLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        addButton = new JButton("Dodaj"); // Ustvarimo nov gumb
        addButton.setBounds(10, 450, 100, 40); // Nastavimo pozicijo in velikost
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dodajIgro();
            }
        });
        container.add(addButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno
    }

    private void dodajIgro() {
        String ime = imeField.getText();

        if (ime.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Baza db = Baza.getInstance();

            ResultSet rs = db.executeQuery("SELECT id FROM \"Igre\" WHERE ime = '" + ime + "';");

            if (rs.next()) {
                int igraId = rs.getInt("id");

                if (igraId < 1) {
                    JOptionPane.showMessageDialog(null, "Igra ne obstaja.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                db.executeQuery("SELECT dodaj_igro_ekipi(" + ekipaId + ", " + igraId + ");");
            } else {
                JOptionPane.showMessageDialog(null, "Igra ne obstaja.", "Napaka", JOptionPane.ERROR_MESSAGE);
                return;
            }

            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri dodajanju igre. (Preverite, če igra že obstaja)", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
