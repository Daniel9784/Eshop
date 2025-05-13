package sk.tmconsulting.Eshop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
