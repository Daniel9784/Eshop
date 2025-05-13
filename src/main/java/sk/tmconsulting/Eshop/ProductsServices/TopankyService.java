package sk.tmconsulting.Eshop.ProductsServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tmconsulting.Eshop.Products.Topanky;
import sk.tmconsulting.Eshop.ProductsRepository.TopankyRepository;

import java.util.List;

@Service
public class TopankyService {
    @Autowired
    private TopankyRepository topankyRepository;

    public List<Topanky> ziskajVsetkyTopanky() {
        return topankyRepository.findAll();
    }

    public void ulozTopanky(Topanky topanky){
        topankyRepository.save(topanky);
    }
    public void aktualizujTopanky(Topanky topanky){
        topankyRepository.save(topanky);    // Aktualizacia tricka a jeho ukladanie z pohladu JPA je to iste
    }
    public void odstranTopankyPodlaID(long id){
        topankyRepository.deleteById(id);
    }
    public Topanky ziskajTopankyPodlaID(long id){
        return topankyRepository.findById(id).orElse(null);
    }


}
