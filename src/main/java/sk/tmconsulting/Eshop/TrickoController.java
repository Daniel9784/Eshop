package sk.tmconsulting.Eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TrickoController implements ErrorController {

    @Autowired
    private TrickoService trickoService;

    @Autowired
    private TopankyService topankyService;

    @GetMapping("/")
    public String uvodnaStranka(){
        return "index";
    }

    @GetMapping("/zobraz-vsetky-zaznamy")
    public String zobrazVsetkyZaznamy(Model model){
        List<Produkt> vsetkyProdukty = new ArrayList<>();
        vsetkyProdukty.addAll(trickoService.ziskajVsetkyTricka());
        vsetkyProdukty.addAll(topankyService.ziskajVsetkyTopanky());
        model.addAttribute("vsetkyZaznamy", vsetkyProdukty);
        return "zobraz-vsetky-zaznamy";
    }
    @GetMapping("/pridaj-zaznam")
    public String pridajZaznam(Model model) {
        model.addAttribute("pridajZaznam", new Tricko());
        return "pridaj-zaznam";
    }

    @PostMapping("/uloz-zaznam")
    public String ulozZaznam(@Valid @ModelAttribute("upravZaznam") ProduktForm produktForm,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "uprav-zaznam";
        }
        if (produktForm.getProduktID() != 0) {
            Tricko existingTricko = trickoService.ziskajTrickoPodlaID(produktForm.getProduktID());
            Topanky existingTopanky = topankyService.ziskajTopankyPodlaID(produktForm.getProduktID());

            // Pri zmene kategorii sa zmaze produkt zo starej a vytvori znova
            if (produktForm.getKategoria() == KategoriaProduktu.TRICKO) {
                if (existingTopanky != null) {
                    // Z topanky na tricko zmena
                    topankyService.odstranTopankyPodlaID(produktForm.getProduktID());
                    Tricko noveTricko = new Tricko();
                    copyProduktFormToEntity(produktForm, noveTricko);
                    trickoService.ulozTricko(noveTricko);
                } else if (existingTricko != null) {
                    // Update pre tricko
                    copyProduktFormToEntity(produktForm, existingTricko);
                    trickoService.ulozTricko(existingTricko);
                }
            } else if (produktForm.getKategoria() == KategoriaProduktu.TOPANKY) {
                if (existingTricko != null) {
                    // Z tricka na topanky zmena
                    trickoService.odstranTrickoPodlaID(produktForm.getProduktID());
                    Topanky noveTopanky = new Topanky();
                    copyProduktFormToEntity(produktForm, noveTopanky);
                    topankyService.ulozTopanky(noveTopanky);
                } else if (existingTopanky != null) {
                    //Updatne topanky
                    copyProduktFormToEntity(produktForm, existingTopanky);
                    topankyService.ulozTopanky(existingTopanky);
                }
            }
        } else {
            // Vyvara novy produkt
            if (produktForm.getKategoria() == KategoriaProduktu.TRICKO) {
                Tricko tricko = new Tricko();
                copyProduktFormToEntity(produktForm, tricko);
                trickoService.ulozTricko(tricko);
            } else if (produktForm.getKategoria() == KategoriaProduktu.TOPANKY) {
                Topanky topanky = new Topanky();
                copyProduktFormToEntity(produktForm, topanky);
                topankyService.ulozTopanky(topanky);
            }
        }

        return "redirect:/zobraz-vsetky-zaznamy";
    }

    // Helper method to copy form data to entity
    private void copyProduktFormToEntity(ProduktForm form, Produkt entity) {
        entity.setNazov(form.getNazov());
        entity.setFarba(form.getFarba());
        entity.setVelkost(form.getVelkost());
        entity.setCena(form.getCena());
        entity.setKategoria(form.getKategoria());
    }

    @GetMapping("/uprav-zaznam/{kategoria}/{id}")
    public String upravZaznam(@PathVariable("kategoria") String kategoria,
                              @PathVariable("id") long id, Model model) {
        Produkt produkt = null;

        if ("tricko".equalsIgnoreCase(kategoria)) {
            produkt = trickoService.ziskajTrickoPodlaID(id);
        } else if ("topanky".equalsIgnoreCase(kategoria)) {
            produkt = topankyService.ziskajTopankyPodlaID(id);
        }

        if (produkt == null) {
            return "redirect:/error";
        }

        model.addAttribute("upravZaznam", produkt);
        return "uprav-zaznam";
    }



    @GetMapping("/vymaz-zaznam/{kategoria}/{id}")
    public String vymazZaznam(@PathVariable String kategoria, @PathVariable Long id) {
        if ("tricko".equalsIgnoreCase(kategoria)) {
            trickoService.odstranTrickoPodlaID(id);
        } else if ("topanky".equalsIgnoreCase(kategoria)) {
            topankyService.odstranTopankyPodlaID(id);
        }
        return "redirect:/zobraz-vsetky-zaznamy";
    }


    @RequestMapping("/error") // adresa pre stranku 404, zobrazi sa vtedy, ked nie je podstranka (uri) najdena
    public String zobrazChybovuStranku() {
        return "zobraz-chybovu-stranku";
    }

}
