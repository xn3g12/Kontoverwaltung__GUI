public class Girokonto extends KontoKlasse {
    public Girokonto(String kontoinhaber, double startbetrag) {
        super(kontoinhaber, startbetrag);
    }

    @Override
    public void kontoauszug() {
        super.kontoauszug();
        System.out.println("Girokonto-Details");
    }
}
