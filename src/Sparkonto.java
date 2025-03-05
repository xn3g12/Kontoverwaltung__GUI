public class Sparkonto extends KontoKlasse {
    public Sparkonto(String kontoinhaber, double startbetrag) {
        super(kontoinhaber, startbetrag);
    }

    @Override
    public void kontoauszug() {
        super.kontoauszug();
        System.out.println("Sparkonto-Details");
    }
}
