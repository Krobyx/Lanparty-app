import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
public class Igre {
    private JFrame window; // Okno aplikacije
    private Container container; // Glavni vsebnik za elemente uporabniškega vmesnika
    private JLabel mainTitle; // Glavni naslov obrazca
    private JTable table; // Tabela za prikaz iger
    private DefaultTableModel model; // Model tabele za shranjevanje podatkov
    private Baza db;

    public Igre() {
        try {
            db = Baza.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        // Inicializacija okna
        window = new JFrame("Igre");
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavitev velikosti okna
        window.setBounds(10, 10, 1024, 768); // Nastavitev položaja in velikosti okna
        window.setLayout(new BorderLayout()); // Uporaba BorderLayout za razporeditev komponent
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Zapiranje okna ob zaprtju
        window.setLocationRelativeTo(null); // Središčenje okna glede na zaslon
        window.setResizable(false); // Onemogočanje spreminjanja velikosti okna

        // Inicializacija glavnega vsebnika
        container = window.getContentPane();
        container.setLayout(new BorderLayout());

        // Dodajanje glavnega naslova obrazca
        mainTitle = new JLabel("Igre");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(mainTitle);

        // Ustvarjanje modela tabele
        model = new DefaultTableModel();
        model.addColumn("ID"); // Dodajanje stolpca "ID"
        model.addColumn("Ime"); // Dodajanje stolpca "Ime"
        model.addColumn("Opis"); // Dodajanje stolpca "Poštna številka"

        // Pridobivanje podatkov iz podatkovne baze
        try {
            String query = "SELECT * FROM \"Igre\";";
            ResultSet resultSet = db.executeQuery(query);
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("opis")});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        // Ustvarjanje tabele s podanim modelom
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 24));
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Onemogočanje samodejnega prilagajanja velikosti stolpcev

        // Nastavitev preferirane širine stolpcev
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(600);

        // Ustvarjanje plavajočega okvirja za tabelo
        JScrollPane scrollPane = new JScrollPane(table);

        // Ustvarjanje panela z gumbi
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Dodaj novo igro");
        JButton editButton = new JButton("Uredi igro");
        JButton deleteButton = new JButton("Izbriši igro");
        JButton refreshButton = new JButton("Osveži");
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                try {
                    String query = "SELECT * FROM \"Igre\";";
                    ResultSet resultSet = db.executeQuery(query);
                    while (resultSet.next()) {
                        model.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("opis")});
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Dodajanje poslušalcev dogodkov gumbom
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ob kliku na gumb "Dodaj novo igro" se prikaže sporočilo
                IgreForm obrazec = new IgreForm(0);
                obrazec.show();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ob kliku na gumb "Uredi igro" se preveri izbrana vrstica in prikaže ustrezno sporočilo
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    IgreForm obrazec = new IgreForm(Integer.parseInt(model.getValueAt(selectedRow, 0).toString()));
                    obrazec.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite igro za urejanje.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ob kliku na gumb "Izbriši igro" se preveri izbrana vrstica in izbriše ustrezno vrstico iz tabele
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        String roleID = model.getValueAt(selectedRow, 0).toString();
                        String query = "SELECT delete_igra(" + roleID + ");";
                        db.executeQuery(query);
                        model.removeRow(selectedRow);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri brisanju igre.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite igro za brisanje.");
                }
            }
        });

        // Dodajanje elementov v glavni vsebnik
        container.add(scrollPane, BorderLayout.CENTER); // Dodajanje tabele
        container.add(buttonsPanel, BorderLayout.SOUTH); // Dodajanje panela z gumbi
    }

    // Metoda za prikaz okna
    public void show() {
        window.setVisible(true);
    }
}