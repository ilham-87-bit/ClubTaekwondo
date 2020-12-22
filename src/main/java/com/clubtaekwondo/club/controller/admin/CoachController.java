package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private CategoryByCoachService categoryByCoachService;
    @Autowired
    private CategoryBySchoolService categoryBySchoolService;


    @GetMapping(value = "/coachList")
    public String coachList(Model model) {

        model.addAttribute("coachList", coachService.getAllCoach());

        return ("adminPart/coach/coachList");
    }

    @GetMapping(value = "/addCoach")
    public String getAddCoach(Model model) {

        List<CategoryBySchool> categoryBySchoolList = categoryBySchoolService.getAllCategoryBySchool();

        model.addAttribute(COACH, new Coach());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("parameters", categoryBySchoolList);
        model.addAttribute("categoryList", categoriesService.getAllCategory());


        return ("adminPart/coach/addCoach");
    }

    @PostMapping(value = "/addCoach")
    public String addCoach(Coach coach, School school, City city, @RequestParam("categoriesList[]") List<Categories> categoriesList, Model model) {
        List<Categories> listAdd = new ArrayList<>();
        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getId());
        coach.setSchool(s);

        Optional<Address> firstAddress = addressService.getAllAddress().stream().filter(a -> a.getStreet().equals(coach.getAddress().getStreet()) && a.getNumber().equals(coach.getAddress().getNumber()) && a.getCity().equals(coach.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            coach.setAddress(firstAddress.get());
        } else {
            addressService.save(coach.getAddress());
        }
        coachService.saveCoach(coach);

        for (Categories categories : categoriesList) {
            CategoryByCoach categoryByCoach = new CategoryByCoach();
            categoryByCoach.setCoach(coach);
            categoryByCoach.setCategories(categories);
            categoryByCoachService.save(categoryByCoach);
            listAdd.add(categoryByCoach.getCategories());
        }

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", listAdd);

        return "redirect:/admin/coach/coachList";

    }

    @GetMapping(value = "/delete/{coach}")
    public String deleteCoach(@PathVariable("coach") Long id, Model model) {

        Coach coach = coachService.findById(id);

        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getIdCoach().equals(coach.getIdCoach())) {
                categoryByCoachService.delete(categoryByCoach);
            }
        }

        coachService.deleteCoach(coach);

        model.addAttribute("coachList", coachService.getAllCoach());

        return "redirect:/admin/coach/coachList";
    }

    @GetMapping(value = "/edit/{coach}")
    public String coachDetails(@PathVariable("coach") Long id, Model model) {

        List<Categories> list = new ArrayList<>();
        Coach coach = coachService.findById(id);

        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getIdCoach().equals(coach.getIdCoach())) {
                list.add(categoryByCoach.getCategories());
            }
        }
        List<CategoryBySchool> categoryBySchoolList = categoryBySchoolService.getAllCategoryBySchool();

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("listCat", list);
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("parameters", categoryBySchoolList);

        return "adminPart/coach/addCoach";
    }

    @PostMapping(value = "/edit")
    public String editCoach(Coach coach, School school, City city, @RequestParam("categoriesList[]") List<Categories> categoriesList, Model model) {

        List<Categories> listCat = new ArrayList<>();

        City c = cityService.findById(city.getIdCity());
        coach.getAddress().setCity(c);
        School s = schoolService.findById(school.getId());
        coach.setSchool(s);
        addressService.save(coach.getAddress());
        List<CategoryByCoach> categoryByCoachList = categoryByCoachService.getAllCategoryByCoach();
        for (CategoryByCoach categoryByCoach : categoryByCoachList) {
            if (categoryByCoach.getCoach().getIdCoach() == coach.getIdCoach()) {
                categoryByCoachService.delete(categoryByCoach);
            }
        }
        for (Categories categories : categoriesList) {
            CategoryByCoach categoryByCoach = new CategoryByCoach();
            categoryByCoach.setCoach(coach);
            categoryByCoach.setCategories(categories);
            categoryByCoachService.save(categoryByCoach);
            listCat.add(categoryByCoach.getCategories());
        }
        coachService.saveCoach(coach);

        model.addAttribute(COACH, coach);
        model.addAttribute(ADDRESS, coach.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", listCat);

        return "redirect:/admin/coach/coachList";
    }
}
