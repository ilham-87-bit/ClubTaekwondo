package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private SubscriptionService service;


    @GetMapping(value = "/index")
    public String home(Model model) {
        model.addAttribute("subscriptions", service.getCart());
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.isAuthenticated()) {
//            User user = (User) authentication;
//            model.addAttribute("user", user);
//        }
        System.out.println(authentication.getPrincipal());
        return ("index");
    }

    @GetMapping(value = "/admin/indexAdmin")
    public String homeAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return ("indexAdmin");
    }

}
