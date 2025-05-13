package sk.tmconsulting.Eshop.Products;

import jakarta.persistence.*;

@Entity
public class Tricko extends Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long produktID;

    public long getProduktID() {
        return produktID;
    }

    public void setProduktID(long produktID) {
        this.produktID = produktID;
    }
}
