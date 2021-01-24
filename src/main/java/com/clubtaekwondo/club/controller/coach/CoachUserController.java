package com.clubtaekwondo.club.controller.coach;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.CategoriesService;
import com.clubtaekwondo.club.service.SubscriptionService;
import com.clubtaekwondo.club.service.TimeTableService;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("user")
@Transactional
public class CoachUserController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping(value = "/studentList")
    public String getStudentsList(Model model) {

        List<Student> studentList = new ArrayList<>();
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        List<Subscription> subscriptionStudentList = new ArrayList<>();
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof Coach) {

            Coach coach = (Coach) authentication;
            for (Subscription subscription : subscriptionList) {
                if (!coach.getCategoriesList().isEmpty()) {
                    for (Categories categories : coach.getCategoriesList()) {
                        if (subscription.getCategories().getIdCategory().equals(categories.getIdCategory()) && subscription.getSchool().getIdSchool().equals(coach.getSchool().getIdSchool()) && subscription.getValidation() && subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
                            studentList.add(subscription.getStudent());
                            subscriptionStudentList.add(subscription);
                        }
                    }
                }
            }
            model.addAttribute("categoryList", coach.getCategoriesList());
        }
        model.addAttribute("studentList", studentList);
        model.addAttribute("subscriptionStudentList", subscriptionStudentList);
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "coach/studentList";
    }

    @GetMapping(value = "/myTime")
    public String getTimeCoach(Model model) {

        List<TimeTable> timeTablesCoach = new ArrayList<>();
        List<TimeTable> timeTableList = timeTableService.getAllTimeTable();
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof Coach) {

            Coach coach = (Coach) authentication;

            for (TimeTable timeTable : timeTableList) {
                if (timeTable.getS().getIdSchool().equals(coach.getSchool().getIdSchool())) {
                    for (Coach co : timeTable.getCoachList()) {
                        if (co.getId().equals(coach.getId())) {
                            timeTablesCoach.add(timeTable);
                        }
                    }
                }
            }
        }
        model.addAttribute("timeList", timeTablesCoach);

        return "coach/myTime";
    }
}
