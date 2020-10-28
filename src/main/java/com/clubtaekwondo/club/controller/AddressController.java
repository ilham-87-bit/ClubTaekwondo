package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.model.Address;
import com.clubtaekwondo.club.model.Categories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddressController {


    @GetMapping(value = "/addAddress")
    public String getAddAddress(Address address, Model model) {
        model.addAttribute("address", new Address());
        return ("addAddress");
    }

    @PostMapping(value = "/addAddress")
    public String addAddress(Address address, Model model) {

//        categoriesService.saveCategories(categories);
//
//        model.addAttribute("address", categories);
//        model.addAttribute("addressList", categoriesService.getAlladdress());
//        model.addAttribute("mode", "add");
        return "index";
    }

}
