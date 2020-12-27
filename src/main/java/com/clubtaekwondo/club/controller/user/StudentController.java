package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CityService cityService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ContactPersonService contactPersonService;
    @Autowired
    private StudentRelationService studentRelationService;


    @GetMapping(value = "/addStudent/{subscription}")
    public String getAddStudent(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", new Student());
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("subscriptions", subscriptionService.getCart());

            return "user/addStudent";
        } catch (Exception e) {
            String url = "/addSubscriptionPart2/" + subscription.getIdSubscription();
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:" + url;
        }
    }

    @PostMapping(value = "/addStudent/{subscription}")
    public String addStudent(@PathVariable("subscription") Long id, StudentDTO studentDTO, City city, Model model) {
        Subscription subscription = subscriptionService.findById(id);
        City c = cityService.findById(city.getIdCity());
        try {
            Student student = initializeStudent(studentDTO, city);
            Student s = studentService.save(student);
            subscription.setStudent(s);
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);
            model.addAttribute("subscriptions", subscriptionService.getCart());
//            url = "/addContactPerson/" + student.getIdStudent();

            return "redirect:/addContactPerson/" + student.getIdStudent();

        } catch (Exception e) {
//            url = "/addStudent/" + subscription.getIdSubscription();
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/addStudent/" + subscription.getIdSubscription();
        }
    }

    @GetMapping(value = "/addContactPerson/{student}")
    public String getAddContactPerson(@PathVariable("student") Long id, Model model) {
        Subscription s = new Subscription();
        Student student = studentService.findById(id);
//        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", new StudentRelation());
        model.addAttribute("contactPerson", new ContactPerson());
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "user/addContactPerson";
    }

    @PostMapping(value = "/addContactPerson/{student}")
    public String addContactPerson(@PathVariable("student") Long id, StudentRelation studentRelation, ContactPerson contactPerson, Model model) {
        Subscription s = new Subscription();

        Student student = studentService.findById(id);
        ContactPerson c = contactPersonService.save(contactPerson);
        studentRelation.setStudent(student);
        studentRelation.setContactPerson(c);
        StudentRelation sr = studentRelationService.save(studentRelation);

        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
//        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
//        for (Subscription subscription : subscriptionList) {
//            if (subscription.getStudent().getIdStudent() == student.getIdStudent()) {
//                s = subscription;
//            }
//        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", studentRelation);
        model.addAttribute("contactPerson", c);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "redirect:/summary/" + s.getIdSubscription();
    }

    private Student initializeStudent(StudentDTO studentDTO, City c) throws ParseException {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setGsm(new Integer(studentDTO.getGsm()));

        studentDTO.getAddress().setCity(c);
        Optional<Address> firstAddress = addressService.getAllAddress().stream().filter(a -> a.getStreet().equals(studentDTO.getAddress().getStreet()) && a.getNumber().equals(studentDTO.getAddress().getNumber()) && a.getCity().equals(studentDTO.getAddress().getCity()))
                .findFirst();
        if (firstAddress.isPresent()) {
            studentDTO.setAddress(firstAddress.get());
        } else {
            addressService.save(studentDTO.getAddress());
        }
        student.setAddress(studentDTO.getAddress());
        student.setNationalRegistry(studentDTO.getNationalRegistry());
        student.setFirstName(studentDTO.getFirstName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateAsString = studentDTO.getBirthDay();
        Date date = sdf.parse(dateAsString);
        student.setBirthDay(date);

        return student;
    }
}
