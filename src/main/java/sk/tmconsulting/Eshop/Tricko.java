package sk.tmconsulting.Eshop;

import jakarta.persistence.*;

@Entity
public class Tricko {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //nastavi ako primarny kluc a zaroven inkrementuje hodnotu +1
    private long produktID;
    private String farba;
    private String velkost;
    private double cena;
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
