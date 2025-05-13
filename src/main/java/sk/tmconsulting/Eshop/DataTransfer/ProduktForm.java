package sk.tmconsulting.Eshop.DataTransfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import sk.tmconsulting.Eshop.KategoriaProduktu;

public class ProduktForm {

    @NotBlank(message = "Názov je povinný.")
    private String nazov;

    @Positive(message = "Cena musí byť väčšia než 0.")
    private double cena;

    @NotBlank(message = "Farba je povinná.")
    private String farba;

    @NotBlank(message = "Veľkosť je povinná.")
    private String velkost;

    @Positive(message = "Počet je povinný.")
    private int pocet;

    private KategoriaProduktu kategoria;

    private long produktID;

    // Getters a Setters
    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getFarba() {
        return farba;
    }

    public void setFarba(String farba) {
        this.farba = farba;
    }

    public String getVelkost() {
        return velkost;
    }

    public void setVelkost(String velkost) {
        this.velkost = velkost;
    }

    public KategoriaProduktu getKategoria() {
        return kategoria;
    }

    public void setKategoria(KategoriaProduktu kategoria) {
        this.kategoria = kategoria;
    }

    public int getPocet() {
        return pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;

    }
}
