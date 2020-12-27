package com.clubtaekwondo.club.controller;


import com.clubtaekwondo.club.controller.user.StudentDTO;
import com.clubtaekwondo.club.controller.user.SubscriptionDTO;
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

        model.addAttribute("subscription", new Subscription());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("subscriptions", subscriptionService.getCart());

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
            model.addAttribute("subscriptions", subscriptionService.getCart());

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
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/addSubscriptionPart2/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
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
        model.addAttribute("subscriptions", subscriptionService.getCart());
        return "user/addSubscriptionPart2";
    }

    @PostMapping(value = "/addSubscriptionPart2/{subscription}")
    public String addSubscriptionPart2(@PathVariable("subscription") Long id, SubscriptionDTO subscriptionDTO, SubscriptionType subscriptionT, SubscriptionPeriod subscriptionP, Model model, Locale locale) {
        try {
            Subscription subscription = subscriptionService.findById(id);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", locale);
            String dateString = subscriptionDTO.getStartDate();
            Date date = sdf.parse(dateString);
            subscription.setStartDate(date);
            String dateStringEnd = subscriptionDTO.getEndDate();
            Date d = sdf.parse(dateStringEnd);
            subscription.setEndDate(d);
            subscription.setSubscriptionType(subscriptionTypeService.findById(subscriptionT.getIdType()));
            subscription.setSubscriptionPeriod(subscriptionPeriodService.findById(subscriptionP.getId()));
            subscriptionService.save(subscription);
            Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getCategory().equals(subscription.getCategories()) && t.getPeriod().equals(subscription.getSubscriptionPeriod()) && t.getType().equals(subscription.getSubscriptionType()))
                    .findFirst();
            if (firstTariff.isPresent()) {
                Tariff tariff = firstTariff.get();
                subscription.setPrice(tariff.getPrix());
                subscription.setTotalPrice(tariff.getPrix() + subscription.getExpenses());
                subscriptionService.save(subscription);
            }
//            Long idTariff = tariffService.getOneTariff(subscription.getCategories(), subscription.getSubscriptionPeriod(), subscription.getSubscriptionType());
//            Tariff tariff = tariffService.getTariffById(idTariff);
//            subscription.setTotalPrice(tariff.getPrix() + subscription.getExpenses());
//            subscriptionService.save(subscription);

            model.addAttribute("subscription", subscription);
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/addStudent/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "/summary/{subscription}")
    public String getSummary(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);

        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/summary";
    }

    @PostMapping(value = "/confirmSubscription/{subscription}")
    public String confirmSubsctiption(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);
        // put the sbscription in the chart
        subscription.setSubscriptionStatus(SubscriptionStatus.CHART);
        subscriptionService.save(subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        return "redirect:/index";
    }

    @GetMapping(value = "/delete/{subscription}")
    public String deleteSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        Student student = subscription.getStudent();
        List<StudentRelation> studentRelations = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelations) {
            if (studentRelation.getStudent().equals(student)) {
                studentRelationService.delete(studentRelation);
            }
        }
        studentService.delete(student);
        subscriptionService.delete(subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "redirect:/index";
    }

    @GetMapping(value = "/edit/{subscription}")
    public String getEditSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/subscription";
    }

    @GetMapping(value = "/editSubscription/{subscription}/{category}")
    public String getEditSubscription(@PathVariable("subscription") Long idSub, @PathVariable("category") Long id, Model model) {


        List<School> schoolList = new ArrayList<>();
        Subscription subscription = subscriptionService.findById(idSub);
        Categories categories = categoriesService.findById(id);

        List<CategoryBySchool> categoryBySchools = categoryBySchoolService.getAllCategoryBySchool();
        for (CategoryBySchool categoryBySchool : categoryBySchools) {
            if (categoryBySchool.getCat().getIdCategory().equals(categories.getIdCategory())) {
                schoolList.add(categoryBySchool.getSchool());
            }
        }
        model.addAttribute("subscription", subscription);
        model.addAttribute("user", subscription.getUser());
        model.addAttribute("cat", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("schoolList", schoolList);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/addSubscription1";
    }

    @PostMapping(value = "/editSubscription/{subscription}/{category}")
    public String editSubscription(@PathVariable("subscription") Long idSub, @PathVariable("category") Long id, School school, Model model) {
        try {
            Categories c = categoriesService.findById(id);
            Subscription subscription = subscriptionService.findById(idSub);
            subscription.setCategories(c);
            subscription.setSchool(schoolService.findById(school.getId()));
            subscription.setValidation(false);
            subscription.setExpenses(expenses);

            subscriptionService.save(subscription);

            model.addAttribute("subscription", subscription);
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/addSubscriptionPart2/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/subscription";
        }
    }
}
