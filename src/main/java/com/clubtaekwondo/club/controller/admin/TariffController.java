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

import java.util.Optional;

@Controller
@RequestMapping("admin/tarif")
public class TariffController {
    private static final String TARIF = "tarif";

    @Autowired
    private TariffService tariffService;
    @Autowired
    private SubscriptionPeriodService subscriptionPeriodService;
    @Autowired
    private SubscriptionTypeService subscriptionTypeService;
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping(value = "/tarifList")
    public String tariffList(Model model) {

        model.addAttribute("tarifList", tariffService.getAllTariff());

        return ("adminPart/tarif/tarifList");
    }

    @GetMapping(value = "/addTarif")
    public String getAddTariff(Model model) {

        model.addAttribute(TARIF, new Tariff());
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return ("adminPart/tarif/addTarif");
    }

    @PostMapping(value = "/addTarif")
    public String addTariff(Tariff tariff, SubscriptionPeriod subscriptionPeriod, SubscriptionType subscriptionType, Categories categories, Model model) {

        Categories cat = categoriesService.findById(categories.getIdCategory());
        SubscriptionPeriod period = subscriptionPeriodService.findById(subscriptionPeriod.getId());
        SubscriptionType type = subscriptionTypeService.findById(subscriptionType.getIdType());

        Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getCategory().getIdCategory().equals(cat.getIdCategory()) && t.getPeriod().getId().equals(period.getId()) && t.getType().getIdType().equals(type.getIdType())).findFirst();
        if (firstTariff.isPresent()) {
            model.addAttribute(TARIF, new Tariff());
            model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
            model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce tarif avec ces caractéristiques existe déjà.");
            return ("adminPart/tarif/addTarif");
        } else {
            return getTariff(tariff, cat, period, type, model);
        }

    }

    @GetMapping(value = "/delete/{tarif}")
    public String deleteTariff(@PathVariable("tarif") Long id, Model model) {

        Tariff tariff = tariffService.getTariffById(id);
        tariffService.delete(tariff);

        model.addAttribute("tarifList", tariffService.getAllTariff());

        return "redirect:/admin/tarif/tarifList";
    }

    @GetMapping(value = "/edit/{tarif}")
    public String timeDetails(@PathVariable("tarif") Long id, Model model) {

        Tariff tariff = tariffService.getTariffById(id);

        model.addAttribute(TARIF, tariff);
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return ("adminPart/tarif/addTarif");

    }

    @PostMapping(value = "/edit")
    public String editTariff(Tariff tariff, SubscriptionPeriod subscriptionPeriod, SubscriptionType subscriptionType, Categories categories, Model model) {

        Categories cat = categoriesService.findById(categories.getIdCategory());
        SubscriptionPeriod period = subscriptionPeriodService.findById(subscriptionPeriod.getId());
        SubscriptionType type = subscriptionTypeService.findById(subscriptionType.getIdType());

        Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getCategory().getIdCategory().equals(cat.getIdCategory()) && t.getPeriod().getId().equals(period.getId()) && t.getType().getIdType().equals(type.getIdType())).findFirst();
        if (firstTariff.isPresent()) {
            model.addAttribute(TARIF, tariff);
            model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
            model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce tarif avec ces caractéristiques existe déjà.");
            return ("adminPart/tarif/addTarif");
        } else {
            return getTariff(tariff, cat, period, type, model);
        }
    }

    private String getTariff(Tariff tariff, Categories categories, SubscriptionPeriod subscriptionPeriod, SubscriptionType subscriptionType, Model model) {
        tariff.setCategory(categories);
        tariff.setPeriod(subscriptionPeriod);
        tariff.setType(subscriptionType);

        tariffService.save(tariff);

        model.addAttribute(TARIF, tariff);
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "redirect:/admin/tarif/tarifList";
    }
}
