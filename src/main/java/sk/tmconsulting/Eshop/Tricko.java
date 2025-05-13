package sk.tmconsulting.Eshop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


@Entity
public class Tricko {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //nastavi ako primarny kluc a zaroven inkrementuje hodnotu +1
    private long produktID;

    @NotBlank(message = "Farba je povinná.")
    private String farba;

    @NotBlank(message = "Veľkosť je povinná.")
    private String velkost;

    @Positive(message = "Cena musí byť väčšia než 0.")
    private double cena;

    @NotBlank(message = "Názov je povinný.")
    private String nazov;

    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
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

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }
}
