package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.Address;
import com.clubtaekwondo.club.model.City;
import com.clubtaekwondo.club.model.Coach;
import com.clubtaekwondo.club.model.School;
import com.clubtaekwondo.club.service.AddressService;
import com.clubtaekwondo.club.service.CityService;
import com.clubtaekwondo.club.service.CoachService;
import com.clubtaekwondo.club.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("admin/coach")
public class CoachController {

    private static final String COACH = "coach";
    private static final String SCHOOL = "school";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";

    @Autowired
    private CoachService coachService;
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/coachList")
    public String coachList(Model model) {

        model.addAttribute("coachList", coachService.getAllCoach());
        return ("adminPart/coach/coachList");
    }

    @GetMapping(value = "/addCoach")
    public String getAddCoach(Model model) {
        model.addAttribute(COACH, new Coach());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        return ("adminPart/coach/addCoach");
    }

    @PostMapping(value = "/addCoach")
    public String addCoach(Coach coach, School school, City city, Model model) {

        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getId());
        coach.setSchool(s);

        Optional<Address> firstAddress = addressService.getAllAddress().stream()
                .filter(a -> a.getStreet().equals(coach.getAddress().getStreet()) && a.getNumber().equals(coach.getAddress().getNumber()) && a.getCity().equals(coach.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            coach.setAddress(firstAddress.get());
        } else {
            addressService.save(coach.getAddress());
        }
        coachService.saveCoach(coach);


        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        return "redirect:/admin/coach/coachList";
    }

    @GetMapping(value = "/delete/{coach}")
    public String deleteCoach(@PathVariable("coach") Long id, Model model) {

        Coach coach = coachService.findById(id);
        coachService.deleteCoach(coach);

        model.addAttribute("coachList", coachService.getAllCoach());

        return "redirect:/admin/coach/coachList";
    }

    @GetMapping(value = "/edit/{coach}")
    public String coachDetails(@PathVariable("coach") Long id, Model model) {

        Coach coach = coachService.findById(id);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("cityList", cityService.getAllCity());

        return "adminPart/coach/addCoach";
    }

    @PostMapping(value = "/edit")
    public String editCoach(Coach coach, School school, City city, Model model) {

        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getId());
        coach.setSchool(s);
        addressService.save(coach.getAddress());
        coachService.saveCoach(coach);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());

        return "redirect:/admin/coach/coachList";
    }

}
