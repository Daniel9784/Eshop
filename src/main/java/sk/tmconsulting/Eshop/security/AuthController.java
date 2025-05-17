package sk.tmconsulting.Eshop.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthController(CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        // Kontrola či používateľ už existuje
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Používateľské meno už existuje");
            return "register";
        }
        // Kontrola či sa heslá zhodujú
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Heslá sa nezhodujú");
            return "register";
        }


        // Registrácia nového používateľa s rolou USER
        userDetailsService.registerNewUser(username, password, "USER");
        return "redirect:/login";
    }
}