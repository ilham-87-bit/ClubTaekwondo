package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.clubtaekwondo.club.controller.user.UserController.fileToPath;

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

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/schoolList")
    public String schoolList(Model model) {

        List<School> allSchool = schoolService.getAllSchool();
        allSchool.stream().forEach(school -> {
            String newImageName = String.format("schools/%s.jpeg", school.getIdSchool());
            school.setFullUrlImg(fileToPath(storageService.load(newImageName)));
        });
        model.addAttribute("schoolList", allSchool);
        return "adminPart/school/schoolList";
    }

    @GetMapping(value = "/addSchool")
    public String getAddSchool(Model model) {
        model.addAttribute(SCHOOL, new School());
        model.addAttribute(ADDRESS, new Address());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        return ("adminPart/school/addSchool");
    }

    @PostMapping(value = "/addSchool")
    public String addSchool(School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model, @RequestParam("file") MultipartFile file) {

        List<Categories> listAdd = new ArrayList<>();

        Optional<School> firstSchool = schoolService.getAllSchool().stream().filter(s -> s.getName().equals(school.getName())).findFirst();
        if (firstSchool.isPresent()) {
            model.addAttribute(SCHOOL, new School());
            model.addAttribute(ADDRESS, new Address());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce école existe déjà.");
            return ("adminPart/school/addSchool");
        } else {
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
            school.setCategoriesList(categoryList);
            School s = schoolService.save(school);
            if (file != null && !file.isEmpty()) {
                String newImageName = String.format("schools/%s.jpeg", s.getIdSchool());
                storageService.store(file, newImageName);
            }

            model.addAttribute(SCHOOL, school);
            model.addAttribute(ADDRESS, school.getAddress());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("schoolList", schoolService.getAllSchool());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("listCat", s.getCategoriesList());
        }

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

        List<Categories> list = new ArrayList<>();

        School school = schoolService.findById(id);

        model.addAttribute(SCHOOL, school);
        model.addAttribute(ADDRESS, school.getAddress());
        model.addAttribute("listCat", school.getCategoriesList());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "adminPart/school/addSchool";
    }

    @PostMapping(value = "/edit")
    public String editSchool(School school, City city, @RequestParam("categoryList[]") List<Categories> categoryList, Model model) {

        List<Categories> listCat = new ArrayList<>();
        Optional<School> firstSchool = schoolService.getAllSchool().stream().filter(s -> s.getName().equals(school.getName()) && !s.getIdSchool().equals(school.getIdSchool())).findFirst();
        if (firstSchool.isPresent()) {
            model.addAttribute(SCHOOL, school);
            model.addAttribute(ADDRESS, school.getAddress());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("listCat", school.getCategoriesList());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce école existe déjà.");
            return ("adminPart/school/addSchool");
        } else {

            City c = cityService.findById(city.getIdCity());
            school.getAddress().setCity(c);
            addressService.save(school.getAddress());
            school.setCategoriesList(categoryList);
            schoolService.save(school);

            model.addAttribute(SCHOOL, school);
        }
        model.addAttribute(ADDRESS, school.getAddress());
        model.addAttribute("cityList", cityService.getAllCity());
        model.addAttribute("schoolList", schoolService.getAllSchool());
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("listCat", school.getCategoriesList());

        return "redirect:/admin/school/schoolList";
    }

}
