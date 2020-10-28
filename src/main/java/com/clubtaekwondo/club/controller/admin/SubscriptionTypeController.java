package com.clubtaekwondo.club.controller.admin;

import com.clubtaekwondo.club.model.SubscriptionType;
import com.clubtaekwondo.club.service.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("admin/type")
public class SubscriptionTypeController {

    private static final String TYPE = "type";

    @Autowired
    private SubscriptionTypeService subscriptionTypeService;

    @GetMapping(value = "/typeList")
    public String typeList(Model model, WebRequest request) {
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());
        return "adminPart/type/typeList";
    }

    @GetMapping(value = "/{type}")
    public String type(@PathVariable("type") Long id, Model model) {

        SubscriptionType type = subscriptionTypeService.findById(id);

        model.addAttribute(TYPE, type);

        return "adminPart/type/addType";
    }

    @GetMapping(value = "/addType")
    public String getAddType(Model model) {
        model.addAttribute(TYPE, new SubscriptionType());
        return ("adminPart/type/addType");
    }

    @PostMapping(value = "/addType")
    public String addType(SubscriptionType type, Model model) {

        subscriptionTypeService.save(type);

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

        subscriptionTypeService.save(type);

        model.addAttribute(TYPE, type);
        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return "adminPart/type/typeList";
    }

    @GetMapping(value = "/delete/{type}")
    public String deleteType(@PathVariable("type") Long id, Model model) {

        SubscriptionType type = subscriptionTypeService.findById(id);
        subscriptionTypeService.delete(type);

        model.addAttribute("typeList", subscriptionTypeService.getAllSubscriptionType());

        return "adminPart/type/typeList";
    }
}
