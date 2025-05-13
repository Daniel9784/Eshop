package sk.tmconsulting.Eshop;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import sk.tmconsulting.Eshop.DataTransfer.ProduktForm;
import sk.tmconsulting.Eshop.Products.*;
import sk.tmconsulting.Eshop.ProductsServices.*;

import java.util.*;

@Controller
public class ProduktController implements ErrorController {
    private final Map<KategoriaProduktu, ProduktService<? extends Produkt>> serviceMap;

    // Mapa pre spajanie kazdeho typu produktu s prislusnou sluzbou
    public ProduktController(
            TrickoService trickoService,
            TopankyService topankyService,
            NohaviceService nohaviceService) {
        serviceMap = new EnumMap<>(KategoriaProduktu.class);
        serviceMap.put(KategoriaProduktu.TRICKO, trickoService);
        serviceMap.put(KategoriaProduktu.TOPANKY, topankyService);
        serviceMap.put(KategoriaProduktu.NOHAVICE, nohaviceService);
    }

    @GetMapping("/")
    public String uvodnaStranka() {
        return "index";
    }

    //Zobraz vsetky zaznamy (spaja vsetky produkty sluzieb do jedneho zoznamu)
    @GetMapping("/zobraz-vsetky-zaznamy")
    public String zobrazVsetkyZaznamy(Model model) {
        List<Produkt> vsetkyProdukty = new ArrayList<>();
        serviceMap.values().forEach(service ->
                vsetkyProdukty.addAll(service.ziskajVsetkyProdukty())
        );
        model.addAttribute("vsetkyZaznamy", vsetkyProdukty);
        return "zobraz-vsetky-zaznamy";
    }

    //Pridaj zaznam formular
    @GetMapping("/pridaj-zaznam")
    public String pridajZaznam(Model model) {
        model.addAttribute("pridajZaznam", new ProduktForm());
        return "pridaj-zaznam";
    }

    //Uloz zaznam (spravuje validaciu, kontroluje ci formular neobsahuje chyby a hlada produkt v sluzbach, pri zmene kategorii zmaze stary produkt a vytvori novy)
    @PostMapping("/uloz-zaznam")
    public String ulozZaznam(@Valid @ModelAttribute("upravZaznam") ProduktForm produktForm,
                             BindingResult bindingResult, Model model) {
        // Kontrola validacie formulara
        if (bindingResult.hasErrors()) {
            return "uprav-zaznam";
        }

        KategoriaProduktu novaKategoria = produktForm.getKategoria();
        long produktId = produktForm.getProduktID();

        // Kontrola ci existuje produkt podla ID
        if (produktId != 0) {
            KategoriaProduktu aktualnaKategoria = null;
            Produkt existujuciProdukt = null;

        // Prechadza vsetky sluzby a zisti ci produkt existuje
            for (Map.Entry<KategoriaProduktu, ProduktService<? extends Produkt>> entry : serviceMap.entrySet()) {
                Produkt produkt = entry.getValue().ziskajProduktPodlaID(produktId);
                if (produkt != null) {
                    aktualnaKategoria = entry.getKey();
                    existujuciProdukt = produkt;
                    break;
                }
            }
        // Zistuje existenciu produktu podla ID a zisti ci je to rovnaky produkt alebo je to novy produkt
            if (aktualnaKategoria != null && aktualnaKategoria != novaKategoria) {
                try {
                    // Pridanie  oneskorenia pred vymazaním
                    Thread.sleep(500);

                    serviceMap.get(aktualnaKategoria).odstranProduktPodlaID(produktId);

                    // Pridanie  oneskorenie pred vytvorením noveho
                    Thread.sleep(500);

                    Produkt novyProdukt = vytvorProdukt(novaKategoria);
                    copyProduktFormToEntity(produktForm, novyProdukt);
                    ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(novyProdukt);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Aktualizacia existujuceho produktu
            } else if (existujuciProdukt != null) {
                copyProduktFormToEntity(produktForm, existujuciProdukt);
                ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(existujuciProdukt);
            }
            // Vytvorenie noveho produktu
        } else {
            Produkt novyProdukt = vytvorProdukt(novaKategoria);
            copyProduktFormToEntity(produktForm, novyProdukt);
            ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(novyProdukt);
        }

        return "redirect:/zobraz-vsetky-zaznamy";
    }

    // Uprav zaznam ziskava produkt podla kategorie a ID, pridava ho do model pre upravu
    @GetMapping("/uprav-zaznam/{kategoria}/{id}")
    public String upravZaznam(@PathVariable("kategoria") String kategoria,
                              @PathVariable("id") long id, Model model) {
    // Kontrola kategorie nacitanie a uprava existujuceho produktu
        KategoriaProduktu kategoriaProduktu;
        try {
            kategoriaProduktu = KategoriaProduktu.valueOf(kategoria.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "redirect:/error";
        }
        // Ziskanie sluzby pro danu kategoriu
        ProduktService<? extends Produkt> service = serviceMap.get(kategoriaProduktu);
        if (service == null) {
            return "redirect:/error";
        }
        // Ziskanie produktu podla ID
        Produkt produkt = service.ziskajProduktPodlaID(id);
        if (produkt == null) {
            return "redirect:/error";
        }
        // Pridanie produktu do modelu pre upravu
        model.addAttribute("upravZaznam", produkt);
        return "uprav-zaznam";
    }

    //Vymaze zaznam ziskava produkt podla kategorie a ID, vymaze ho z databazy
    @GetMapping("/vymaz-zaznam/{kategoria}/{id}")
    public String vymazZaznam(@PathVariable String kategoria, @PathVariable Long id) {
        try {
            KategoriaProduktu kategoriaProduktu = KategoriaProduktu.valueOf(kategoria.toUpperCase());
            ProduktService<? extends Produkt> service = serviceMap.get(kategoriaProduktu);
            if (service != null) {
                service.odstranProduktPodlaID(id);
            }
        } catch (IllegalArgumentException e) {
            return "redirect:/error";
        }

        return "redirect:/zobraz-vsetky-zaznamy";
    }

    // Pomocna metoda vytvara produkt podla kategorie
    private Produkt vytvorProdukt(KategoriaProduktu kategoria) {
        switch (kategoria) {
            case TRICKO:
                return new Tricko();
            case TOPANKY:
                return new Topanky();
            case NOHAVICE:
                return new Nohavice();
            default:
                throw new IllegalArgumentException("Neznáma kategória produktu: " + kategoria);
        }
    }

    // Pomocma metoda kopiruje data z formulara do entity
    private void copyProduktFormToEntity(ProduktForm form, Produkt entity) {
        entity.setNazov(form.getNazov());
        entity.setFarba(form.getFarba());
        entity.setVelkost(form.getVelkost());
        entity.setCena(form.getCena());
        entity.setKategoria(form.getKategoria());
        entity.setPocet(form.getPocet());
    }

    @RequestMapping("/error")
    public String zobrazChybovuStranku() {
        return "zobraz-chybovu-stranku";
    }
}