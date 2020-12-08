package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.School;
import com.clubtaekwondo.club.model.TimeTable;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private TimeTableService timeTableService;

    @GetMapping(value = "/school")
    public String getAllSchool(Model model) {

//        School s = schoolService.findById(id);
//
//        List<TimeTable> timeTableList = timeTableService.getAllTimeTable();
//        for(TimeTable timeTable : timeTableList){
//            if(timeTable.getS().getId().equals(id)){
//                s.getTimeTables().add(timeTable);
//            }
//        }

        model.addAttribute("schoolList", schoolService.getAllSchool());
        return "user/school";
    }
}
