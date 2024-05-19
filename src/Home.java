import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Home {

    private JFrame window;
    private Container container;

    private JLabel mainTitle;

    private JLabel kodaSkupineLabel;
    private JTextField kodaSkupineField;
    private JButton pridruziSeButton;

    private JLabel imeSkupineLabel;
    private JTextField imeSkupineField;
    private JButton ustvariSkupinoButton;

    private JLabel imeSvojeSkupineLabel;
    private JLabel kodaSvojeSkupineLabel;
    private JButton zapustiSkupinoButton;
    private JButton claniSkupineButton;
    private JButton igreEkipeButton;

    private Baza baza;

    public Home() {
        try {
            baza = Baza.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        window = new JFrame("Lanparty aplikacija"); // Ustvarimo novo okno
        window.setPreferredSize(new Dimension(1024, 768)); // Nastavimo velikost okna
        window.setBounds(10, 10, 1024, 768); // Nastavimo pozicijo in velikost okna
        window.setLayout(null); // Nastavimo postavitev okna
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Nastavimo akcijo ob zaprtju okna
        window.setLocationRelativeTo(null); // Nastavimo pozicijo okna na sredino
        window.setResizable(false); // Onemogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY); // Nastavitev ozadja

        mainTitle = new JLabel("Nadzorna plošča"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setForeground(Color.DARK_GRAY); // Nastavitev barve besedila
        mainTitle.setBounds(10, 20, 1004, 50); // Nastavimo pozicijo in velikost
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER); // Nastavimo poravnavo besedila
        container.add(mainTitle); // Dodamo label v container

        if (Shramba.getInstance().isAdministrator()) {
            JButton kraji = new JButton("Kraji");
            customizeButton(kraji, 10, 200);
            container.add(kraji);

            kraji.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Kraji kraji = new Kraji();
                    kraji.show();
                }
            });

            JButton igre = new JButton("Igre");
            customizeButton(igre, 10, 250);
            container.add(igre);

            igre.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Igre igre = new Igre();
                    igre.show();
                }
            });

            JButton ekipe = new JButton("Ekipe");
            customizeButton(ekipe, 10, 300);
            container.add(ekipe);

            ekipe.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Ekipe ekipe = new Ekipe();
                    ekipe.show();
                }
            });
        } else {
            imeSvojeSkupineLabel = new JLabel("Ime skupine:");
            customizeLabel(imeSvojeSkupineLabel, 10, 200);
            container.add(imeSvojeSkupineLabel);

            kodaSvojeSkupineLabel = new JLabel("Koda skupine:");
            customizeLabel(kodaSvojeSkupineLabel, 10, 250);
            container.add(kodaSvojeSkupineLabel);

            zapustiSkupinoButton = new JButton("Zapusti skupino");
            customizeButton(zapustiSkupinoButton, 10, 300);
            container.add(zapustiSkupinoButton);

            zapustiSkupinoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        baza.executeQuery("SELECT zapusti_ekipo(" + Shramba.getInstance().uporabnikId + ");");
                        Shramba.getInstance().uporabnikEkipaId = -1;
                        showJoinCreateGroup();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Napaka pri zapuščanju skupine.", "Napaka", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            claniSkupineButton = new JButton("Člani skupine");
            customizeButton(claniSkupineButton, 10, 350);
            container.add(claniSkupineButton);

            claniSkupineButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClaniEkipe claniEkipe = new ClaniEkipe(Shramba.getInstance().uporabnikEkipaId);
                    claniEkipe.show();
                }
            });

            igreEkipeButton = new JButton("Igre skupine");
            customizeButton(igreEkipeButton, 10, 400);
            container.add(igreEkipeButton);

            igreEkipeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IgreEkipe igreEkipe = new IgreEkipe(Shramba.getInstance().uporabnikEkipaId);
                    igreEkipe.show();
                }
            });

            kodaSkupineLabel = new JLabel("Koda skupine:");
            customizeLabel(kodaSkupineLabel, 10, 200);
            container.add(kodaSkupineLabel);

            kodaSkupineField = new JTextField();
            customizeTextField(kodaSkupineField, 220, 200);
            container.add(kodaSkupineField);

            pridruziSeButton = new JButton("Pridruži se");
            customizeButton(pridruziSeButton, 10, 250);
            container.add(pridruziSeButton);

            pridruziSeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pridruziSe();
                }
            });

            imeSkupineLabel = new JLabel("Ime skupine:");
            customizeLabel(imeSkupineLabel, 10, 300);
            container.add(imeSkupineLabel);

            imeSkupineField = new JTextField();
            customizeTextField(imeSkupineField, 220, 300);
            container.add(imeSkupineField);

            ustvariSkupinoButton = new JButton("Ustvari skupino");
            customizeButton(ustvariSkupinoButton, 10, 350);
            container.add(ustvariSkupinoButton);

            ustvariSkupinoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ustvariSkupino();
                }
            });

            if (Shramba.getInstance().uporabnikEkipaId != -1) {
                removeJoinCreateGroup();
            } else {
                showJoinCreateGroup();
            }
        }

        JButton logout = new JButton("Odjava");
        customizeButton(logout, 10, 600);
        container.add(logout);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        window.pack();
        window.setVisible(true);
    }

    private void customizeLabel(JLabel label, int x, int y) {
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setBounds(x, y, 200, 40);
    }

    private void customizeTextField(JTextField textField, int x, int y) {
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setBounds(x, y, 300, 40);
    }

    private void customizeButton(JButton button, int x, int y) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBounds(x, y, 200, 40);
    }

    private void removeJoinCreateGroup() {
        kodaSkupineLabel.setVisible(false);
        kodaSkupineField.setVisible(false);
        pridruziSeButton.setVisible(false);
        imeSkupineLabel.setVisible(false);
        imeSkupineField.setVisible(false);
        ustvariSkupinoButton.setVisible(false);

        imeSvojeSkupineLabel.setVisible(true);
        kodaSvojeSkupineLabel.setVisible(true);
        zapustiSkupinoButton.setVisible(true);
        claniSkupineButton.setVisible(true);
        igreEkipeButton.setVisible(true);

        try {
            ResultSet resultSet = baza.executeQuery("SELECT get_ekipa_ime(" + Shramba.getInstance().uporabnikEkipaId + ");");
            if (resultSet.next()) {
                imeSvojeSkupineLabel.setText("Ime skupine: " + resultSet.getString(1));
            }

            resultSet = baza.executeQuery("SELECT get_ekipa_koda(" + Shramba.getInstance().uporabnikEkipaId + ");");
            if (resultSet.next()) {
                kodaSvojeSkupineLabel.setText("Koda skupine: " + resultSet.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri pridobivanju imena skupine.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showJoinCreateGroup() {
        kodaSkupineLabel.setVisible(true);
        kodaSkupineField.setVisible(true);
        pridruziSeButton.setVisible(true);
        imeSkupineLabel.setVisible(true);
        imeSkupineField.setVisible(true);
        ustvariSkupinoButton.setVisible(true);

        imeSvojeSkupineLabel.setVisible(false);
        kodaSvojeSkupineLabel.setVisible(false);
        zapustiSkupinoButton.setVisible(false);
        claniSkupineButton.setVisible(false);
        igreEkipeButton.setVisible(false);
    }

    private void pridruziSe() {
        String kodaSkupine = kodaSkupineField.getText();

        if (kodaSkupine.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "SELECT pridruzi_se_ekipi(" + Shramba.getInstance().uporabnikId + ", '" + kodaSkupine + "');";
            baza.executeQuery(query);

            ResultSet resultSet = baza.executeQuery("SELECT get_uporabnik_ekipa_id(" + Shramba.getInstance().uporabnikId + ");");
            if (resultSet.next()) {
                int uporabnikEkipaId = resultSet.getInt(1);

                if (uporabnikEkipaId > 0) {
                    Shramba.getInstance().uporabnikEkipaId = uporabnikEkipaId;
                    removeJoinCreateGroup();
                } else {
                    JOptionPane.showMessageDialog(null, "Napačna koda skupine.", "Napaka", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri pridruževanju skupini.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ustvariSkupino() {
        String imeSkupine = imeSkupineField.getText();

        if (imeSkupine.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Izpolnite vsa polja.", "Napaka", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "SELECT ustvari_ekipo('" + imeSkupine + "', " + Shramba.getInstance().uporabnikId + ");";
            baza.executeQuery(query);

            ResultSet resultSet = baza.executeQuery("SELECT get_uporabnik_ekipa_id(" + Shramba.getInstance().uporabnikId + ");");
            if (resultSet.next()) {
                Shramba.getInstance().uporabnikEkipaId = resultSet.getInt(1);
                removeJoinCreateGroup();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Napaka pri ustvarjanju skupine.", "Napaka", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        // Implementacija odjave
        Shramba.getInstance().uporabnikId = 0;
        Shramba.getInstance().uporabnikEkipaId = -1;
        window.dispose();
        Prijava prijava = new Prijava();
        prijava.show();
    }

    public void show() {
        window.setVisible(true);
    }
}
