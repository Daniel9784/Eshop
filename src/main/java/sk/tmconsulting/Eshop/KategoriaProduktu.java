package sk.tmconsulting.Eshop;

public enum KategoriaProduktu {
    TRICKO("Tričko"),
    TOPANKY("Topánky"),
    NOHAVICE("Nohavice");

    private final String nazovKategorie;

    KategoriaProduktu(String nazovKategorie) {
        this.nazovKategorie = nazovKategorie;
    }

    public String getNazovKategorie() {
        return nazovKategorie;
    }
}