package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/time")
public class TimeTableController {


    private static final String TIME = "time";

    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private SubscriptionService subscriptionService;
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
        Map<Long, List<Long>> parameter = new HashMap<>();
        Map<Long, List<Long>> parameterCoach = new HashMap<>();

        List<Coach> coaches = coachService.getAllCoach().stream().filter(coach1 -> Role.COACH.getAlea().equals(coach1.getUserRole().getRole())).collect(Collectors.toList());
        List<School> schoolList = schoolService.getAllSchool().stream().filter(school -> school.isBelongTo()).collect(Collectors.toList());
        for (School school : schoolList) {
            List<Long> idCoList = new ArrayList<>();
            for (Coach coach : coaches) {
                if (coach.getSchool().getIdSchool().equals(school.getIdSchool())) {
                    idCoList.add(coach.getId());
                }
                List<Long> idAllCatByCoach = new ArrayList<>();
                for (Categories categories : coach.getCategoriesList()) {
                    idAllCatByCoach.add(categories.getIdCategory());
                }
                parameterCoach.put(coach.getId(), idAllCatByCoach);
            }
            parameter.put(school.getIdSchool(), idCoList);
        }

        model.addAttribute(TIME, new TimeTable());
        model.addAttribute("schoolList", schoolList);
        model.addAttribute("coList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("parameter", parameter);
        model.addAttribute("parameterCoach", parameterCoach);

        return ("adminPart/time/addTime");
    }

    @PostMapping(value = "/addTime")
    public String addTime(TimeTable timeTable, @RequestParam("coaList[]") List<Coach> coaList, Day day, School school, @RequestParam("typeList[]") List<SubscriptionType> subscriptionTypeList, Model model) {


        return getString(timeTable, coaList, day, school, subscriptionTypeList, model);
    }

    @GetMapping(value = "/delete/{time}")
    public String deleteTime(@PathVariable("time") Long id, Model model) {

        TimeTable t = timeTableService.getTimeTableById(id);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(s -> s.getCategories().getIdCategory().equals(t.getC().getIdCategory()) && t.getSubscriptionTypeList().contains(s.getSubscriptionType()) && s.getSchool().getIdSchool().equals(t.getS().getIdSchool()) && s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)).findFirst();
        if (firstSubscription.isPresent()) {
            model.addAttribute("messageError", "Vous ne pouvez pas supprimer cet horaire ! Cet horaire est lié à des abonnements en cours.");

        } else {
            timeTableService.delete(t);
        }

        model.addAttribute("timeList", timeTableService.getAllTimeTable());

        return "adminPart/time/timeList";
    }


    @GetMapping(value = "/edit/{time}")
    public String timeDetails(@PathVariable("time") Long id, Model model) {

        TimeTable timeTable = timeTableService.getTimeTableById(id);
        Map<Long, List<Long>> parameter = new HashMap<>();
        Map<Long, List<Long>> parameterCoach = new HashMap<>();

        List<Coach> coaches = coachService.getAllCoach().stream().filter(coach1 -> Role.COACH.getAlea().equals(coach1.getUserRole().getRole())).collect(Collectors.toList());
        List<School> schoolList = schoolService.getAllSchool().stream().filter(school -> school.isBelongTo()).collect(Collectors.toList());
        for (School school : schoolList) {
            List<Long> idCoList = new ArrayList<>();
            for (Coach coach : coaches) {
                if (coach.getSchool().getIdSchool().equals(school.getIdSchool())) {
                    idCoList.add(coach.getId());
                }
                List<Long> idAllCatByCoach = new ArrayList<>();
                for (Categories categories : coach.getCategoriesList()) {
                    idAllCatByCoach.add(categories.getIdCategory());
                }
                parameterCoach.put(coach.getId(), idAllCatByCoach);
            }
            parameter.put(school.getIdSchool(), idCoList);
        }

        model.addAttribute(TIME, timeTable);
        model.addAttribute("schoolList", schoolList);
        model.addAttribute("coList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("listType", timeTable.getSubscriptionTypeList());
        model.addAttribute("listCoach", timeTable.getCoachList());
        model.addAttribute("parameter", parameter);
        model.addAttribute("parameterCoach", parameterCoach);

        return ("adminPart/time/addTime");

    }

    @PostMapping(value = "/edit")
    public String editTime(TimeTable timeTable, @RequestParam("coaList[]") List<Coach> coaList, Day day, School school, @RequestParam("typeList[]") List<SubscriptionType> subscriptionTypeList, Model model) {


        return getString(timeTable, coaList, day, school, subscriptionTypeList, model);
    }

    private String getString(TimeTable timeTable, List<Coach> coachList, Day day, School school, List<SubscriptionType> subscriptionTypeList, Model model) {
        List<School> allSchool = schoolService.getAllSchool().stream().filter(sc -> sc.isBelongTo()).collect(Collectors.toList());
        timeTable.setDay(dayService.getDayById(day.getIdDay()));
        timeTable.setS(schoolService.findById(school.getIdSchool()));
        timeTable.setSubscriptionTypeList(subscriptionTypeList);
        timeTable.setCoachList(coachList);

        timeTableService.save(timeTable);

        School s = schoolService.findById(school.getIdSchool());
        s.getTimeTables().add(timeTable);

        Categories cat = categoriesService.findById(timeTable.getC().getIdCategory());
        cat.getTimeTables().add(timeTable);

        Day d = dayService.getDayById(day.getIdDay());
        d.getTimes().add(timeTable);

        model.addAttribute(TIME, timeTable);
        model.addAttribute("timeList", timeTableService.getAllTimeTable());
        model.addAttribute("listType", timeTable.getSubscriptionTypeList());
        model.addAttribute("schoolList", allSchool);
        model.addAttribute("coList", coachService.getAllCoach());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("dayList", dayService.getAllDay());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("listCoach", timeTable.getCoachList());


        return "redirect:/admin/time/timeList";
    }
}
