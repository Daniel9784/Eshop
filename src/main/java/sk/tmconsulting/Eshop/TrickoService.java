package sk.tmconsulting.Eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrickoService {
    @Autowired
    private TrickoRepository trickoRepository;

    public List<Tricko> ziskajVsetkyTricka() {
        return trickoRepository.findAll();
    }

    public void ulozTricko(Tricko tricko){
        trickoRepository.save(tricko);
    }
    public void aktualizujTricko(Tricko tricko){
        trickoRepository.save(tricko);    // Aktualizacia tricka a jeho ukladanie z pohladu JPA je to iste
    }
    public void odstranTrickoPodlaID(long id){
        trickoRepository.deleteById(id);
    }
    public Tricko ziskajTrickoPodlaID(long id){
        return trickoRepository.findById(id).orElse(null);
    }

}