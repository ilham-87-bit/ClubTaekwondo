package com.clubtaekwondo.club.controller;


import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                if (categoryBySchool.getCat().getIdCategory() == categories.getIdCategory()) {
                    schoolList.add(categoryBySchool.getSchool());
                }
            }

            model.addAttribute("cat", categories);
            model.addAttribute("subscription", new Subscription());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
            model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
            model.addAttribute("schoolList", schoolList);

            return "user/addSubscription";
        } else {
            model.addAttribute("messageWarning", "Vous devez se connecter/s'inscrire pour pouvoir acheter/prolonger vos abonnements.");
            return "login";
        }

    }

    @PostMapping(value = "/addSubscription/{category}")
    public String addSubscription(@PathVariable("category") Long id, Subscription subscription, SubscriptionType subscriptionType, SubscriptionPeriod subscriptionPeriod, School school, Model model) {
        Categories c = categoriesService.findById(id);

        subscription.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        subscription.setCategories(c);
        subscription.setSubscriptionType(subscriptionTypeService.findById(subscriptionType.getIdType()));
        subscription.setSubscriptionPeriod(subscriptionPeriodService.findById(subscriptionPeriod.getId()));
        subscription.setSchool(schoolService.findById(school.getId()));
        subscription.setValidation(false);
        subscription.setExpenses(expenses);

        return "user/addStudent";
    }
}
