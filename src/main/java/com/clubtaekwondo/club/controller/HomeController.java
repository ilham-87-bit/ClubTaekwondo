package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.controller.contact.Contact;
import com.clubtaekwondo.club.mail.MailConstructor;
import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.PaymentService;
import com.clubtaekwondo.club.service.SubscriptionService;
import com.clubtaekwondo.club.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private SubscriptionService service;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping(value = "/index")
    public String home(Model model) {
        model.addAttribute("subscriptions", service.getCart());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return ("index");
    }

    @GetMapping(value = "/admin/indexAdmin")
    public String homeAdmin(Model model) {

        List<Subscription> subscriptionConfirmed = new ArrayList<>();
        List<Subscription> subscriptionList = service.getAllSubscription();
        for (Subscription subscription : subscriptionList) {
            if (subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED) && !subscription.getValidation()) {
                subscriptionConfirmed.add(subscription);
            }
        }
        model.addAttribute("subscriptionConfirmed", subscriptionConfirmed);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return ("indexAdmin");
    }

    @GetMapping(value = "/contact")
    public String getContact() {

        return "contact";
    }

    @PostMapping(value = "/contact")
    public String sendMail(Contact contact, Model model) {
        SimpleMailMessage mailMessage = mailConstructor.constructContactEmail(contact);
        mailSender.send(mailMessage);
        model.addAttribute("contact", contact);
        return "confirmMail";
    }

    @GetMapping(value = "/paymentSucces")
    public String getPaymentSucces(@RequestParam(name = "response_order_id") String id, @RequestParam(name = "response_cust_id") String idCust, HttpServletRequest request) {
        System.out.println(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("User should be connected");
        }
        User user = (User) authentication.getPrincipal();


        List<Payment> payments = paymentService.findByCode(id);
        for (Payment p : payments) {
            p.setPaymentStatus(PaymentStatus.CONFIRMED);
            p.getSubscriptionList().forEach(subscription -> subscription.setSubscriptionStatus(SubscriptionStatus.CONFIRMED));
            paymentService.save(p);
        }
        return "paymentSuccess";
    }

}
