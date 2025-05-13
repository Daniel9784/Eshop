package sk.tmconsulting.Eshop.Products;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import sk.tmconsulting.Eshop.KategoriaProduktu;

@MappedSuperclass
public abstract class Produkt {

    @NotBlank(message = "Názov je povinný.")
    private String nazov;

    @Positive(message = "Cena musí byť väčšia než 0.")
    private double cena;

    @NotBlank(message = "Farba je povinná.")
    private String farba;

    @NotBlank(message = "Veľkosť je povinná.")
    private String velkost;

    @Enumerated(EnumType.STRING)
    private KategoriaProduktu kategoria;


    // Gettery a settery
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
}
