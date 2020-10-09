package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.service.CategoriesService;
import com.clubtaekwondo.club.utils.RequestIndirectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Controller
@RequestMapping("admin/category")
public class CategoriesController {

    private static final String CATEGORY = "category";
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping(value = "/categoryList")
    public String categoryList(Model model, WebRequest request) {
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        return "categoryList";
    }

    @GetMapping(value = "/{category}")
    public String category(@PathVariable("category") Long id, Model model) {

        Categories category = categoriesService.findById(id);

        model.addAttribute(CATEGORY, category);

        return "addCategory";
    }

    @GetMapping(value = "/addCategory")
    public String getAddCategory(Categories categories, Model model) {
        model.addAttribute(CATEGORY, new Categories());
        return ("addCategory");
    }

    @PostMapping(value = "/addCategory")
    public String addCategory(Categories categories, Model model) {

        categoriesService.saveCategories(categories);

        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("mode", "add");
        return "categoryList";
    }

    @GetMapping(value = "/edit/{category}")
    public String categoryDetails(@PathVariable("category") Long id, Model model) {

        Categories category = categoriesService.findById(id);

        model.addAttribute(CATEGORY, category);

        return "addCategory";
    }

    @PostMapping(value = "/edit")
    public String editCategory(Categories categories, Model model) {

        categoriesService.saveCategories(categories);

        model.addAttribute("category", categories);
        model.addAttribute("categoryList", categoriesService.getAllCategory());
        model.addAttribute("mode", "edit");

        return "categoryList";
    }

    //    @PostMapping(value = "/delete/{category}")
//    @GetMapping(value = "delete-category")
//    @GetMapping(value = "/delete/{category}")
//    public String deleteCategory(@PathVariable("category") Long id, Model model) {
//
//        Categories category = categoriesService.findById(id);
//
//        categoriesService.deleteCategories(category);
//
//        model.addAttribute("categoryList", categoriesService.getAllCategory());
//
//        return "redirect:/admin/category/categoryList";
//    }
    @PostMapping(value = "/delete")
    public String delete(@RequestIndirectParam("categoriesList[]") List<Categories> categoriesList, RedirectAttributes redirectAttributes) {

        if (CollectionUtils.isNotEmpty(categoriesList)) {
//            ValidationResult validationResult = applicationFacade.deleteApplications(applications);
//            //Adapt message result to guaranty
//            redirectAttributes.addFlashAttribute("messageResult",
//                    MessageUtils.getMessageResult(validationResult,
//                            SucessMessage.SuccessType.CUSTOM,
//                            APPLICATION_DELETE_SUCCESS_MESSAGE));
            categoriesService.deleteAllCategories(categoriesList);
        }

        return "redirect:/admin/category/categoryList";
    }

    @PostMapping(value = "/deleteOne/{category}")
    public String deleteOne() {

        return "redirect:/admin/category/categoryList";
    }
}
