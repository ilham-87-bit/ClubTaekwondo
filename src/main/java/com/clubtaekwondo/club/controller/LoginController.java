package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.Token;
import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.repository.UserRepository;
import com.clubtaekwondo.club.repository.UserRoleRepository;
import com.clubtaekwondo.club.service.TokenService;
import com.clubtaekwondo.club.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;


    @Autowired
    private TokenService tokenService;

    @ModelAttribute("user")
    public User newUser() {

        return new User();
    }

    @GetMapping("/login")
    public String loginGet() {
        return ("login");
    }

    @PostMapping("/login")
    public String loginPost() {

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof User) {

            User user = (User) authentication;
            if (user.getUserRole().getRole().equals(Role.ADMIN.getAlea())) {
                return "redirect:/admin/indexAdmin";
            }
        }
        return ("index");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping(value = "/addAdmin")
    public String addAdmin(Model model) {
        User user = new User();
        user.setEmail("admin@takewando.com");
//        user.setLogin("admin");
        user.setPassword(encoder.encode("admin"));
        try {
            userService.save(user, Role.ADMIN);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            System.out.println(ex.getMessage());
        }

        return ("indexAdmin");
    }

    @GetMapping("/inscription")
    public String inscriptionGet() {
        return ("inscription");
    }

    @PostMapping("/inscription")
    public String inscriptionPost(@ModelAttribute("user") @Valid User user, BindingResult result) {
    /*
    BindingResult interface qui permet d'appliquer un validateur et de lier les résultats avec les vues
    */
        if (result.hasErrors()) {
            return new ModelAndView("inscription");
        }
        try {
            String password = user.getPassword();
            String hashedPassword = encoder.encode(password);
            user.setPassword(hashedPassword);
            user = userService.save(user, Role.USER);

            String tokenString = generateNewToken();
            // create a new Token in the database

            Token token = new Token();
            token.setToken(tokenString);
            token.setUser(user);

            token = tokenService.save(token);


            String s = urlFromMethod(token.getToken(), user.getEmail());
            SimpleMailMessage mailMessage = mailConstructor.constructResetTokenEmail(s, user);

            mailSender.send(mailMessage);

        } catch (Exception e) {
            return new ModelAndView("inscription.html", "message", user.getEmail() + " existe déjà");
        }
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof User) {

            user = (User) authentication;
            if (user.getUserRole().getRole().equals(Role.ADMIN.getAlea())) {
                return new ModelAndView("redirect:/indexAdmin/html", "messageIngredient", user.getFirstName() + user.getLastName() + " , votre compte a bien été créé");
            }
        }
        return new ModelAndView("redirect:/index/html", "messageIngredient", user.getFirstName() + user.getLastName() + " , votre compte a bien été créé");

    }

    @GetMapping("activate/{login}/{token}")

    public String activate(@PathVariable String token, @PathVariable String login) {

        //TODO complete the validation
        return "index";
    }


    private String urlFromMethod(String token, String login) {
        return MvcUriComponentsBuilder.fromMethodName(LoginController.class,
                "activate", token, login).build().toString();

    }

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
