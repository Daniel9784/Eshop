package sk.tmconsulting.Eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NohaviceService {
    @Autowired
    private NohaviceRepository nohaviceRepository;

    public List<Nohavice> ziskajVsetkyNohavice() {
        return nohaviceRepository.findAll();
    }

    public void ulozNohavice(Nohavice nohavice) {
        nohaviceRepository.save(nohavice);
    }

    public void odstranNohavicePodlaID(long id) {
        nohaviceRepository.deleteById(id);
    }

    public Nohavice ziskajNohavicePodlaID(long id) {
        return nohaviceRepository.findById(id).orElse(null);
    }
}