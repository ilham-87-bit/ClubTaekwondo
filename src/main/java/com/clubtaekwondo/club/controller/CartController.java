package com.clubtaekwondo.club.controller;

import com.clubtaekwondo.club.model.ContactPerson;
import com.clubtaekwondo.club.model.Student;
import com.clubtaekwondo.club.model.StudentRelation;
import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.service.StudentRelationService;
import com.clubtaekwondo.club.service.StudentService;
import com.clubtaekwondo.club.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private StudentRelationService studentRelationService;
    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/cart")
    public String home(Model model) {
        List<Subscription> subscriptions = subscriptionService.getCart();
        float total = totalAmount(subscriptions);
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("total", total);
        return ("user/cart");
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

    @GetMapping(value = "/paymentSuccess")
    public String getPaymentSuccess() {
        return "paymentSuccess";
    }
}
