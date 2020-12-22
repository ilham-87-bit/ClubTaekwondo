package com.clubtaekwondo.club.controller;


import com.clubtaekwondo.club.controller.user.StudentDTO;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SubscriptionController {

    private float expenses = 35;

    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SubscriptionPeriodService subscriptionPeriodService;
    @Autowired
    private SubscriptionTypeService subscriptionTypeService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private CategoryBySchoolService categoryBySchoolService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CityService cityService;
    @Autowired
    private AddressService addressService;


    @GetMapping(value = "/subscription")
    public String getSubscription(Model model) {

        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "user/subscription";
    }

    @GetMapping(value = "/addSubscription/{category}")
    public String getAddSubscription(@PathVariable("category") Long id, Model model) {

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<School> schoolList = new ArrayList<>();
        if (authentication instanceof User) {
            User user = (User) authentication;
            model.addAttribute("user", user);
            Categories categories = categoriesService.findById(id);

            List<CategoryBySchool> categoryBySchools = categoryBySchoolService.getAllCategoryBySchool();
            for (CategoryBySchool categoryBySchool : categoryBySchools) {
                if (categoryBySchool.getCat().getIdCategory().equals(categories.getIdCategory())) {
                    schoolList.add(categoryBySchool.getSchool());
                }
            }
            model.addAttribute("cat", categories);
            model.addAttribute("subscription", new Subscription());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("schoolList", schoolList);

            return "user/addSubscription1";
        } else {
            model.addAttribute("messageWarning", "Vous devez se connecter/s'inscrire pour pouvoir acheter/prolonger vos abonnements.");
            return "login";
        }

    }

    @PostMapping(value = "/addSubscription/{category}")
    public String addSubscription(@PathVariable("category") Long id, Subscription subscription, School school, Model model) {
        try {
            Categories c = categoriesService.findById(id);
            subscription.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            subscription.setCategories(c);
            subscription.setSchool(schoolService.findById(school.getId()));
            subscription.setValidation(false);
            subscription.setExpenses(expenses);

            subscriptionService.save(subscription);

            model.addAttribute("subscription", subscription);
            String url = "/addSubscriptionPart2/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "/addSubscriptionPart2/{subscription}")
    public String getSubscriptionPart2(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        Map<Long, Integer> parameters = new HashMap<>();
        List<SubscriptionPeriod> periodList = subscriptionPeriodService.getAllPeriod();
        for (SubscriptionPeriod subscriptionPeriod : periodList) {
            parameters.put(subscriptionPeriod.getId(), subscriptionPeriod.getNbrMonth());
        }

        model.addAttribute("subscription", subscription);
        model.addAttribute("periodList", periodList);
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("parameters", parameters);
        return "user/addSubscriptionPart2";
    }

    @PostMapping(value = "/addSubscriptionPart2/{subscription}")
    public String addSubscriptionPart2(@PathVariable("subscription") Long id, Subscription subs, SubscriptionType subscriptionT, SubscriptionPeriod subscriptionP, Model model) {
        try {
            Subscription subscription = subscriptionService.findById(id);
            subscription.setStartDate(subs.getStartDate());
            subscription.setEndDate(subs.getEndDate());
            subscription.setSubscriptionType(subscriptionTypeService.findById(subscriptionT.getIdType()));
            subscription.setSubscriptionPeriod(subscriptionPeriodService.findById(subscriptionP.getId()));
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
            String url = "/addStudent/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "/addStudent/{subscription}")
    public String getAddStudent(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", new Student());
            model.addAttribute("cityList", cityService.getAllCity());

            return "user/addStudent";
        } catch (Exception e) {
            String url = "/addSubscriptionPart2/" + subscription.getIdSubscription();
            model.addAttribute("messageError", "erreur, veuillez essayer");
            return "redirect:" + url;
        }
    }

    @PostMapping(value = "/addStudent/{subscription}")
    public String addStudent(@PathVariable("subscription") Long id, StudentDTO studentDTO, City city, Model model) {
        Subscription subscription = subscriptionService.findById(id);
        City c = cityService.findById(city.getIdCity());
        try {
            Student student = initializeStudent(studentDTO, city);
            Student s = studentService.save(student);
            subscription.setStudent(s);
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);

            return "user/addContactPerson";
        } catch (Exception e) {
            String url = "/addStudent/" + subscription.getIdSubscription();
            model.addAttribute("messageError", "erreur, veuillez essayer");
            return "redirect:" + url;
        }
    }

    private Student initializeStudent(StudentDTO studentDTO, City c) throws ParseException {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setGsm(new Integer(studentDTO.getGsm()));

        studentDTO.getAddress().setCity(c);
        Optional<Address> firstAddress = addressService.getAllAddress().stream().filter(a -> a.getStreet().equals(studentDTO.getAddress().getStreet()) && a.getNumber().equals(studentDTO.getAddress().getNumber()) && a.getCity().equals(studentDTO.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            studentDTO.setAddress(firstAddress.get());
        } else {
            addressService.save(studentDTO.getAddress());
        }
        student.setAddress(studentDTO.getAddress());
        student.setNationalRegistry(studentDTO.getNationalRegistry());
        student.setFirstName(studentDTO.getFirstName());

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String dateAsString = studentDTO.getBirthDay();
        Date date = sdf.parse(dateAsString);
        student.setBirthDay(date);

        return student;
    }
}
