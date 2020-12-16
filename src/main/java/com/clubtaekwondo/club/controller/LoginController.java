package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.repository.UserRepository;
import com.clubtaekwondo.club.repository.UserRoleRepository;
import com.clubtaekwondo.club.service.TokenService;
import com.clubtaekwondo.club.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.*;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    public final String MAIL_ADMIN = "clubtaekwondo.asbl@gmail.com";
//    Map<String,String> map = new HashMap<>();

    @ModelAttribute("user")
    public User newUser() {

        return new User();
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("user", new User());
        return ("login");
    }

    @PostMapping("/login")
    public String loginPost(Model model) {
        try {
            Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (authentication instanceof User) {

                User user = (User) authentication;
                if (user.getUserRole().getRole().equals(Role.ADMIN.getAlea())) {
                    return "redirect:/admin/indexAdmin";
                }
                model.addAttribute("user", user);
            }
            return ("index");
        } catch (Exception e) {
            model.addAttribute("messageError", "Veuillez bien vérifier les champs !");
            return ("index");
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("messageAlert", "Votre compte n'est pas encore activer, Veuillez verifier vos e-mail ");
        return "redirect:/index";
    }

    @GetMapping("/inscription")
    public String inscriptionGet() {

        return ("inscription");
    }

    @PostMapping("/inscription")
    public String inscriptionPost(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
    /*
    BindingResult interface qui permet d'appliquer un validateur et de lier les résultats avec les vues
    */
        if (result.hasErrors()) {
            return "inscription";
        }
        try {
            Token token = new Token();
            String password = user.getPassword();
            token.setPass(password);
            String hashedPassword = encoder.encode(password);
            user.setPassword(hashedPassword);
            @Valid User finalUser = user;
            Optional<User> firstUser = userService.getAllUser().stream().filter(u -> u.getEmail().equals(finalUser.getEmail()))
                    .findFirst();
            if (firstUser.isPresent()) {
                result.rejectValue("email", "email", "le e-mail existe déja");
                return "inscription";
            }
            Role role = user.getEmail().equals(MAIL_ADMIN) ? Role.ADMIN : Role.USER;
            user = userService.save(user, role);

//            map.put(user.getEmail(), password);

            String tokenString = generateNewToken();
            // create a new Token in the database

            token.setToken(tokenString);
            token.setUser(user);
            token.setType(TokenType.NEW_ACCOUNT);
            token = tokenService.save(token);
//            String s = urlFromMethod(token.getToken(), user.getEmail(), "activate");
            String s = MvcUriComponentsBuilder.fromMethodName(LoginController.class,
                    "activate", token.getToken(), user.getEmail(), null, null).build().toString();
            SimpleMailMessage mailMessage = mailConstructor.constructResetTokenEmail(s, user);

            mailSender.send(mailMessage);
            model.addAttribute("user", user);

        } catch (Exception e) {
            model.addAttribute("messageError", "Veuillez bien vérifier les champs !");
            return "inscription";
        }

        return "validation";
    }

    @GetMapping("/forgetPassword")
    public String forgetPasswordGet() {

        return ("forgetPassword");
    }

    @PostMapping("/forgetPassword")
    public String forgetPasswordPost(@ModelAttribute("user") @Valid User user) {
        User persisted = userService.findByLogin(user.getEmail());
        if (persisted != null) {
            String tokenString = generateNewToken();
            // create a new Token in the database

            Token token = new Token();
            token.setToken(tokenString);
            token.setUser(persisted);
            token.setType(TokenType.FORGET_PASSWORD);
            token = tokenService.save(token);


            String s = MvcUriComponentsBuilder.fromMethodName(LoginController.class,
                    "resetPassword", token.getToken(), user.getEmail(), null).build().toString();
            SimpleMailMessage mailMessage = mailConstructor.constructResetPasswordEmail(s, user);

            mailSender.send(mailMessage);
            System.out.println("Sent Mail" + mailMessage.getText());
        }
        return "redirect:index";
    }

    @GetMapping("resetPassword/{login}/{token}")

    public String resetPassword(@PathVariable String token, @PathVariable String login, Model model) {

        User user = userService.findByLogin(login);
        if (user != null && user.getTokens() != null) {

            Optional<Token> first = user.getTokens().stream()
                    .filter(tok -> tok.getType() == TokenType.FORGET_PASSWORD)
                    .filter(tok -> tok.getToken().equals(token))
                    .findFirst();
            if (first.isPresent()) {

                User temp = new User();
                temp.setTokens(new ArrayList<>());
                temp.getTokens().add(first.get());
                temp.setEmail(user.getEmail());
                model.addAttribute("user", temp);

                return "resetPassword";
            }
        }
        System.out.println("Token or mail are not correct ");
        //TODO complete the validation
        return "redirect:/index";
    }

    @PostMapping("resetPassword")
    public String resetPasswordPost(@ModelAttribute User user, Model model) {

        User persited = userService.findByLogin(user.getEmail());
        String token = user.getTokens() != null && user.getTokens().size() == 1 ? user.getTokens().get(0).getToken() : "ND";
        if (persited != null && persited.getTokens() != null) {

            Optional<Token> first = persited.getTokens().stream()
                    .filter(tok -> tok.getType() == TokenType.FORGET_PASSWORD)
                    .filter(tok -> tok.getToken().equals(token))
                    .findFirst();
            if (first.isPresent()) {

                persited.setPassword(encoder.encode(user.getPassword()));
                userService.save(persited);
                // TODO : Remove all tokens
                tokenService.deleteAllToken(persited.getTokens());
                System.out.println("Password has been modified ");
            }
        }
        //TODO complete the validation
        return "redirect:/index";
    }

    @GetMapping("activate/{login}/{token}")
    public String activate(@PathVariable String token, @PathVariable String login, Model model, HttpServletRequest req) {

        String pass = null;
        User user = userService.findByLogin(login);
        if (user != null && !user.isActive() && user.getTokens() != null) {

            Optional<Token> first = user.getTokens().stream()
                    .filter(tok -> tok.getType() == TokenType.NEW_ACCOUNT)
                    .filter(tok -> tok.getToken().equals(token))
                    .findFirst();
            if (first.isPresent()) {
                user.setActive(true);
                userService.save(user);
//                if(map.containsKey(user.getEmail())){
//                    pass = map.get(user.getEmail());
//                }
                UsernamePasswordAuthenticationToken authReq
                        = new UsernamePasswordAuthenticationToken(user.getUsername(), first.get().getPass());
                Authentication auth = authenticationManager.authenticate(authReq);
                // TODO : Remove all tokens
                tokenService.deleteAllToken(user.getTokens());

                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                HttpSession session = req.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                System.out.println("The account has been activated ");
            }
        }
        if (user == null || !user.isActive()) {
            System.out.println("Activation of user has been failed ");
            model.addAttribute("messageError", "Veuillez bien vérifier les champs !");
            return "inscription";
        }
        return "index";
    }


    private String urlFromMethod(String token, String login, String method) {
        return MvcUriComponentsBuilder.fromMethodName(LoginController.class,
                method, token, login).build().toString();

    }

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
