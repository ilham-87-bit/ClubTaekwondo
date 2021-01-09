package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.model.SubscriptionPeriod;
import com.clubtaekwondo.club.service.SubscriptionPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("admin/period")
public class PeriodController {

    private static final String PERIOD = "period";

    @Autowired
    private SubscriptionPeriodService subscriptionPeriodService;

    @GetMapping(value = "/periodList")
    public String periodList(Model model, WebRequest request) {
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        return "adminPart/period/periodList";
    }

    @GetMapping(value = "/addPeriod")
    public String getAddPeriod(SubscriptionPeriod period, Model model) {
        model.addAttribute(PERIOD, new SubscriptionPeriod());
        return ("adminPart/period/addPeriod");
    }

    @PostMapping(value = "/addPeriod")
    public String addPeriod(SubscriptionPeriod period, Model model) {

        Optional<SubscriptionPeriod> firstPeriod = subscriptionPeriodService.getAllPeriod().stream().filter(p -> p.getPeriod().equals(period.getPeriod())).findFirst();
        if (firstPeriod.isPresent()) {
            model.addAttribute(PERIOD, new SubscriptionPeriod());
            model.addAttribute("messageError", "Cette durée d'abonnement existe déjà.");
            return ("adminPart/period/addPeriod");
        } else {
            subscriptionPeriodService.save(period);
        }

        model.addAttribute(PERIOD, period);
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());
        return "adminPart/period/periodList";
    }

    @GetMapping(value = "/edit/{period}")
    public String periodDetails(@PathVariable("period") Long id, Model model) {

        SubscriptionPeriod period = subscriptionPeriodService.findById(id);

        model.addAttribute(PERIOD, period);

        return "adminPart/period/addPeriod";
    }

    @PostMapping(value = "/edit")
    public String editPeriod(SubscriptionPeriod period, Model model) {

        Optional<SubscriptionPeriod> firstPeriod = subscriptionPeriodService.getAllPeriod().stream().filter(p -> p.getPeriod().equals(period.getPeriod()) && !p.getId().equals(period.getId())).findFirst();
        if (firstPeriod.isPresent()) {
            model.addAttribute(PERIOD, period);
            model.addAttribute("messageError", "Cette durée d'abonnement existe déjà.");
            return ("adminPart/period/addPeriod");
        } else {
            subscriptionPeriodService.save(period);
        }

        model.addAttribute(PERIOD, period);
        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());

        return "adminPart/period/periodList";
    }

    @GetMapping(value = "/delete/{period}")
    public String deletePeriod(@PathVariable("period") Long id, Model model) {

        SubscriptionPeriod period = subscriptionPeriodService.findById(id);
        subscriptionPeriodService.delete(period);

        model.addAttribute("periodList", subscriptionPeriodService.getAllPeriod());

        return "adminPart/period/periodList";
    }
}
