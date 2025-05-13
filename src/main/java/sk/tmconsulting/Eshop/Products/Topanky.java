package sk.tmconsulting.Eshop.Products;

import jakarta.persistence.*;

@Entity
public class Topanky extends Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topanky_seq")
    @SequenceGenerator(name = "topanky_seq", sequenceName = "topanky_sequence", initialValue = 2000)
    private long produktID;

    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
    }
}
