package sk.tmconsulting.Eshop;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import sk.tmconsulting.Eshop.DataTransfer.ProduktForm;
import sk.tmconsulting.Eshop.Products.*;
import sk.tmconsulting.Eshop.ProductsServices.*;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/zobraz-vsetky-zaznamy")
    public String zobrazVsetkyZaznamy(Model model, Authentication authentication) {
        List<Produkt> vsetkyProdukty = new ArrayList<>();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        serviceMap.values().forEach(service -> {
            List<? extends Produkt> produkty = service.ziskajVsetkyProdukty();
            if (!isAdmin) {
                // Pre bežného používateľa filtrujeme len jeho produkty
                produkty = produkty.stream()
                        .filter(p -> p.getCreatedBy() != null &&
                                p.getCreatedBy().equals(authentication.getName()))
                        .collect(Collectors.toList());
            }
            vsetkyProdukty.addAll(produkty);
        });

        model.addAttribute("vsetkyZaznamy", vsetkyProdukty);
        model.addAttribute("isAdmin", isAdmin);
        return "zobraz-vsetky-zaznamy";
    }


    //Pridaj zaznam formular
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/pridaj-zaznam")
    public String pridajZaznam(Model model) {
        model.addAttribute("pridajZaznam", new ProduktForm());
        return "pridaj-zaznam";
    }

    //Uloz zaznam (spravuje validaciu, kontroluje ci formular neobsahuje chyby a hlada produkt v sluzbach, pri zmene kategorii zmaze stary produkt a vytvori novy)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/uloz-zaznam")
    public String ulozZaznam(@Valid @ModelAttribute("upravZaznam") ProduktForm produktForm,
                             BindingResult bindingResult, Model model,
                             Authentication authentication) {

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
             // Pridanie oneskorenia pred vymazaním
                Thread.sleep(500);

            // Zachováme pôvodného vlastníka
                String povodnyVlastnik = existujuciProdukt.getCreatedBy();

                serviceMap.get(aktualnaKategoria).odstranProduktPodlaID(produktId);

            // Pridanie oneskorenie pred vytvorením noveho
                Thread.sleep(500);

                Produkt novyProdukt = vytvorProdukt(novaKategoria);
                copyProduktFormToEntity(produktForm, novyProdukt);

                // Zachováme pôvodného vlastníka
                novyProdukt.setCreatedBy(povodnyVlastnik);

                ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(novyProdukt);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else if (existujuciProdukt != null) {
            copyProduktFormToEntity(produktForm, existujuciProdukt);

             // Zachováme pôvodného vlastníka ak ho upravuje admin
                if (!authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                existujuciProdukt.setCreatedBy(authentication.getName());
            }
            ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(existujuciProdukt);
        }
    } else {
            // Vytvorenie nového produktu
            Produkt novyProdukt = vytvorProdukt(novaKategoria);
        copyProduktFormToEntity(produktForm, novyProdukt);
        novyProdukt.setCreatedBy(authentication.getName());
        ((ProduktService<Produkt>) serviceMap.get(novaKategoria)).ulozProdukt(novyProdukt);
    }

    if (produktId != 0) {
        return "redirect:/zobraz-vsetky-zaznamy";
    } else {
        return "redirect:/";
    }
}

    // Uprav zaznam ziskava produkt podla kategorie a ID, pridava ho do model pre upravu
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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

    // Pomocma metoda kopiruje data z formulara(produktform)  do entity produkt
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