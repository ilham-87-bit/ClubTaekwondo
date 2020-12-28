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
    private CategoryByCoachService categoryByCoachService;
    @Autowired
    private CategoryBySchoolService categoryBySchoolService;
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

        model.addAttribute("coachList", coachService.getAllCoach());

        return ("adminPart/coach/coachList");
    }

    @GetMapping(value = "/addCoach")
    public String getAddCoach(Model model) {

        List<CategoryBySchool> categoryBySchoolList = categoryBySchoolService.getAllCategoryBySchool();

        model.addAttribute(COACH, new Coach());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("parameters", categoryBySchoolList);
        model.addAttribute("categoryList", categoriesService.getAllCategory());


        return ("adminPart/coach/addCoach");
    }

    @PostMapping(value = "/addCoach")
    public String addCoach(Coach coach, School school, City city, @RequestParam("categoriesList[]") List<Categories> categoriesList, Model model) {
        List<Categories> listAdd = new ArrayList<>();

        @Valid User finalUser = coach;
        Optional<User> firstUser = userService.getAllUser().stream().filter(u -> u.getEmail().equals(finalUser.getEmail()))
                .findFirst();
        if (firstUser.isPresent()) {
            List<CategoryBySchool> categoryBySchoolList = categoryBySchoolService.getAllCategoryBySchool();
            model.addAttribute("messageError", "le e-mail existe déja");
            model.addAttribute(COACH, new Coach());
            model.addAttribute(ADDRESS, new Address());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("schoolList", schoolService.getAllSchool());
            model.addAttribute("parameters", categoryBySchoolList);
            model.addAttribute("categoryList", categoriesService.getAllCategory());

            return ("adminPart/coach/addCoach");
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
        coachService.saveCoach(coach);
        Role role = Role.COACH;
        coach = (Coach) userService.save(coach, role);

        for (Categories categories : categoriesList) {
            CategoryByCoach categoryByCoach = new CategoryByCoach();
            categoryByCoach.setCoach(coach);
            categoryByCoach.setCategories(categories);
            categoryByCoachService.save(categoryByCoach);
            listAdd.add(categoryByCoach.getCategories());
        }
        Token token = new Token();
        String tokenString = LoginController.generateNewToken();
        // create a new Token in the database

        token.setToken(tokenString);
        token.setUser(coach);
        token.setType(TokenType.NEW_ACCOUNT);
        token = tokenService.save(token);
//        String s = MvcUriComponentsBuilder.fromMethodName(CoachController.class,
//                "activateCoach", token.getToken(), coach.getEmail(), null, null).build().toString();
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

        Coach coach = coachService.findById(id);
        User user = userService.findByLogin(coach.getEmail());
        user.setUserRole(userRoleService.findByRole(Role.USER));

        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getId().equals(coach.getId())) {
                categoryByCoachService.delete(categoryByCoach);
            }
        }
//        coachService.deleteCoach(coach);

        model.addAttribute("coachList", coachService.getAllCoach());

        return "redirect:/admin/coach/coachList";
    }

    @GetMapping(value = "/edit/{coach}")
    public String coachDetails(@PathVariable("coach") Long id, Model model) {

        List<Categories> list = new ArrayList<>();
        Coach coach = coachService.findById(id);

        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getId().equals(coach.getId())) {
                list.add(categoryByCoach.getCategories());
            }
        }
        List<CategoryBySchool> categoryBySchoolList = categoryBySchoolService.getAllCategoryBySchool();

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("listCat", list);
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("parameters", categoryBySchoolList);

        return "adminPart/coach/addCoach";
    }

    @PostMapping(value = "/edit")
    public String editCoach(Coach coach, School school, City city, @RequestParam("categoriesList[]") List<Categories> categoriesList, Model model) {

        List<Categories> listCat = new ArrayList<>();

        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getIdSchool());
        coach.setSchool(s);
        addressService.save(coach.getAddress());
        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getId() == coach.getId()) {
                categoryByCoachService.delete(categoryByCoach);
            }
        }
        for (Categories categories : categoriesList) {
            CategoryByCoach categoryByCoach = new CategoryByCoach();
            categoryByCoach.setCoach(coach);
            categoryByCoach.setCategories(categories);
            categoryByCoachService.save(categoryByCoach);
            listCat.add(categoryByCoach.getCategories());
        }
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
