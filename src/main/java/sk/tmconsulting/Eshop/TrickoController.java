package sk.tmconsulting.Eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;


@Controller
public class TrickoController implements ErrorController {

    @Autowired
    private TrickoService trickoService;

    @GetMapping("/")
    public String uvodnaStranka(){
        return "index";
    }

    @GetMapping("/zobraz-vsetky-zaznamy")
    public String zobrazVsetkyZaznamy(Model model){
        model.addAttribute("vsetkyZaznamy", trickoService.ziskajVsetkyTricka());
        return "zobraz-vsetky-zaznamy";
    }
    @GetMapping("/pridaj-zaznam")
    public String pridajZaznam(Model model) {
        model.addAttribute("pridajZaznam", new Tricko());
        return "pridaj-zaznam";
    }

    @PostMapping("/uloz-zaznam")
    public String ulozZaznam(@Valid @ModelAttribute Tricko tricko, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if (tricko.getProduktID() == 0) {
                model.addAttribute("pridajZaznam", tricko);
                return "pridaj-zaznam";
            } else {
                model.addAttribute("upravZaznam", tricko);
                return "uprav-zaznam";
            }
        }
        trickoService.ulozTricko(tricko);
        return "redirect:/zobraz-vsetky-zaznamy";
    }

    @GetMapping("/uprav-zaznam/{id}")
    public String upravZaznam(@PathVariable("id") long id, Model model) {
        Tricko tricko = trickoService.ziskajTrickoPodlaID(id);
        model.addAttribute("upravZaznam", tricko);
        return "uprav-zaznam";
    }

    @GetMapping("/vymaz-zaznam/{id}")
    public String vymazZaznam(@PathVariable("id") long id) {
        trickoService.odstranTrickoPodlaID(id);
        return "redirect:/zobraz-vsetky-zaznamy";
    }

    @RequestMapping("/error") // adresa pre stranku 404, zobrazi sa vtedy, ked nie je podstranka (uri) najdena
    public String zobrazChybovuStranku() {
        return "zobraz-chybovu-stranku";
    }

}
