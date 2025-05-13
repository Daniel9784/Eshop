package sk.tmconsulting.Eshop.ProductsServices;

import sk.tmconsulting.Eshop.Products.Produkt;
import java.util.List;

public interface ProduktService<T extends Produkt> {
    List<T> ziskajVsetkyProdukty();
    void ulozProdukt(T produkt);
    void aktualizujProdukt(T produkt);
    void odstranProduktPodlaID(long id);
    T ziskajProduktPodlaID(long id);
}