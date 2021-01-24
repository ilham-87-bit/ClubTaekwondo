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
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping(value = "/tarifList")
    public String tariffList(Model model) {

        model.addAttribute("tarifList", tariffService.getAllTariff());
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

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

        Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getTariffPK().getIdCategory().equals(cat.getIdCategory()) && t.getTariffPK().getIdPeriod().equals(period.getId()) && t.getTariffPK().getIdType().equals(type.getIdType())).findFirst();
        if (firstTariff.isPresent()) {
            model.addAttribute(TARIF, new Tariff());
            model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
            model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce tarif avec ces caractéristiques existe déjà.");
            return ("adminPart/tarif/addTarif");
        } else {
            return getTariff(tariff, categories.getIdCategory(), subscriptionPeriod.getId(), subscriptionType.getIdType(), model);
        }

    }

//    @GetMapping(value = "/delete/{category}/{period}/{type}")
//    public String deleteTariff(@PathVariable("category") Long idCategory, @PathVariable("period") Long idPeriod, @PathVariable("type") Long idType, Model model) {
//
//        TariffPK tariffPK = new TariffPK();
//        tariffPK.setIdType(idType);
//        tariffPK.setIdPeriod(idPeriod);
//        tariffPK.setIdCategory(idCategory);
//        Tariff tariff = tariffService.getOneTariff(tariffPK);
//        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(s -> s.getCategories().getIdCategory().equals(tariff.getTariffPK().getIdCategory()) && s.getSubscriptionType().getIdType().equals(tariff.getTariffPK().getIdType()) && s.getSubscriptionPeriod().getId().equals(tariff.getTariffPK().getIdPeriod()) && s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)).findFirst();
//        if (firstSubscription.isPresent()) {
//            model.addAttribute("messageError", "Vous ne pouvez pas supprimer cet horaire ! Cet horaire est lié à des abonnements en cours.");
//
//        } else {
//            tariffService.delete(tariff);
//        }
//
//        model.addAttribute("tarifList", tariffService.getAllTariff());
//
//        return "redirect:/admin/tarif/tarifList";
//    }

    @GetMapping(value = "/edit/{category}/{period}/{type}")
    public String timeDetails(@PathVariable("category") Long idCategory, @PathVariable("period") Long idPeriod, @PathVariable("type") Long idType, Model model) {

        TariffPK tariffPK = new TariffPK();
        tariffPK.setIdType(idType);
        tariffPK.setIdPeriod(idPeriod);
        tariffPK.setIdCategory(idCategory);
        Tariff tariff = tariffService.getOneTariff(tariffPK);
        tariff.setExist(false);

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

        Optional<Tariff> firstTariff = tariffService.getAllTariff().stream().filter(t -> t.getTariffPK().getIdCategory().equals(cat.getIdCategory()) && t.getTariffPK().getIdPeriod().equals(period.getId()) && t.getTariffPK().getIdType().equals(type.getIdType()) && t.isExist()).findFirst();
        if (firstTariff.isPresent()) {
            model.addAttribute(TARIF, tariff);
            model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
            model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
            model.addAttribute("categoryList", categoriesService.getAllCategory());
            model.addAttribute("messageError", "Ce tarif avec ces caractéristiques existe déjà.");
            return ("adminPart/tarif/addTarif");
        } else {
            return getTariff(tariff, categories.getIdCategory(), subscriptionPeriod.getId(), subscriptionType.getIdType(), model);
        }
    }

    private String getTariff(Tariff tariff, Long idCategory, Long idPeriod, Long idType, Model model) {
        TariffPK tariffPK = new TariffPK();
        tariffPK.setIdCategory(idCategory);
        tariffPK.setIdPeriod(idPeriod);
        tariffPK.setIdType(idType);
        tariff.setTariffPK(tariffPK);
        tariff.setExist(true);

        tariffService.save(tariff);

        model.addAttribute(TARIF, tariff);
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        model.addAttribute("categoryList", categoriesService.getAllCategory());

        return "redirect:/admin/tarif/tarifList";
    }
}
