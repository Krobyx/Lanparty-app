import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Ekipe {
    private JFrame window; // Okno aplikacije
    private Container container; // Glavni vsebnik za elemente uporabniškega vmesnika
    private JLabel mainTitle; // Glavni naslov obrazca
    private JTable table; // Tabela za prikaz iger
    private DefaultTableModel model; // Model tabele za shranjevanje podatkov
    private Baza db;

    public Ekipe() {
        try {
            db = Baza.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri povezavi s podatkovno bazo.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }

        // Inicializacija okna
        window = new JFrame("Ekipe");
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavitev velikosti okna
        window.setBounds(10, 10, 1024, 768); // Nastavitev položaja in velikosti okna
        window.setLayout(new BorderLayout()); // Uporaba BorderLayout za razporeditev komponent
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Zapiranje okna ob zaprtju
        window.setLocationRelativeTo(null); // Središčenje okna glede na zaslon
        window.setResizable(false); // Onemogočanje spreminjanja velikosti okna

        // Inicializacija glavnega vsebnika
        container = window.getContentPane();
        container.setLayout(new BorderLayout(20, 20));
        container.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja

        // Dodajanje glavnega naslova obrazca
        mainTitle = new JLabel("Ekipe");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48));
        mainTitle.setForeground(Color.DARK_GRAY); // Nastavitev barve besedila
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Dodajanje notranje obrobe za odmik
        container.add(mainTitle, BorderLayout.NORTH);

        // Ustvarjanje modela tabele
        model = new DefaultTableModel();
        model.addColumn("ID"); // Dodajanje stolpca "ID"
        model.addColumn("Ime"); // Dodajanje stolpca "Ime"
        model.addColumn("Koda"); // Dodajanje stolpca "Koda"

        // Pridobivanje podatkov iz podatkovne baze
        try {
            String query = "SELECT * FROM \"Ekipe\";";
            ResultSet resultSet = db.executeQuery(query);
            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("koda")});
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Dodajanje notranje obrobe za odmik

        // Ustvarjanje panela z gumbi
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja panela
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Dodajanje notranje obrobe za odmik

        JButton addButton = new JButton("Dodaj novo ekipo");
        JButton editButton = new JButton("Uredi ekipo");
        JButton deleteButton = new JButton("Izbriši ekipo");
        JButton refreshButton = new JButton("Osveži");
        JButton teamMembersButton = new JButton("Člani ekipe");
        JButton teamGamesButton = new JButton("Igre ekipe");

        addButton.setFont(new Font("Arial", Font.PLAIN, 20));
        addButton.setForeground(Color.BLUE);
        addButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        editButton.setFont(new Font("Arial", Font.PLAIN, 20));
        editButton.setForeground(Color.BLUE);
        editButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        deleteButton.setFont(new Font("Arial", Font.PLAIN, 20));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        refreshButton.setFont(new Font("Arial", Font.PLAIN, 20));
        refreshButton.setForeground(Color.BLUE);
        refreshButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        teamMembersButton.setFont(new Font("Arial", Font.PLAIN, 20));
        teamMembersButton.setForeground(Color.BLUE);
        teamMembersButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        teamGamesButton.setFont(new Font("Arial", Font.PLAIN, 20));
        teamGamesButton.setForeground(Color.BLUE);
        teamGamesButton.setFocusPainted(false); // Onemogočanje obrobe fokusa

        buttonsPanel.add(refreshButton);
        buttonsPanel.add(teamMembersButton);
        buttonsPanel.add(teamGamesButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        teamMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int teamID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                    ClaniEkipe claniEkipe = new ClaniEkipe(teamID);
                    claniEkipe.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite ekipo za ogled članov.");
                }
            }
        });

        teamGamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int teamID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                    IgreEkipe igreEkipe = new IgreEkipe(teamID);
                    igreEkipe.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite ekipo za ogled iger.");
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                try {
                    String query = "SELECT * FROM \"Ekipe\";";
                    ResultSet resultSet = db.executeQuery(query);
                    while (resultSet.next()) {
                        model.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("ime"), resultSet.getString("koda")});
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
                // Ob kliku na gumb "Dodaj novo ekipo" se prikaže sporočilo
                EkipeForm obrazec = new EkipeForm(0);
                obrazec.show();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ob kliku na gumb "Uredi ekipo" se preveri izbrana vrstica in prikaže ustrezno sporočilo
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    EkipeForm obrazec = new EkipeForm(Integer.parseInt(model.getValueAt(selectedRow, 0).toString()));
                    obrazec.show();
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite ekipo za urejanje.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ob kliku na gumb "Izbriši ekipo" se preveri izbrana vrstica in izbriše ustrezno vrstico iz tabele
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        String roleID = model.getValueAt(selectedRow, 0).toString();
                        String query = "SELECT delete_ekipa(" + roleID + ");";
                        db.executeQuery(query);
                        model.removeRow(selectedRow);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri brisanju ekipe.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(container, "Prosimo, izberite ekipo za brisanje.");
                }
            }
        });

        // Dodajanje elementov v glavni vsebnik
        container.add(scrollPane, BorderLayout.CENTER); // Dodajanje tabele
        container.add(buttonsPanel, BorderLayout.SOUTH); // Dodajanje panela z gumbi
    }

    // Metoda za prikaz okna
    public void show() {
        window.pack();
        window.setVisible(true);
    }
}
