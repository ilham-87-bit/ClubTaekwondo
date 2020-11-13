package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.AddressService;
import com.clubtaekwondo.club.service.CityService;
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
@RequestMapping("admin/school")
public class SchoolController {

    private static final String SCHOOL = "school";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/schoolList")
    public String schoolList(Model model) {
        model.addAttribute("schoolList", schoolService.getAllSchool());
        return "adminPart/school/schoolList";
    }

    @GetMapping(value = "/addSchool")
    public String getAddSchool(Model model) {
        model.addAttribute(SCHOOL, new School());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        return ("adminPart/school/addSchool");
    }

    @PostMapping(value = "/addSchool")
    public String addSchool(School school, City city, Model model) {

        City c = cityService.findById(city.getIdCity());
        school.getAddress().setCity(c);
        Optional<Address> firstAddress = addressService.getAllAddress().stream()
                .filter(a -> a.getStreet().equals(school.getAddress().getStreet()) && a.getNumber().equals(school.getAddress().getNumber()) && a.getCity().equals(school.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            school.setAddress(firstAddress.get());
        } else {
            addressService.save(school.getAddress());
        }
        schoolService.save(school);


        model.addAttribute(SCHOOL, school);
        model.addAttribute(ADDRESS, school.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        return "redirect:/admin/school/schoolList";
    }

    @GetMapping(value = "/delete/{school}")
    public String deleteSchool(@PathVariable("school") Long id, Model model) {

        School s = schoolService.findById(id);
        schoolService.delete(s);

        model.addAttribute("schoolList", schoolService.getAllSchool());

        return "redirect:/admin/school/schoolList";
    }

    @GetMapping(value = "/edit/{school}")
    public String schoolDetails(@PathVariable("school") Long id, Model model) {

        School school = schoolService.findById(id);

        model.addAttribute(SCHOOL, school);
        model.addAttribute(ADDRESS, school.getAddress());
//        model.addAttribute(CITY, school.getAddress().getCity());
        model.addAttribute("cityList", cityService.getAllCity());

        return "adminPart/school/addSchool";
    }

    @PostMapping(value = "/edit")
    public String editSchool(School school, City city, Model model) {

        City c = cityService.findById(city.getIdCity());
        school.getAddress().setCity(c);
        addressService.save(school.getAddress());
        schoolService.save(school);

        model.addAttribute(SCHOOL, school);
        model.addAttribute(ADDRESS, school.getAddress());
//        model.addAttribute(CITY, school.getAddress().getCity());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());

        return "redirect:/admin/school/schoolList";
    }

}
