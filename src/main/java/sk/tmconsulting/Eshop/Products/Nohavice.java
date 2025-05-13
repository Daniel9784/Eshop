package sk.tmconsulting.Eshop.Products;

import jakarta.persistence.*;

@Entity
public class Nohavice extends Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nohavice_seq")
    @SequenceGenerator(name = "nohavice_seq", sequenceName = "nohavice_sequence", initialValue = 3000)
    private long produktID;

    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
    }
}