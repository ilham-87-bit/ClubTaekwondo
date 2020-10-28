package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("admin/category")
public class CategoriesController {

    private static final String CATEGORY = "category";
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping(value = "/categoryList")
    public String categoryList(Model model, WebRequest request) {
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        return "adminPart/category/categoryList";
    }

    @GetMapping(value = "/{category}")
    public String category(@PathVariable("category") Long id, Model model) {

        Categories category = categoriesService.findById(id);

        model.addAttribute(CATEGORY, category);

        return "adminPart/category/addCategory";
    }

    @GetMapping(value = "/addCategory")
    public String getAddCategory(Categories categories, Model model) {
        model.addAttribute(CATEGORY, new Categories());
        return ("adminPart/category/addCategory");
    }

    @PostMapping(value = "/addCategory")
    public String addCategory(Categories categories, Model model) {

        categoriesService.saveCategories(categories);

        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("mode", "add");
        return "adminPart/category/categoryList";
    }

    @GetMapping(value = "/edit/{category}")
    public String categoryDetails(@PathVariable("category") Long id, Model model) {

        Categories category = categoriesService.findById(id);

        model.addAttribute(CATEGORY, category);

        return "adminPart/category/addCategory";
    }

    @PostMapping(value = "/edit")
    public String editCategory(Categories categories, Model model) {

        categoriesService.saveCategories(categories);

        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("mode", "edit");

        return "adminPart/category/categoryList";
    }

    @GetMapping(value = "/delete/{category}")
    public String deletePeriod(@PathVariable("category") Long id, Model model) {

        Categories categories = categoriesService.findById(id);
        categoriesService.deleteCategories(categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "adminPart/category/categoryList";
    }
}
