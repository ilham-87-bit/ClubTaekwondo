package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.clubtaekwondo.club.controller.user.UserController.fileToPath;

@Controller
@RequestMapping("admin/school")
public class SchoolController {

    private static final String SCHOOL = "school";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailConstructor mailConstructor;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private CoachService coachService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping(value = "/schoolList")
    public String schoolList(Model model) {

        List<School> allSchool = schoolService.getAllSchool();
        allSchool.stream().forEach(school -> {
            String newImageName = String.format("schools/%s.jpeg", school.getIdSchool());
            school.setFullUrlImg(fileToPath(storageService.load(newImageName)));
        });
        model.addAttribute("schoolList", allSchool);
        return "adminPart/school/schoolList";
    }

    @GetMapping(value = "/addSchool")
    public String getAddSchool(Model model) {
        model.addAttribute(SCHOOL, new School());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("map", getAllPostalCodeByCity());

        return ("adminPart/school/addSchool");
    }

    @PostMapping(value = "/addSchool")
    public String addSchool(School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model, @RequestParam("file") MultipartFile file) {

        List<Categories> listAdd = new ArrayList<>();

        Optional<School> firstSchool = schoolService.getAllSchool().stream().filter(s -> s.getName().equals(school.getName())).findFirst();
        if (firstSchool.isPresent()) {
            model.addAttribute(SCHOOL, new School());
            model.addAttribute(ADDRESS, new Address());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce école existe déjà.");
            return ("adminPart/school/addSchool");
        } else {
            City c = cityService.findById(city.getIdCity());
            school.getAddress().setCity(c);
            Optional<Address> firstAddress = addressService.getAllAddress().stream()
                    .filter(a -> a.getStreet().equals(school.getAddress().getStreet()) && a.getNumber().equals(school.getAddress().getNumber()) && a.getCity().equals(school.getAddress().getCity()))
                    .findFirst();
            if (firstAddress.isPresent()) {
                school.setAddress(firstAddress.get());
            } else {
                school.setBelongTo(true);
                addressService.save(school.getAddress());
            }
            school.setCategoriesList(categoryList);
            School s = schoolService.save(school);
            if (file != null && !file.isEmpty()) {
                String newImageName = String.format("schools/%s.jpeg", s.getIdSchool());
                storageService.store(file, newImageName);
            }

            model.addAttribute(SCHOOL, school);
            model.addAttribute(ADDRESS, school.getAddress());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("schoolList", schoolService.getAllSchool());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("listCat", s.getCategoriesList());
        }

        return "redirect:/admin/school/schoolList";
    }

    @GetMapping(value = "/delete/{school}")
    public String deleteSchool(@PathVariable("school") Long id, Model model) {
        List<User> userList = new ArrayList<>();
        School s = schoolService.findById(id);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        for (Subscription subscription : subscriptionList) {
            if (subscription.getSchool().getIdSchool().equals(s.getIdSchool())) {
                userList.add(subscription.getUser());
            }
        }
        for (User user : userList) {
            SimpleMailMessage mailMessage = mailConstructor.constructSchoolDelete(user, s);
            mailSender.send(mailMessage);
        }

        s.setBelongTo(false);
        schoolService.save(s);
        List<TimeTable> timeTableList = timeTableService.getAllTimeTable();
        for (TimeTable timeTable : timeTableList) {
            if (timeTable.getS().getIdSchool().equals(s.getIdSchool())) {
                timeTableService.delete(timeTable);
            }
        }
        List<Coach> coaches = coachService.getAllCoach();
        for (Coach coach : coaches) {
            if (coach.getSchool().getIdSchool().equals(s.getIdSchool())) {
                User user = userService.findByLogin(coach.getEmail());
                user.setUserRole(userRoleService.findByRole(Role.USER));
            }
        }

        model.addAttribute("schoolList", schoolService.getAllSchool());

        return "redirect:/admin/school/schoolList";
    }

    @GetMapping(value = "/edit/{school}")
    public String schoolDetails(@PathVariable("school") Long id, Model model) {

        List<Categories> list = new ArrayList<>();

        School school = schoolService.findById(id);

        model.addAttribute(SCHOOL, school);
        model.addAttribute(ADDRESS, school.getAddress());
        model.addAttribute("listCat", school.getCategoriesList());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("map", getAllPostalCodeByCity());

        return "adminPart/school/addSchool";
    }

    @PostMapping(value = "/edit")
    public String editSchool(School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model) {

        List<Categories> listCat = new ArrayList<>();
        Optional<School> firstSchool = schoolService.getAllSchool().stream().filter(s -> s.getName().equals(school.getName()) && !s.getIdSchool().equals(school.getIdSchool())).findFirst();
        if (firstSchool.isPresent()) {
            model.addAttribute(SCHOOL, school);
            model.addAttribute(ADDRESS, school.getAddress());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("listCat", school.getCategoriesList());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce école existe déjà.");
            return ("adminPart/school/addSchool");
        } else {

            City c = cityService.findById(city.getIdCity());
            school.getAddress().setCity(c);
            addressService.save(school.getAddress());
            school.setCategoriesList(categoryList);
            schoolService.save(school);

            model.addAttribute(SCHOOL, school);
        }
        model.addAttribute(ADDRESS, school.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", school.getCategoriesList());

        return "redirect:/admin/school/schoolList";
    }

    public Map<Long, String> getAllPostalCodeByCity() {
        Map<Long, String> map = new HashMap<>();

        List<City> cityList = cityService.getAllCity();
        for (City city : cityList) {
            map.put(city.getIdCity(), city.getPostalCode());
        }
        return map;
    }
}
