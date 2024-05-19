import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        window = new JFrame("Dodaj igro k ekipi");
        window.setPreferredSize(new Dimension(400, 300));
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        container = window.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.WHITE); // Set background color

        mainTitle = new JLabel("Dodaj igro k ekipi");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setForeground(Color.BLUE); // Set text color
        container.add(mainTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(1, 2));
        formPanel.setBackground(Color.WHITE); // Set background color

        imeLabel = new JLabel("Ime igre:");
        imeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        formPanel.add(imeLabel);

        imeField = new JTextField();
        imeField.setFont(new Font("Arial", Font.PLAIN, 18));
        formPanel.add(imeField);

        container.add(formPanel, BorderLayout.CENTER);

        addButton = new JButton("Dodaj");
        addButton.setFont(new Font("Arial", Font.PLAIN, 18));
        addButton.setForeground(Color.GREEN); // Set button text color
        addButton.addActionListener(e -> dodajIgro());
        container.add(addButton, BorderLayout.SOUTH);

        window.pack();
        window.setVisible(true);
    }

    private void dodajIgro() {
        String ime = imeField.getText().trim();

        if (ime.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Vnesite ime igre.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Baza db = Baza.getInstance();

            ResultSet rs = db.executeQuery("SELECT id FROM \"Igre\" WHERE ime = '" + ime + "';");

            if (rs.next()) {
                int igraId = rs.getInt("id");

                if (igraId < 1) {
                    JOptionPane.showMessageDialog(window, "Igra ne obstaja.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                db.executeQuery("SELECT dodaj_igro_ekipi(" + ekipaId + ", " + igraId + ");");
                JOptionPane.showMessageDialog(window, "Igra uspešno dodana k ekipi.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                window.dispose();
            } else {
                JOptionPane.showMessageDialog(window, "Igra ne obstaja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Napaka pri dodajanju igre. (Preverite, če igra že obstaja)", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true);
    }
}
