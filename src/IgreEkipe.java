import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class IgreEkipe {
    private JFrame window; // Okno aplikacije
    private Container container; // Glavni vsebnik za elemente uporabniškega vmesnika
    private JLabel mainTitle; // Glavni naslov obrazca
    private JTable table; // Tabela za prikaz iger
    private DefaultTableModel model; // Model tabele za shranjevanje podatkov
    private Baza db;
    private int ekipaId;

    public IgreEkipe(int ekipaId) {
        this.ekipaId = ekipaId;

        try {
            db = Baza.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        // Inicializacija okna
        window = new JFrame("Zastopane igre ekipe");
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavitev velikosti okna
        window.setBounds(10, 10, 1024, 768); // Nastavitev položaja in velikosti okna
        window.setLayout(new BorderLayout()); // Uporaba BorderLayout za razporeditev komponent
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Zapiranje okna ob zaprtju
        window.setLocationRelativeTo(null); // Središčenje okna glede na zaslon
        window.setResizable(false); // Onemogočanje spreminjanja velikosti okna

        // Inicializacija glavnega vsebnika
        container = window.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja

        // Dodajanje glavnega naslova obrazca
        mainTitle = new JLabel("Zastopane igre ekipe");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48));
        mainTitle.setForeground(Color.DARK_GRAY);
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(mainTitle, BorderLayout.NORTH);

        // Ustvarjanje modela tabele
        model = new DefaultTableModel();
        model.addColumn("ID"); // Dodajanje stolpca "ID"
        model.addColumn("Ime"); // Dodajanje stolpca "Ime"
        model.addColumn("Opis"); // Dodajanje stolpca "Opis"

        // Pridobivanje podatkov iz podatkovne baze
        fetchData();

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
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja

        JButton addButton = new JButton("Dodaj novo igro k ekipi");
        JButton deleteButton = new JButton("Odstrani igro iz ekipe");
        JButton refreshButton = new JButton("Osveži");

        customizeButton(refreshButton);
        customizeButton(addButton);
        customizeButton(deleteButton);

        buttonsPanel.add(refreshButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        // Dodajanje poslušalcev dogodkov gumbom
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                fetchData();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IgreEkipeForm obrazec = new IgreEkipeForm(ekipaId);
                obrazec.show();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        String roleID = model.getValueAt(selectedRow, 0).toString();
                        String query = "SELECT odstrani_igro_ekipe(" + ekipaId + ", " + roleID + ");";
                        db.executeQuery(query);
                        model.removeRow(selectedRow);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri odstranjevanju igre iz ekipe.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite igro za odstranjevanje.");
                }
            }
        });

        // Dodajanje elementov v glavni vsebnik
        container.add(scrollPane, BorderLayout.CENTER); // Dodajanje tabele
        container.add(buttonsPanel, BorderLayout.SOUTH); // Dodajanje panela z gumbi

        window.pack();
        window.setVisible(true);
    }

    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    private void fetchData() {
        try {
            String query = "SELECT * FROM \"Igre\" WHERE id IN (SELECT igra_id FROM \"Ekipe_Igre\" WHERE ekipa_id = " + ekipaId + ");";
            ResultSet resultSet = db.executeQuery(query);
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("opis")});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju podatkov iz baze.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metoda za prikaz okna
    public void show() {
        window.setVisible(true);
    }
}
