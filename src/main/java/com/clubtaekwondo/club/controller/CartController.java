package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.PaymentService;
import com.clubtaekwondo.club.service.StudentRelationService;
import com.clubtaekwondo.club.service.StudentService;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


@Controller
@RequestMapping("user")
public class CartController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private StudentRelationService studentRelationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/cart")
    public String home(Model model, HttpServletRequest req) {
        List<Subscription> subscriptions = subscriptionService.getCart();
        float total = totalAmount(subscriptions);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("total", total);

        Payment payment = paymentService.createNewPayment(subscriptions);
        String url = "https://serviweb.ca/projet/transaction.php?id=" + payment.getCode() + "&prix=10.00";
//        String url = "https://esqa.moneris.com/HPPDP/index.php?id=" + payment.getCode() + "&prix=10.00&ps_store_id=" + user.getId();

        model.addAttribute("paymentUrl", url);
        model.addAttribute("payment", payment);
        model.addAttribute("auth", auth);
        return ("user/cart");
    }

    @GetMapping(value = "/transaction/{payment}")
    private String getTransaction(@PathVariable("payment") Long id, Model model, HttpServletRequest request) {

        Payment payment = paymentService.findById(id);
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        HttpSession session = request.getSession(true);
//        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, auth);

        model.addAttribute("payment", payment);
        model.addAttribute("auth", auth);

        return "user/transaction";
    }

    @GetMapping(value = "/deleteInCart/{subscription}")
    public String deleteSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        Student student = subscription.getStudent();
        List<StudentRelation> studentRelations = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelations) {
            if (studentRelation.getStudent().equals(student)) {
                studentRelationService.delete(studentRelation);
            }
        }
        studentService.delete(student);
        subscriptionService.delete(subscription);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "redirect:/cart";
    }

    private float totalAmount(List<Subscription> subscriptionList) {
        float totalAmount = 0;
        for (Subscription subscription : subscriptionList) {
            totalAmount = totalAmount + subscription.getTotalPrice();
        }
        return totalAmount;
    }
}