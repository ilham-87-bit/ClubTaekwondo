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
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("user")
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
    @Autowired
    private TimeTableService timeTableService;


    @GetMapping(value = "/addStudent/{subscription}")
    public String getAddStudent(@PathVariable("subscription") Long id, Model model) {
        Subscription subscription = subscriptionService.findById(id);

        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Student student = new Student();
        if (subscription.getStudent() != null) {
            student = subscription.getStudent();
        }

        try {
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);
            model.addAttribute("cityList", cityService.getAllCity());
            model.addAttribute("subscriptions", subscriptionService.getCart());

            return "user/addStudent";
        } catch (Exception e) {
            String url = "/user/addSubscriptionPart2/" + subscription.getIdSubscription();
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
            List<Student> studentList = studentService.getAllStudent();
            Student student = new Student();
            if (subscription.getStudent() == null) {
                for (Student student1 : studentList) {
                    if (student1.getNationalRegistry().equals(studentDTO.getNationalRegistry())) {
                        model.addAttribute("messageError", "cet éleve exciste déja!");
                        model.addAttribute("subscription", subscription);
                        model.addAttribute("student", new Student());
                        model.addAttribute("cityList", cityService.getAllCity());
                        model.addAttribute("subscriptions", subscriptionService.getCart());
                        return "user/addStudent";
                    }
                }
            }

            student = initializeStudent(studentDTO, city);
            if (subscription.getStudent() != null) {
                student.setIdStudent(subscription.getStudent().getIdStudent());
            }
            Student s = studentService.save(student);
            subscription.setStudent(s);
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);
            model.addAttribute("subscriptions", subscriptionService.getCart());

            return "redirect:/user/addContactPerson/" + student.getIdStudent();

        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/user/addStudent/" + subscription.getIdSubscription();
        }
    }

    @GetMapping(value = "/addContactPerson/{student}")
    public String getAddContactPerson(@PathVariable("student") Long id, Model model) {
        Subscription s = new Subscription();
        ContactPerson contactPerson = new ContactPerson();
        StudentRelation studentRelation = new StudentRelation();
        Student student = studentService.findById(id);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation1 : studentRelationList) {
            if (studentRelation1.getStudent().getIdStudent().equals(student.getIdStudent())) {
                contactPerson = studentRelation1.getContactPerson();
                studentRelation = studentRelation1;
            }
        }
//        if (!student.getPerson().isEmpty()) {
//            for (StudentRelation studentRelation1 : student.getPerson()) {
//                contactPerson = studentRelation1.getContactPerson();
//                studentRelation = studentRelation1;
//            }
//        }

        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", studentRelation);
        model.addAttribute("contactPerson", contactPerson);
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

        student.getPerson().add(sr);
        studentService.save(student);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", sr);
        model.addAttribute("contactPerson", c);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        return "redirect:/user/summary/" + s.getIdSubscription();
    }

    @GetMapping(value = "/mySubscription")
    private String getMySubscription(Model model) {

        Map<Subscription, List<TimeTable>> infoList = new HashMap<>();
        Date date = new Date();

        List<Subscription> subscriptionByUser = new ArrayList<>();
        List<TimeTable> timeTablesStudent = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        List<TimeTable> timeTableList = timeTableService.getAllTimeTable();

        for (Subscription subscription : subscriptionList) {
            if (subscription.getUser().getId().equals(user.getId())) {
                subscriptionByUser.add(subscription);
            }
        }
        if (!subscriptionByUser.isEmpty()) {
            for (Subscription subscription : subscriptionByUser) {
                if (subscription.getEndDate().before(date)) {
                    subscription.setValidation(false);
                }
                for (TimeTable timeTable : timeTableList) {
                    if (subscription.getSchool().equals(timeTable.getS()) && subscription.getCategories().equals(timeTable.getC()) && subscription.getSubscriptionType().equals(timeTable.getSubscriptionType())) {
                        timeTablesStudent.add(timeTable);
                    }
                }
                infoList.put(subscription, timeTablesStudent);
            }
        }
        model.addAttribute("subscriptions", subscriptionService.getCart());
        model.addAttribute("infoList", infoList);

        return "user/mySubscription";
    }

//    @GetMapping(value = "/editStudent/{subscription}/{student}")
//    public String getEditStudent(@PathVariable("subscription") Long id, @PathVariable("student") Long idStudent, Model model) {
//
//        Student student = studentService.findById(idStudent);
//        Subscription subscription = subscriptionService.findById(id);
//
//        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        try {
//            model.addAttribute("subscription", subscription);
//            model.addAttribute("student", new Student());
//            model.addAttribute("cityList", cityService.getAllCity());
//            model.addAttribute("subscriptions", subscriptionService.getCart());
//
//            return "user/addStudent";
//        } catch (Exception e) {
//            String url = "/user/addSubscriptionPart2/" + subscription.getIdSubscription();
//            model.addAttribute("messageError", "erreur, veuillez essayer");
//            model.addAttribute("subscriptions", subscriptionService.getCart());
//            return "redirect:" + url;
//        }
//    }

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

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date.getTime());
    }
}
