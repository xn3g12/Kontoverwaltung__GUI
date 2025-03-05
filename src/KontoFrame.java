import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class KontoFrame extends Component {
    private JPanel JPanel;
    private JComboBox<String> kontoComboBox;
    private JLabel kontoartJLabel;
    private JLabel kontoinhaberJLabel;
    private JLabel kontonummerJLabel;
    private JLabel gebuehrenJLabel;
    private JLabel kontostandJLabel;
    private JLabel VerlaufJLabel;

    private JTextField kontoinhaberField;
    private JTextField kontonummerField;
    private JTextField gebuehrenField;
    private JTextField kontostandField;


    private JButton closeButton;
    private JButton saveButton;
    public JTextArea textArea1;
    private JButton einzahlenButton;
    private JButton auszahlenButton;
    private JButton ueberweisenButton;
    private JLabel einzahlenJLabel;
    private JLabel auszahlenJLabel;
    private JLabel ueberweisenJLabel;

    private static  ArrayList<KontoKlasse> konten = new ArrayList<>();

    public KontoFrame() {

        kontoComboBox.addItem("Girokonto");
        kontoComboBox.addItem("Sparkonto");
        kontoComboBox.addItem("Kreditkonto");

        // Event: Speichern
        saveButton.addActionListener(e -> {
            String kontoinhaber = kontoinhaberField.getText();
            double kontobetrag;

            try {
                kontobetrag = Double.parseDouble(kontostandField.getText());
            } catch (NumberFormatException ex) {
                textArea1.append("Ungültiger Kontobetrag\n");
                return;
            }

            String kontoart = (String) kontoComboBox.getSelectedItem();
            KontoKlasse neuesKonto = null;

            switch (kontoart) {
                case "Girokonto":
                    neuesKonto = new Girokonto(kontoinhaber, kontobetrag);
                    break;
                case "Sparkonto":
                    neuesKonto = new Sparkonto(kontoinhaber, kontobetrag);
                    break;
                case "Kreditkonto":
                    neuesKonto = new Kreditkonto(kontoinhaber, kontobetrag);
                    break;
                default:
                    textArea1.append("Ungültige Kontoart\n");
                    return;
            }

            // Konto in der Liste speichern
            konten.add(neuesKonto);

            updateTextArea();

            // Eingabefelder leeren
            kontoinhaberField.setText("");
            kontostandField.setText("");
            kontonummerField.setText("");
            gebuehrenField.setText("");
        });

        // Einzahlen-Button: Betrag auf Konto hinzufügen
        einzahlenButton.addActionListener(e -> {
            String kontonummerStr = JOptionPane.showInputDialog("Gib die Kontonummer ein:");
            if (kontonummerStr == null || kontonummerStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib eine gültige Kontonummer ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int kontonummer;
            try {
                kontonummer = Integer.parseInt(kontonummerStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ungültige Kontonummer.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KontoKlasse konto = findeKonto(kontonummer);
            if (konto == null) {
                JOptionPane.showMessageDialog(null, "Kein Konto mit dieser Nummer gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String kontoinhaberStr = JOptionPane.showInputDialog("Gib den Kontoinhaber ein:");
            if (kontoinhaberStr == null || kontoinhaberStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib einen gültigen Kontoinhaber ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String betragStr = JOptionPane.showInputDialog("Gib den Betrag ein:");
            if (betragStr == null || betragStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib einen gültigen Betrag ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double betrag;
            try {
                betrag = Double.parseDouble(betragStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ungültiger Betrag.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            konto.einzahlen(betrag);
            updateTextArea();
        });
        // Auszahlen-Button: Betrag vom Konto abheben

        auszahlenButton.addActionListener(e -> {
            // Eingabe der Kontonummer
            String kontonummerStr = JOptionPane.showInputDialog("Gib die Kontonummer ein:");
            if (kontonummerStr == null || kontonummerStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib eine gültige Kontonummer ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int kontonummer;
            try {
                kontonummer = Integer.parseInt(kontonummerStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ungültige Kontonummer.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Eingabe des Kontoinhabers
            String kontoinhaberStr = JOptionPane.showInputDialog("Gib den Kontoinhaber ein:");
            if (kontoinhaberStr == null || kontoinhaberStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib einen gültigen Kontoinhaber ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Konto suchen
            KontoKlasse konto = findeKonto(kontonummer);
            if (konto == null || !konto.getKontoinhaber().equals(kontoinhaberStr)) {
                JOptionPane.showMessageDialog(null, "Kein Konto mit dieser Nummer und diesem Inhaber gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Eingabe des Betrags
            String betragStr = JOptionPane.showInputDialog("Gib den Betrag ein:");
            if (betragStr == null || betragStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte gib einen gültigen Betrag ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double betrag;
            try {
                betrag = Double.parseDouble(betragStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ungültiger Betrag.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vorherigen Kontostand speichern
            double alterBetrag = konto.getKontostand();

            // Überprüfen, ob genug Guthaben auf dem Konto ist
            if (!konto.abheben(betrag)) {
                JOptionPane.showMessageDialog(null, "Nicht genügend Guthaben!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Neuer Kontostand nach der Auszahlung
            double neuerBetrag = konto.getKontostand();

            // Anzeige der Transaktion im TextArea
            textArea1.append("\nAuszahlung: -" + betrag + " Euro vom Konto " + kontonummer + "\n");
            textArea1.append("Alter Kontostand: " + alterBetrag + " Euro\n");
            textArea1.append("Neuer Kontostand: " + neuerBetrag + " Euro\n");
            textArea1.append("------------------------------\n");

            // Update der Anzeige
            updateTextArea();
        });




        ueberweisenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Überweisung");
                frame.setContentPane(new UeberweisungGUI(KontoFrame.this).UeberweisungView);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        // Event: Schließen
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void einzahlen() {
        KontoKlasse konto = getSelectedKonto();
        if (konto != null) {
            double betrag = Double.parseDouble(JOptionPane.showInputDialog("Betrag zum Einzahlen:"));
            konto.einzahlen(betrag);
            updateFields(konto);
        }
    }

    public void abheben() {
        KontoKlasse konto = getSelectedKonto();
        if (konto != null) {
            double betrag = Double.parseDouble(JOptionPane.showInputDialog("Betrag zum Abheben:"));
            konto.abheben(betrag);
            updateFields(konto);
        }
    }

    private KontoKlasse getSelectedKonto() {
        int index = kontoComboBox.getSelectedIndex();
        if (index >= 0) {
            return konten.get(index);
        }
        return null;
    }

    private void updateFields(KontoKlasse konto) {
        kontonummerField.setText(String.valueOf(konto.getKontonummer()));
        kontostandField.setText(String.format("%.2f", konto.getKontostand()));
    }
    protected KontoKlasse findeKonto(int kontonummer) {
        for (KontoKlasse konto : konten) {
            if (konto.getKontonummer() == kontonummer) {
                return konto;
            }
        }
        return null;
    }
    private void updateTextArea() {
        textArea1.setText(""); // Vorherigen Text löschen

        for (KontoKlasse konto : konten) {
            textArea1.append("\nArt: " + konto.getClass().getSimpleName() + "\n"); // Kontoart über Klassennamen bestimmen
            textArea1.append("Kontonummer: " + konto.getKontonummer() + "\n");
            textArea1.append("Inhaber: " + konto.getKontoinhaber() + "\n");
            textArea1.append("Betrag: " + String.format("%.2f", konto.getKontostand()) + " Euro\n");
            textArea1.append("------------------------------\n");
        }
    }

    public static void main(String[] args) {
        JFrame frame=new JFrame("");
        frame.setContentPane(new KontoFrame().JPanel);
        frame.setTitle("Kontoverwaltung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

