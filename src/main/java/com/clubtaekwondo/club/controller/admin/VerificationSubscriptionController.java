package com.clubtaekwondo.club.controller.admin;


import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.model.SubscriptionStatus;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
public class VerificationSubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @GetMapping(value = "/verification/{subscription}")
    public String getAllInformation(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);

        model.addAttribute("subscription", subscription);

        return "adminPart/verification";
    }

    @PostMapping(value = "/verification/{subscription}")
    public String checkedSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        subscription.setValidation(true);
        subscriptionService.save(subscription);

        model.addAttribute("subscription", subscription);
        return "redirect:/admin/indexAdmin";
    }

    @GetMapping(value = "/noValid/{subscription}")
    public String noValidSubscription(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);

        SimpleMailMessage mailMessage = mailConstructor.constructNoValidationEmail(subscription.getUser(), subscription);
        mailSender.send(mailMessage);

        model.addAttribute("subscription", subscription);

        return "redirect:/admin/indexAdmin";

    }

    @GetMapping(value = "/validSubscriptionList")
    public String getAllValidSubscription(Model model) {
        List<Subscription> validSubscriptionList = new ArrayList<>();
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        for (Subscription subscription : subscriptionList) {
            if (subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED) && subscription.getValidation()) {
                validSubscriptionList.add(subscription);
            }
        }
        model.addAttribute("validSubscriptionList", validSubscriptionList);
        return "adminPart/subscriptionValid";
    }
}
