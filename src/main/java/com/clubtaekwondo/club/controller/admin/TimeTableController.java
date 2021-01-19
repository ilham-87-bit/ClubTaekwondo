package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/time")
public class TimeTableController {


    private static final String TIME = "time";

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private CoachService coachService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private DayService dayService;
    @Autowired
    private SubscriptionTypeService subscriptionTypeService;

    @GetMapping(value = "/timeList")
    public String timeList(Model model) {

        model.addAttribute("timeList", timeTableService.getAllTimeTable());

        return ("adminPart/time/timeList");
    }

    @GetMapping(value = "/addTime")
    public String getAddTime(Model model) {

        model.addAttribute(TIME, new TimeTable());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("coachList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return ("adminPart/time/addTime");
    }

    @PostMapping(value = "/addTime")
    public String addTime(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, @RequestParam("typeList[]") List<SubscriptionType> subscriptionTypeList, Model model) {

        return getString(timeTable, coach, day, school, categories, subscriptionTypeList, model);
    }

    @GetMapping(value = "/delete/{time}")
    public String deleteTime(@PathVariable("time") Long id, Model model) {

        TimeTable t = timeTableService.getTimeTableById(id);
        timeTableService.delete(t);

        model.addAttribute("timeList", timeTableService.getAllTimeTable());

        return "redirect:/admin/time/timeList";
    }


    @GetMapping(value = "/edit/{time}")
    public String timeDetails(@PathVariable("time") Long id, Model model) {

        TimeTable timeTable = timeTableService.getTimeTableById(id);

        model.addAttribute(TIME, timeTable);
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("coachList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("listType", timeTable.getSubscriptionTypeList());

        return ("adminPart/time/addTime");

    }

    @PostMapping(value = "/edit")
    public String editTime(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, @RequestParam("typeList[]") List<SubscriptionType> subscriptionTypeList, Model model) {


        return getString(timeTable, coach, day, school, categories, subscriptionTypeList, model);
    }

    private String getString(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, List<SubscriptionType> subscriptionTypeList, Model model) {

        timeTable.setC(categoriesService.findById(categories.getIdCategory()));
        timeTable.setCo(coachService.findById(coach.getId()));
        timeTable.setDay(dayService.getDayById(day.getIdDay()));
        timeTable.setS(schoolService.findById(school.getIdSchool()));
        timeTable.setSubscriptionTypeList(subscriptionTypeList);

        timeTableService.save(timeTable);

        School s = schoolService.findById(school.getIdSchool());
        s.getTimeTables().add(timeTable);

        Coach c = coachService.findById(coach.getId());
        c.getTimeTables().add(timeTable);

        Categories cat = categoriesService.findById(categories.getIdCategory());
        cat.getTimeTables().add(timeTable);

        Day d = dayService.getDayById(day.getIdDay());
        d.getTimes().add(timeTable);

//        SubscriptionType subsType = subscriptionTypeService.findById(subscriptionType.getIdType());
//        subsType.getTimeTables().add(timeTable);

        model.addAttribute(TIME, timeTable);
        model.addAttribute("timeList", timeTableService.getAllTimeTable());
        model.addAttribute("listType", timeTable.getSubscriptionTypeList());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("coachList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());


        return "redirect:/admin/time/timeList";
    }
}
