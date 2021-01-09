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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    @PostMapping(value = "/paymentSucces")
    public String getPaymentSucces(@RequestParam(name = "response_order_id") String id, @RequestParam(name = "cust_id") Long auth, HttpServletRequest request) {
        System.out.println(id);

        User user = userService.findById(auth);
//        HttpSession ses = request.getSession(true);
//        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);

//        UsernamePasswordAuthenticationToken authReq
//                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate((Authentication) user);

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

        List<Payment> payments = paymentService.findByCode(id);
        for (Payment p : payments) {
            p.setPaymentStatus(PaymentStatus.CONFIRMED);
            p.getSubscriptionList().forEach(subscription -> subscription.setSubscriptionStatus(SubscriptionStatus.CONFIRMED));
            paymentService.save(p);
        }
        return "paymentSuccess";
    }

}
