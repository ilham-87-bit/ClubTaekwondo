package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.PaymentService;
import com.clubtaekwondo.club.service.StudentRelationService;
import com.clubtaekwondo.club.service.StudentService;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;



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
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("total", total);

        Payment payment = paymentService.createNewPayment(subscriptions);
        String url = "https://serviweb.ca/projet/transaction.php?id=" + payment.getCode() + "&prix=10.00";

        model.addAttribute("paymentUrl", url);
        model.addAttribute("payment", payment);
        return ("user/cart");
    }

    @GetMapping(value = "/transaction/{payment}")
    private String getTransaction(@PathVariable("payment") Long id, Model model) {

        User user = new User();
        Payment payment = paymentService.findById(id);
        for (Subscription subscription : payment.getSubscriptionList()) {
            user = subscription.getUser();
        }
        String cust = "client : " + new Long(user.getId()).toString();

        model.addAttribute("payment", payment);
        model.addAttribute("cust", cust);

        return "user/transaction";
    }

    @GetMapping(value = "/deleteInCart/{subscription}")
    public String deleteSubscription(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);
        Student student = subscription.getStudent();
        List<StudentRelation> studentRelations = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelations) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent())) {
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