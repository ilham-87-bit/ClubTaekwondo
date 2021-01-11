package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.controller.LoginController;
import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("admin/coach")
public class CoachController {

    private static final String COACH = "coach";
    private static final String SCHOOL = "school";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CoachService coachService;
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRoleService userRoleService;


    @GetMapping(value = "/coachList")
    public String coachList(Model model) {

        List<Coach> coachList = coachService.getAllCoach().stream().filter(coach1 -> Role.COACH.getAlea().equals(coach1.getUserRole().getRole())).collect(Collectors.toList());
        model.addAttribute("coachList", coachList);

        return ("adminPart/coach/coachList");
    }

    @GetMapping(value = "/addCoach")
    public String getAddCoach(Model model) {

        model.addAttribute(COACH, new Coach());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());


        return ("adminPart/coach/addCoach");
    }

    @PostMapping(value = "/addCoach")
    public String addCoach(Coach coach, School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model) {
        List<Categories> listAdd = new ArrayList<>();

        @Valid User finalUser = coach;
        Optional<User> firstUser = userService.getAllUser().stream().filter(u -> u.getEmail().equals(finalUser.getEmail()))
                .findFirst();
        if (firstUser.isPresent()) {
            City c = cityService.findById(city.getIdCity());
            coach.getAddress().setCity(c);
            School s = schoolService.findById(school.getIdSchool());
            coach.setSchool(s);

            Coach finalCoach = (Coach) firstUser.get();
            Coach finalCoach1 = finalCoach;
            Optional<Address> firstAddress = addressService.getAllAddress().stream().filter(a -> a.getStreet().equals(finalCoach1.getAddress().getStreet()) && a.getNumber().equals(finalCoach1.getAddress().getNumber()) && a.getCity().equals(finalCoach1.getAddress().getCity()))
                    .findFirst();
            if (firstAddress.isPresent()) {
                finalCoach.setAddress(firstAddress.get());
            } else {
                addressService.save(coach.getAddress());
            }

            List<UserRole> userRoleList = userRoleService.getAllUserRole();
            for (UserRole userRole : userRoleList) {
                if (userRole.getRole().equals(Role.COACH.getAlea())) {
                    finalCoach.setUserRole(userRole);
                }
            }

            finalCoach.setCategoriesList(categoryList);
            finalCoach = coachService.saveCoach(finalCoach);

            model.addAttribute(COACH, finalCoach);
            model.addAttribute(ADDRESS, finalCoach.getAddress());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("schoolList", schoolService.getAllSchool());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("listCat", finalCoach.getCategoriesList());

            return "redirect:/admin/coach/coachList";
        }
        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getIdSchool());
        coach.setSchool(s);

        Coach finalCoach = coach;
        Optional<Address> firstAddress = addressService.getAllAddress().stream().filter(a -> a.getStreet().equals(finalCoach.getAddress().getStreet()) && a.getNumber().equals(finalCoach.getAddress().getNumber()) && a.getCity().equals(finalCoach.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            coach.setAddress(firstAddress.get());
        } else {
            addressService.save(coach.getAddress());
        }
        coach.setCategoriesList(categoryList);
        coachService.saveCoach(coach);
        Role role = Role.COACH;
        coach = (Coach) userService.save(coach, role);

        Token token = new Token();
        String tokenString = LoginController.generateNewToken();
        // create a new Token in the database

        token.setToken(tokenString);
        token.setUser(coach);
        token.setType(TokenType.NEW_ACCOUNT);
        token = tokenService.save(token);
        String co = MvcUriComponentsBuilder.fromMethodName(LoginController.class,
                "choosePassword", token.getToken(), coach.getEmail(), null).build().toString();
        SimpleMailMessage mailMessage = mailConstructor.constructResetTokenEmail(co, coach);

        mailSender.send(mailMessage);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", listAdd);

        return "redirect:/admin/coach/coachList";

    }

    @GetMapping(value = "/delete/{coach}")
    public String deleteCoach(@PathVariable("coach") Long id, Model model) {

        Coach coach = (Coach) coachService.findById(id);
        User user = userService.findByLogin(coach.getEmail());
        user.setUserRole(userRoleService.findByRole(Role.USER));

        List<Coach> coachList = coachService.getAllCoach().stream().filter(coach1 -> Role.COACH.equals(coach1.getUserRole())).collect(Collectors.toList());
        model.addAttribute("coachList", coachList);

        return "redirect:/admin/coach/coachList";
    }

    @GetMapping(value = "/edit/{coach}")
    public String coachDetails(@PathVariable("coach") Long id, Model model) {

        List<Categories> list = new ArrayList<>();
        Coach coach = (Coach) coachService.findById(id);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("listCat", coach.getCategoriesList());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());


        return "adminPart/coach/addCoach";
    }

    @PostMapping(value = "/edit")
    public String editCoach(Coach coach, School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model) {

        List<Categories> listCat = new ArrayList<>();

        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getIdSchool());
        coach.setSchool(s);
        addressService.save(coach.getAddress());

        coach.setCategoriesList(categoryList);
        coachService.saveCoach(coach);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", listCat);

        return "redirect:/admin/coach/coachList";
    }
}
