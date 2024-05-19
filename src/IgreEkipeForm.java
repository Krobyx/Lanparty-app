import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IgreEkipeForm {

    private JFrame window;
    private Container container;
    private JLabel mainTitle;
    private JLabel imeLabel;
    private JComboBox<IgraItem> igreComboBox;
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

        igreComboBox = new JComboBox<>();
        igreComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        try {
            Baza db = Baza.getInstance();
            ResultSet rs = db.executeQuery("SELECT id, ime FROM \"Igre\" ORDER BY ime;");

            while (rs.next()) {
                igreComboBox.addItem(new IgraItem(rs.getInt("id"), rs.getString("ime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Napaka pri pridobivanju iger.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        formPanel.add(igreComboBox);

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
        if (igreComboBox.getItemCount() == 0 || igreComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(window, "Vnesite ime igre.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int igraId = ((IgraItem) igreComboBox.getSelectedItem()).getId();

        try {
            Baza db = Baza.getInstance();
            if (igraId < 1) {
                JOptionPane.showMessageDialog(window, "Igra ne obstaja.", "Napaka", JOptionPane.ERROR_MESSAGE);
                return;
            }

            db.executeQuery("SELECT dodaj_igro_ekipi(" + ekipaId + ", " + igraId + ");");
            JOptionPane.showMessageDialog(window, "Igra uspešno dodana k ekipi.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
            window.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "Napaka pri dodajanju igre. (Preverite, če igra že obstaja)", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        window.setVisible(true);
    }

    private class IgraItem {
        private int id;
        private String ime;

        public IgraItem(int id, String ime) {
            this.id = id;
            this.ime = ime;
        }

        public int getId() {
            return id;
        }

        public String getIme() {
            return ime;
        }

        @Override
        public String toString() {
            return ime;
        }
    }
}
