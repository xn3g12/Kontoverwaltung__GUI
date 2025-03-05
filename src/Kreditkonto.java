public class Kreditkonto extends KontoKlasse {
    public Kreditkonto(String kontoinhaber, double startbetrag) {
        super(kontoinhaber, startbetrag);
    }

    @Override
    public void kontoauszug() {
        super.kontoauszug();
        System.out.println("Kreditkonto-Details");
    }
}
