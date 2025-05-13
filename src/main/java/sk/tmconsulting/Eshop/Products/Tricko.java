package sk.tmconsulting.Eshop.Products;

import jakarta.persistence.*;

@Entity
public class Tricko extends Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tricko_seq")
    @SequenceGenerator(name = "tricko_seq", sequenceName = "tricko_sequence", initialValue = 1000)
    private long produktID;

    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
    }
}
