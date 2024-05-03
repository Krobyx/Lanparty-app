import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IgreForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JTextField imeField;
    private JLabel opisLabel;
    private JTextField opisField;
    private JButton shraniButton;
    private int igraId;

    public IgreForm(int igraId) {
        this.igraId = igraId;

        window = new JFrame("Igre Obrazec"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Igre Obrazec"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        imeLabel = new JLabel("Ime igre:"); // Ustvarimo nov label
        imeLabel.setBounds(10, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeLabel); // Dodamo label v container

        imeField = new JTextField(); // Ustvarimo nov textfield
        imeField.setBounds(220, 150, 200, 40); // Nastavimo pozicijo in velikost
        container.add(imeField); // Dodamo textfield v container

        opisLabel = new JLabel("Opis igre:"); // Ustvarimo nov label
        opisLabel.setBounds(10, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(opisLabel); // Dodamo label v container

        opisField = new JTextField(); // Ustvarimo nov textfield
        opisField.setBounds(220, 200, 200, 40); // Nastavimo pozicijo in velikost
        container.add(opisField); // Dodamo textfield v container

        shraniButton = new JButton("Shrani"); // Ustvarimo nov gumb
        shraniButton.setBounds(10, 450, 100, 40); // Nastavimo pozicijo in velikost
        shraniButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shraniIgro();
            }
        });
        container.add(shraniButton); // Dodamo gumb v container

        window.setVisible(true); // Naredimo okno vidno

        if (igraId != 0) {
            try {
                Baza db = Baza.getInstance();
                String query = "SELECT * FROM \"Igre\" WHERE id = " + igraId + ";";
                ResultSet resultSet = db.executeQuery(query);
                if (resultSet.next()) {
                    imeField.setText(resultSet.getString("ime"));
                    opisField.setText(resultSet.getString("opis"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void shraniIgro() {
        String ime = imeField.getText();
        String opis = opisField.getText();

        if (ime.isEmpty() || opis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Baza db = Baza.getInstance();

            if (igraId <= 0) {
                String query = "SELECT insert_igra('" + ime + "', '" + opis + "');";
                db.executeQuery(query);
            } else {
                String query = "SELECT update_igra(" + igraId + ", '" + ime + "', '" + opis + "');";
                db.executeQuery(query);
            }
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri shranjevanju igre.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true); // Naredimo okno vidno
    }
}
