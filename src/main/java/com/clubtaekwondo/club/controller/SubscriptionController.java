package com.clubtaekwondo.club.controller;


import com.clubtaekwondo.club.controller.user.SubscriptionDTO;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.time.Year;
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
            List<School> schools = schoolService.getAllSchool();
            for (School school : schools) {
                for (Categories cat : school.getCategoriesList()) {
                    if (cat.getIdCategory().equals(categories.getIdCategory())) {
                        schoolList.add(school);
                    }
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

    @PostMapping(value = "user/addSubscription/{category}")
    public String addSubscription(@PathVariable("category") Long id, Subscription subscription, School school, Model model) {
        try {
            Date date = new Date();
            Categories c = categoriesService.findById(id);
            subscription.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            subscription.setCategories(c);
            subscription.setSchool(schoolService.findById(school.getIdSchool()));
            subscription.setValidation(false);
            subscription.setExpenses(expenses);


            subscriptionService.save(subscription);

            model.addAttribute("subscription", subscription);
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/user/addSubscriptionPart2/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "user/addSubscriptionPart2/{subscription}")
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

    @PostMapping(value = "user/addSubscriptionPart2/{subscription}")
    public String addSubscriptionPart2(@PathVariable("subscription") Long id, SubscriptionDTO subscriptionDTO, SubscriptionType subscriptionT, SubscriptionPeriod subscriptionP, Model model) {
        try {
            Date dateCurrent = new Date();
            Subscription subscription = subscriptionService.findById(id);
            if (subscription.getEndDate() != null) {
                if (subscription.getEndDate().before(dateCurrent)) {
                    subscription.setSubscriptionStatus(SubscriptionStatus.INITIATED);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            String dateString = subscriptionDTO.getStartDate();
            Date date = sdf.parse(dateString);
            subscription.setStartDate(date);
            String dateStringEnd = subscriptionDTO.getEndDate();
            Date d = sdf.parse(dateStringEnd);
            subscription.setEndDate(d);
            subscription.setSubscriptionType(subscriptionTypeService.findById(subscriptionT.getIdType()));
            subscription.setSubscriptionPeriod(subscriptionPeriodService.findById(subscriptionP.getId()));
            subscriptionService.save(subscription);
            Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getTariffPK().getIdCategory().equals(subscription.getCategories().getIdCategory()) && t.getTariffPK().getIdPeriod().equals(subscription.getSubscriptionPeriod().getId()) && t.getTariffPK().getIdType().equals(subscription.getSubscriptionType().getIdType()))
                    .findFirst();
            if (firstTariff.isPresent()) {
                Tariff tariff = firstTariff.get();
                subscription.setPrice(tariff.getPrix());
                subscription.setTotalPrice(tariff.getPrix() + subscription.getExpenses());
                subscriptionService.save(subscription);
            }

            model.addAttribute("subscription", subscription);
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/user/addStudent/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "user/summary/{subscription}")
    public String getSummary(@PathVariable("subscription") Long id, Model model) {

        Map<String, ContactPerson> listMap = new HashMap<>();
        List<StudentRelation> relationList = new ArrayList<>();
        Subscription subscription = subscriptionService.findById(id);

        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(subscription.getStudent().getIdStudent())) {
                relationList.add(studentRelation);
                listMap.put(studentRelation.getRelationship(), contactPersonService.findById(studentRelation.getStudentRelationPK().getIdContactPerson()));
            }
        }

        if (relationList.isEmpty()) {
            if (subscription.getCategories().getAge() < 18) {
                model.addAttribute("messageError", "Vous achetez un abonnement pour un mineur, veuillez ajouter une personne de contact SVP.");
                model.addAttribute("student", subscription.getStudent());
                model.addAttribute("subscription", subscription);
                model.addAttribute("contactPersonList", listMap);
                return "user/contactPersonList";
            }
        }

        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/summary";
    }

    @PostMapping(value = "user/confirmSubscription/{subscription}")
    public String confirmSubsctiption(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);
        // put the sbscription in the chart
        subscription.setSubscriptionStatus(SubscriptionStatus.CHART);
        subscriptionService.save(subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        return "redirect:/index";
    }

    @GetMapping(value = "user/delete/{subscription}")
    public String deleteSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        Student student = subscription.getStudent();
        List<StudentRelation> studentRelations = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelations) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent())) {
                studentRelationService.delete(studentRelation);
            }
        }
        studentService.delete(student);
        subscriptionService.delete(subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "redirect:/index";
    }

    @GetMapping(value = "user/edit/{subscription}")
    public String getEditSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/subscription";
    }

    @GetMapping(value = "user/editSubscription/{subscription}/{category}")
    public String getEditSubscription(@PathVariable("subscription") Long idSub, @PathVariable("category") Long id, Model model) {


        List<School> schoolList = new ArrayList<>();
        Subscription subscription = subscriptionService.findById(idSub);
        Categories categories = categoriesService.findById(id);
        List<School> schools = schoolService.getAllSchool();
        for (School school : schools) {
            for (Categories cat : school.getCategoriesList()) {
                if (cat.getIdCategory().equals(categories.getIdCategory())) {
                    schoolList.add(school);
                }
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

    @PostMapping(value = "user/editSubscription/{subscription}/{category}")
    public String editSubscription(@PathVariable("subscription") Long idSub, @PathVariable("category") Long id, School school, Model model) {
        try {
            Categories c = categoriesService.findById(id);
            Subscription subscription = subscriptionService.findById(idSub);
            subscription.setCategories(c);
            subscription.setSchool(schoolService.findById(school.getIdSchool()));
            subscription.setValidation(false);
            subscription.setExpenses(expenses);

            subscriptionService.save(subscription);

            model.addAttribute("subscription", subscription);
            model.addAttribute("subscriptions", subscriptionService.getCart());
            String url = "/user/addSubscriptionPart2/" + subscription.getIdSubscription();

            return "redirect:" + url;
        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/subscription";
        }
    }

    @GetMapping(value = "/user/renewSubscription/{subscription}")
    public String getRenewSubscription(@PathVariable("subscription") Long id, Model model) {

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Date date = new Date();
        List<School> schoolList = new ArrayList<>();

        List<Categories> categoriesList = categoriesService.getAllCategory();

        if (authentication instanceof User) {
            User user = (User) authentication;
            model.addAttribute("user", user);
            Subscription subscription = subscriptionService.findById(id);

            int cptYear = Math.abs(Years.yearsBetween(LocalDate.fromDateFields(subscription.getStudent().getBirthDay()), LocalDate.fromDateFields(date)).getYears());

            if (cptYear > subscription.getCategories().getAge()) {
                for (int i = 0; i <= categoriesList.size(); i++) {
                    if ((i + 1) < categoriesList.size()) {
                        if (cptYear > categoriesList.get(i).getAge() && cptYear < categoriesList.get(i + 1).getAge()) {
                            subscription.setCategories(categoriesList.get(i));
                        }
                    }
                    if (i == (categoriesList.size() - 1)) {
                        subscription.setCategories(categoriesList.get(categoriesList.size() - 1));
                    }
                }

            }
            subscription.setValidation(false);
            subscription.setSubscriptionStatus(SubscriptionStatus.INITIATED);

            subscriptionService.save(subscription);

            List<School> schools = schoolService.getAllSchool();
            for (School school : schools) {
                for (Categories cat : school.getCategoriesList()) {
                    if (cat.getIdCategory().equals(subscription.getCategories().getIdCategory())) {
                        schoolList.add(school);
                    }
                }
            }

            model.addAttribute("cat", subscription.getCategories());
            model.addAttribute("subscription", subscription);
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("schoolList", schoolList);
            model.addAttribute("subscriptions", subscriptionService.getCart());

            return "user/addSubscription1";
        } else {
            model.addAttribute("messageWarning", "Vous devez se connecter/s'inscrire pour pouvoir acheter/prolonger vos abonnements.");
            return "login";
        }

    }
}
