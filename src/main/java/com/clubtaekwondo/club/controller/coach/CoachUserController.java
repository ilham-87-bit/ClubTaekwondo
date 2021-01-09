package com.clubtaekwondo.club.controller.coach;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.SubscriptionService;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("user")
public class CoachUserController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping(value = "/studentList")
    public String getStudentsList(Model model) {

        List<Student> studentList = new ArrayList<>();
        List<Categories> categoriesList = new ArrayList<>();
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        List<Subscription> subscriptionStudentList = new ArrayList<>();
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof Coach) {

            Coach coach = (Coach) authentication;
            categoriesList = coach.getCategoriesList();
            for (Subscription subscription : subscriptionList) {
                if (categoriesList != null) {
                    for (Categories categories : categoriesList) {
                        if (subscription.getCategories().getIdCategory().equals(categories.getIdCategory()) && subscription.getSchool().getIdSchool().equals(coach.getSchool().getIdSchool()) && subscription.getValidation() && subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
                            studentList.add(subscription.getStudent());
                            subscriptionStudentList.add(subscription);
                        }
                    }
                }
            }
        }
        model.addAttribute("categoryList", categoriesList);
        model.addAttribute("studentList", studentList);
        model.addAttribute("subscriptionStudentList", subscriptionStudentList);

        return "coach/studentList";
    }
}
