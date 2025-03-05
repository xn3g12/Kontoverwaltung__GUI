import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UeberweisungGUI {
    private static KontoFrame kontoFrame = new KontoFrame();
    private JTextField KontoinhaberVonJTextField;
    private JTextField BetragVonJTextField;
    private JTextField KontonummerVonJTextField;
    private JTextField ZuKontonummerJTextField;
    private JTextField ZuKontoinhaberJTextField;
    private JButton überweisenButton;
    private JButton abbrechenButton;
    private JLabel KontoinhaberVonJLab;
    private JLabel KontonummerVonJLab;
    private JLabel BetragVonJLab;
    private JLabel ueberweisenVonJLabel;
    private JLabel ueberweisenZuJLabel;
    protected JPanel UeberweisungView;

    public UeberweisungGUI(KontoFrame kontoFrame) {
            UeberweisungGUI.kontoFrame = kontoFrame;

            // Überweisen Button
            überweisenButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int vonKontonummer = Integer.parseInt(KontonummerVonJTextField.getText());
                    int zuKontonummer = Integer.parseInt(ZuKontonummerJTextField.getText());
                    double betrag;
                    try {
                        betrag = Double.parseDouble(BetragVonJTextField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ungültiger Betrag!", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    KontoKlasse vonKonto = kontoFrame.findeKonto(vonKontonummer);
                    KontoKlasse zuKonto = kontoFrame.findeKonto(zuKontonummer);

                    if (vonKonto != null && zuKonto != null) {
                        if (vonKonto.abheben(betrag)) {
                            zuKonto.einzahlen(betrag);
                            kontoFrame.textArea1.append("Überweisung erfolgreich.\n");
                            kontoFrame.textArea1.append("Betrag: " + betrag + " EUR von Konto " + vonKontonummer + " zu Konto " + zuKontonummer + " überwiesen.\n");

                            // Neuen Kontostand anzeigen
                            kontoFrame.textArea1.append("Neuer Kontostand von Konto " + vonKontonummer + ": " + vonKonto.getKontostand() + " EUR.\n");
                            kontoFrame.textArea1.append("Neuer Kontostand von Konto " + zuKontonummer + ": " + zuKonto.getKontostand() + " EUR.\n");
                        } else {
                            kontoFrame.textArea1.append("Nicht genügend Guthaben für Überweisung!\n");
                        }
                    } else {
                        kontoFrame.textArea1.append("Eines oder beide Konten nicht gefunden!\n");
                    }

                    // Fenster schließen
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(UeberweisungView);
                    frame.dispose();
                }
            });
            // Abbrechen Button
            abbrechenButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Fenster schließen
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(UeberweisungView);
                    frame.dispose();
                }
            });
        }

        public static void main(String[] args) {
            JFrame frame = new JFrame("Überweisung");
            frame.setContentPane(new UeberweisungGUI(kontoFrame).UeberweisungView);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    }
