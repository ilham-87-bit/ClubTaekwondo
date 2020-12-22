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

    private float expenses = 40;

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
    @Autowired
    private ContactPersonService contactPersonService;
    @Autowired
    private StudentRelationService studentRelationService;
    @Autowired
    private TariffService tariffService;


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
            Subscription newSubscription = subscriptionService.save(subscription);
            Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getCategory().equals(newSubscription.getCategories()) && t.getPeriod().equals(newSubscription.getSubscriptionPeriod()) && t.getType().equals(newSubscription.getSubscriptionType()))
                    .findFirst();
            if (firstTariff.isPresent()) {
                Tariff tariff = firstTariff.get();
                subscription.setTotalPrice(tariff.getPrix() + subscription.getExpenses());
                subscriptionService.save(subscription);
            }
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
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);
//            url = "/addContactPerson/" + student.getIdStudent();

            return "redirect:/addContactPerson/" + student.getIdStudent();

        } catch (Exception e) {
//            url = "/addStudent/" + subscription.getIdSubscription();
            model.addAttribute("messageError", "erreur, veuillez essayer");
            return "redirect:/addStudent/" + subscription.getIdSubscription();
        }
    }

    @GetMapping(value = "/addContactPerson/{student}")
    public String getAddContactPerson(@PathVariable("student") Long id, Model model) {
        Subscription s = new Subscription();
        Student student = studentService.findById(id);
//        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
//        for(Subscription subscription : subscriptionList){
//            if(subscription.getStudent().getIdStudent().equals(student.getIdStudent()) && subscription.getStudent() != null){
//                s = subscription;
//            }
//        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", new StudentRelation());
        model.addAttribute("contactPerson", new ContactPerson());

        return "user/addContactPerson";
    }

    @PostMapping(value = "/addContactPerson/{student}")
    public String addContactPerson(@PathVariable("student") Long id, StudentRelation studentRelation, ContactPerson contactPerson, Model model) {
        Subscription s = new Subscription();

        Student student = studentService.findById(id);
        ContactPerson c = contactPersonService.save(contactPerson);
        studentRelation.setStudent(student);
        studentRelation.setContactPerson(c);
        StudentRelation sr = studentRelationService.save(studentRelation);

        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
//        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
//        for (Subscription subscription : subscriptionList) {
//            if (subscription.getStudent().getIdStudent() == student.getIdStudent()) {
//                s = subscription;
//            }
//        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", studentRelation);
        model.addAttribute("contactPerson", c);

        return "redirect:/summary/" + s.getIdSubscription();
    }

    @GetMapping(value = "/summary/{subscription}")
    public String getSummary(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);

        model.addAttribute("subscription", subscription);

        return "user/summary";
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
