import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {

    private JFrame window;
    private Container container;

    private JLabel mainTitle;

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
        window.setResizable(false); // Omogočimo spreminjanje velikosti okna

        container = window.getContentPane(); // Ustvarimo nov container

        mainTitle = new JLabel("Nadzorna plošča"); // Ustvarimo nov label
        mainTitle.setFont(new Font("Arial", Font.BOLD, 48)); // Nastavimo velikost in obliko pisave
        mainTitle.setBounds(10, 50, 1004, 50); // Nastavimo pozicijo in velikost
        container.add(mainTitle); // Dodamo label v container

        if (Shramba.getInstance().isAdministrator()) {
            JButton kraji = new JButton("Kraji");
            kraji.setBounds(10, 200, 200, 40);
            container.add(kraji);

            kraji.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Kraji kraji = new Kraji();
                    kraji.show();
                }
            });
        }
    }

    public void show() {
        window.setVisible(true);
    }
}