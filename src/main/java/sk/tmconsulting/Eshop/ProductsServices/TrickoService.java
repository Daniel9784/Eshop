package sk.tmconsulting.Eshop.ProductsServices;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tmconsulting.Eshop.Products.Tricko;
import sk.tmconsulting.Eshop.ProductsRepository.TrickoRepository;

import java.util.List;

@Service
public class TrickoService implements ProduktService<Tricko> {
    @Autowired
    private TrickoRepository trickoRepository;

    @Override
    public List<Tricko> ziskajVsetkyProdukty() {
        return trickoRepository.findAll();
    }

    @Override
    public void ulozProdukt(Tricko tricko) {
        trickoRepository.save(tricko);
    }

    @Override
    public void aktualizujProdukt(Tricko tricko) {
        trickoRepository.save(tricko);
    }

    @Override
    public void odstranProduktPodlaID(long id) {
        trickoRepository.deleteById(id);
    }

    @Override
    public Tricko ziskajProduktPodlaID(long id) {
        return trickoRepository.findById(id).orElse(null);
    }
}