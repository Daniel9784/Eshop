package sk.tmconsulting.Eshop.ProductsServices;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tmconsulting.Eshop.Products.Nohavice;
import sk.tmconsulting.Eshop.ProductsRepository.NohaviceRepository;

import java.util.List;

@Service
public class NohaviceService implements ProduktService<Nohavice> {
    @Autowired
    private NohaviceRepository nohaviceRepository;

    @Override
    public List<Nohavice> ziskajVsetkyProdukty() {
        return nohaviceRepository.findAll();
    }

    @Override
    public void ulozProdukt(Nohavice nohavice) {
        nohaviceRepository.save(nohavice);
    }

    @Override
    public void aktualizujProdukt(Nohavice nohavice) {
        nohaviceRepository.save(nohavice);
    }

    @Override
    public void odstranProduktPodlaID(long id) {
        nohaviceRepository.deleteById(id);
    }

    @Override
    public Nohavice ziskajProduktPodlaID(long id) {
        return nohaviceRepository.findById(id).orElse(null);
    }
}