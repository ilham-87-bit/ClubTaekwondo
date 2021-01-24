package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.SubscriptionService;
import com.clubtaekwondo.club.service.SubscriptionTypeService;
import com.clubtaekwondo.club.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/type")
public class SubscriptionTypeController {

    private static final String TYPE = "type";

    @Autowired
    private SubscriptionTypeService subscriptionTypeService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TariffService tariffService;

    @GetMapping(value = "/typeList")
    public String typeList(Model model, WebRequest request) {
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        return "adminPart/type/typeList";
    }

    @GetMapping(value = "/addType")
    public String getAddType(Model model) {
        model.addAttribute(TYPE, new SubscriptionType());
        return ("adminPart/type/addType");
    }

    @PostMapping(value = "/addType")
    public String addType(SubscriptionType type, Model model) {

        Optional<SubscriptionType> firstType = subscriptionTypeService.getAllSubscriptionType().stream().filter(t -> t.getNbrHours().equals(type.getNbrHours())).findFirst();
        if (firstType.isPresent()) {
            model.addAttribute(TYPE, new SubscriptionType());
            model.addAttribute("messageError", "Ce type d'abonnement existe déjà.");
            return ("adminPart/type/addType");
        } else {
            subscriptionTypeService.save(type);
            model.addAttribute("messageSuccess", " Le type d'abonnement a bien été ajouté. ");
        }

        model.addAttribute(TYPE, type);
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        return "adminPart/type/typeList";
    }

    @GetMapping(value = "/edit/{type}")
    public String typeDetails(@PathVariable("type") Long id, Model model) {

        SubscriptionType type = subscriptionTypeService.findById(id);

        model.addAttribute(TYPE, type);

        return "adminPart/type/addType";
    }

    @PostMapping(value = "/edit")
    public String editType(SubscriptionType type, Model model) {

        Optional<SubscriptionType> firstType = subscriptionTypeService.getAllSubscriptionType().stream().filter(t -> t.getNbrHours().equals(type.getNbrHours())).findFirst();
        if (firstType.isPresent()) {
            model.addAttribute(TYPE, type);
            model.addAttribute("messageError", "Ce type d'abonnement existe déjà.");
            return ("adminPart/type/addType");
        } else {
            subscriptionTypeService.save(type);
            model.addAttribute("messageSuccess", " Le type d'abonnement a bien été ajouté.");
        }

        model.addAttribute(TYPE, type);
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return "adminPart/type/typeList";
    }

    @GetMapping(value = "/delete/{type}")
    public String deleteType(@PathVariable("type") Long id, Model model) {

        SubscriptionType type = subscriptionTypeService.findById(id);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(s -> s.getSubscriptionType().getIdType().equals(type.getIdType()) && s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)).findFirst();
        if (firstSubscription.isPresent()) {
            model.addAttribute("messageError", "Vous ne pouvez pas supprimer ce type d'abonnement ! Ce type d'abonnement est liée à des abonnements en cours.");
        } else {
            List<Tariff> tariffList = tariffService.getAllTariff();
            for (Tariff tariff : tariffList) {
                if (tariff.getTariffPK().getIdType().equals(type.getIdType())) {
                    tariffService.delete(tariff);
                }
            }
            subscriptionTypeService.delete(type);
        }

        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return "adminPart/type/typeList";
    }
}
