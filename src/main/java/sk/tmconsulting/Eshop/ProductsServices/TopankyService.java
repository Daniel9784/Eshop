package sk.tmconsulting.Eshop.ProductsServices;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tmconsulting.Eshop.Products.Topanky;
import sk.tmconsulting.Eshop.ProductsRepository.TopankyRepository;

import java.util.List;

@Service
public class TopankyService implements ProduktService<Topanky> {
    @Autowired
    private TopankyRepository topankyRepository;

    @Override
    public List<Topanky> ziskajVsetkyProdukty() {
        return topankyRepository.findAll();
    }

    @Override
    public void ulozProdukt(Topanky topanky) {
        topankyRepository.save(topanky);
    }

    @Override
    public void aktualizujProdukt(Topanky topanky) {
        topankyRepository.save(topanky);
    }

    @Override
    public void odstranProduktPodlaID(long id) {
        topankyRepository.deleteById(id);
    }

    @Override
    public Topanky ziskajProduktPodlaID(long id) {
        return topankyRepository.findById(id).orElse(null);
    }
}