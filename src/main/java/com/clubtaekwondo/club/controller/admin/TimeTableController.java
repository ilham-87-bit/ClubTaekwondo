package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return ("adminPart/time/addTime");
    }

    @PostMapping(value = "/addTime")
    public String addTime(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, Model model) {

        return getString(timeTable, coach, day, school, categories, model);
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

        return ("adminPart/time/addTime");

    }

    @PostMapping(value = "/edit")
    public String editTime(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, Model model) {


        return getString(timeTable, coach, day, school, categories, model);
    }

    private String getString(TimeTable timeTable, Coach coach, Day day, School school, Categories categories, Model model) {

        timeTable.setC(categoriesService.findById(categories.getIdCategory()));
        timeTable.setCo(coachService.findById(coach.getIdCoach()));
        timeTable.setDay(dayService.getDayById(day.getIdDay()));
        timeTable.setS(schoolService.findById(school.getId()));

        timeTableService.save(timeTable);

        model.addAttribute(TIME, timeTable);
        model.addAttribute("timeList", timeTableService.getAllTimeTable());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("coachList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());

        return "redirect:/admin/time/timeList";
    }

}
