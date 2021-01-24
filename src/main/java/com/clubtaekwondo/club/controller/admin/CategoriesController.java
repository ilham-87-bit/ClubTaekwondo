package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.CategoriesService;
import com.clubtaekwondo.club.service.SubscriptionService;
import com.clubtaekwondo.club.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/category")
public class CategoriesController {

    private static final String CATEGORY = "category";
    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TariffService tariffService;

    @GetMapping(value = "/categoryList")
    public String categoryList(Model model, WebRequest request) {
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        return "adminPart/category/categoryList";
    }

    @GetMapping(value = "/addCategory")
    public String getAddCategory(Categories categories, Model model) {
        model.addAttribute(CATEGORY, new Categories());
        return ("adminPart/category/addCategory");
    }

    @PostMapping(value = "/addCategory")
    public String addCategory(Categories categories, Model model) {

        Optional<Categories> firstCategories = categoriesService.getAllCategory().stream().filter(c -> c.getCategoryName().equals(categories.getCategoryName())).findFirst();
        if (firstCategories.isPresent()) {
            model.addAttribute(CATEGORY, new Categories());
            model.addAttribute("messageError", "Cette catégorie existe déjà.");
            return "adminPart/category/addCategory";
        } else {
            categoriesService.saveCategories(categories);
            model.addAttribute("messageSuccess", " La catégorie a bien été ajoutée.");
        }
        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
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

        Optional<Categories> firstCategories = categoriesService.getAllCategory().stream().filter(c -> c.getCategoryName().equals(categories.getCategoryName()) && !c.getIdCategory().equals(categories.getIdCategory())).findFirst();
        if (firstCategories.isPresent()) {
            model.addAttribute(CATEGORY, categories);
            model.addAttribute("messageError", "Cette catégorie existe déjà.");
            return "adminPart/category/addCategory";
        } else {
            categoriesService.saveCategories(categories);
        }

        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("mode", "edit");

        return "adminPart/category/categoryList";
    }

    @GetMapping(value = "/delete/{category}")
    public String deletePeriod(@PathVariable("category") Long id, Model model) {

        Categories categories = categoriesService.findById(id);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(s -> s.getCategories().getIdCategory().equals(categories.getIdCategory()) && s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)).findFirst();
        if (firstSubscription.isPresent()) {
            model.addAttribute("messageError", "Vous ne pouvez pas supprimer cette catégorie ! Cette catégorie est liée à des abonnements en cours.");

        } else {
            List<Tariff> tariffList = tariffService.getAllTariff();
            for (Tariff tariff : tariffList) {
                if (tariff.getTariffPK().getIdCategory().equals(categories.getIdCategory())) {
                    tariffService.delete(tariff);
                }
            }
            categoriesService.deleteCategories(categories);
        }

        model.addAttribute("categoryList", categoriesService.getAllCategory());
        return "adminPart/category/categoryList";
    }
}
