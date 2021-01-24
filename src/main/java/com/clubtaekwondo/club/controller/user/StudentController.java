package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.*;
import com.clubtaekwondo.club.service.*;
import org.dom4j.util.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Years;
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
    @Autowired
    private CategoriesService categoriesService;


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
            model.addAttribute("map", getAllPostalCodeByCity());

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

        Date date = new Date();
        Subscription subscription = subscriptionService.findById(id);
        City c = cityService.findById(city.getIdCity());
        try {
            List<Student> studentList = studentService.getAllStudent();
            Student student = initializeStudent(studentDTO, c);
            if (subscription.getStudent() == null) {
                for (Student student1 : studentList) {
                    if (student1.getNationalRegistry().equals(studentDTO.getNationalRegistry())) {
                        model.addAttribute("messageError", "cet éleve exciste déja!");
                        model.addAttribute("subscription", subscription);
                        model.addAttribute("student", student);
                        model.addAttribute("cityList", cityService.getAllCity());
                        model.addAttribute("subscriptions", subscriptionService.getCart());
                        return "user/addStudent";
                    }
                }
            }

            int cptYear = Math.abs(Years.yearsBetween(LocalDate.fromDateFields(student.getBirthDay()), LocalDate.fromDateFields(date)).getYears());

//            if (cptYear > subscription.getCategories().getAge()) {
//                model.addAttribute("messageError", "La catégorie que vous avez choisie n'est pas compatible avec la date de naissance que vous avez introduite!");
//                model.addAttribute("subscription", subscription);
//                model.addAttribute("student", student);
//                model.addAttribute("cityList", cityService.getAllCity());
//                model.addAttribute("subscriptions", subscriptionService.getCart());
//                return "user/addStudent";
//            }

            if (subscription.getStudent() != null) {
                student.setIdStudent(subscription.getStudent().getIdStudent());
            }
            Student s = studentService.save(student);
            subscription.setStudent(s);
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
            model.addAttribute("student", student);
            model.addAttribute("subscriptions", subscriptionService.getCart());

            return "redirect:/user/contactPersonList/" + student.getIdStudent();

        } catch (Exception e) {
            model.addAttribute("messageError", "erreur, veuillez essayer");
            model.addAttribute("subscriptions", subscriptionService.getCart());
            return "redirect:/user/addStudent/" + subscription.getIdSubscription();
        }
    }

    @GetMapping(value = "/contactPersonList/{student}")
    private String getContactPersonByStudent(@PathVariable("student") Long id, Model model) {

        Map<String, ContactPerson> listMap = new HashMap<>();

        Student student = studentService.findById(id);
        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            Subscription s = firstSubscription.get();
            model.addAttribute("subscription", s);
        }

        for (StudentRelation studentRelation : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent())) {
                listMap.put(getRelationShip(studentRelation), contactPersonService.findById(studentRelation.getStudentRelationPK().getIdContactPerson()));
            }
        }
        model.addAttribute("subscriptions", subscriptionService.getCart());
        model.addAttribute("contactPersonList", listMap);
        model.addAttribute("student", student);

        return "user/contactPersonList";
    }

    @GetMapping(value = "/addContactPerson/{student}")
    public String getAddContactPerson(@PathVariable("student") Long id, Model model) {
        Subscription s = new Subscription();
        Student student = studentService.findById(id);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }

        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("studentRelation", new StudentRelation());
        model.addAttribute("subscriptions", subscriptionService.getCart());
        if (s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
            model.addAttribute("previous", true);
        }

        return "user/addContactPerson";
    }

    @PostMapping(value = "/addContactPerson/{student}")
    public String addContactPerson(@PathVariable("student") Long id, StudentRelation studentRelation, ContactPerson contactPerson, Model model) {
        Subscription s = new Subscription();

        Student student = studentService.findById(id);
        ContactPerson c = contactPersonService.save(contactPerson);

        StudentRelationPK studentRelationPK = new StudentRelationPK();
        studentRelationPK.setIdContactPerson(c.getIdPerson());
        studentRelationPK.setIdStudent(student.getIdStudent());
        studentRelation.setStudentRelationPK(studentRelationPK
        );
        StudentRelation sr = studentRelationService.save(studentRelation);

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

        if (s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
            return "redirect:/user/personList/" + s.getIdSubscription();
        } else {
            return "redirect:/user/contactPersonList/" + student.getIdStudent();
        }
    }

    @GetMapping(value = "/editContactPerson/{person}/{student}")
    public String geteditContactPerson(@PathVariable("person") Long idPerson, @PathVariable("student") Long idStudent, Model model) {

        Subscription s = new Subscription();
        ContactPerson contactPerson = contactPersonService.findById(idPerson);

        Student student = studentService.findById(idStudent);
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent()) && studentRelation.getStudentRelationPK().getIdContactPerson().equals(contactPerson.getIdPerson())) {

                model.addAttribute("studentRelation", studentRelation);
            }
        }

        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("contactPerson", contactPerson);
        model.addAttribute("subscriptions", subscriptionService.getCart());
        if (s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
            model.addAttribute("previous", true);
        }

        return "user/addContactPerson";
    }

    @PostMapping(value = "/editContactPerson/{student}")
    public String editContactPerson(@PathVariable("student") Long idStudent, StudentRelation studentRelation, ContactPerson contactPerson, Model model) {
        Subscription s = new Subscription();

        Student student = studentService.findById(idStudent);
        contactPersonService.save(contactPerson);

        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation1 : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent()) && studentRelation1.getStudentRelationPK().getIdContactPerson().equals(contactPerson.getIdPerson())) {
                studentRelation1.setRelationship(studentRelation.getRelationship());
                studentRelationService.save(studentRelation1);
            }
        }

        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            s = firstSubscription.get();
        }
        model.addAttribute("subscription", s);
        model.addAttribute("student", student);
        model.addAttribute("contactPerson", contactPerson);
        model.addAttribute("subscriptions", subscriptionService.getCart());

        if (s.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
            return "redirect:/user/personList/" + s.getIdSubscription();
        } else {
            return "redirect:/user/contactPersonList/" + student.getIdStudent();
        }
    }

    @GetMapping(value = "/deleteContactPerson/{person}/{student}")
    public String deleteContactPerson(@PathVariable("person") Long idPerson, @PathVariable("student") Long idStudent, Model model) {

        Date date = new Date();
        Subscription subscription = new Subscription();
        List<StudentRelation> relationList = new ArrayList<>();
        ContactPerson contactPerson = contactPersonService.findById(idPerson);
        Student student = studentService.findById(idStudent);

        Optional<Subscription> firstSubscription = subscriptionService.getAllSubscription().stream().filter(su -> su.getStudent().getIdStudent().equals(student.getIdStudent()))
                .findFirst();
        if (firstSubscription.isPresent()) {
            subscription = firstSubscription.get();
            model.addAttribute("subscription", subscription);
        }

        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();
        for (StudentRelation studentRelation : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent())) {
                relationList.add(studentRelation);
            }
        }

        int cptYear = Math.abs(Years.yearsBetween(LocalDate.fromDateFields(student.getBirthDay()), LocalDate.fromDateFields(date)).getYears());

        if (cptYear < 18 && relationList.size() <= 1) {
            model.addAttribute("messageError", " Cet abonnement est lié à un élève mineur, vous ne pouvez pas supprimer la seule personne de contact (vous devez ajouter une autre personne pour pouvoir supprimer celui-là)");

        } else {
            for (StudentRelation studentRelation : studentRelationList) {
                if (studentRelation.getStudentRelationPK().getIdContactPerson().equals(contactPerson.getIdPerson()) && studentRelation.getStudentRelationPK().getIdStudent().equals(student.getIdStudent())) {
                    studentRelationService.delete(studentRelation);
                }
            }
        }
        model.addAttribute("subscriptions", subscriptionService.getCart());

        if (subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
            return "redirect:/user/personList/" + subscription.getIdSubscription();
        } else {
            return "redirect:/user/contactPersonList/" + student.getIdStudent();
        }
    }

    @GetMapping(value = "/mySubscription")
    private String getMySubscription(Model model) {

        Date date = new Date();
        boolean validDate = true;

        List<Subscription> subscriptionByUser = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Subscription> subscriptionList = subscriptionService.getAllSubscription();
        List<Subscription> list = new ArrayList<>();

        for (Subscription subscription : subscriptionList) {
            if (subscription.getUser().getId().equals(user.getId())) {
                subscriptionByUser.add(subscription);
            }
        }
        if (!subscriptionByUser.isEmpty()) {
            for (Subscription subscription : subscriptionByUser) {
                if (subscription.getEndDate().before(date)) {
                    subscription.setValidation(false);
                    validDate = false;
                } else {
                    validDate = true;
                }
                if (subscription.getSubscriptionStatus().equals(SubscriptionStatus.CONFIRMED)) {
                    list.add(subscription);
                }
            }
        }
        model.addAttribute("subscriptions", subscriptionService.getCart());
        model.addAttribute("list", list);
        model.addAttribute("validDate", validDate);

        return "user/mySubscription";
    }

    @GetMapping(value = "/myTime/{subscription}")
    private String getMyTime(@PathVariable("subscription") Long id, Model model) {

        List<TimeTable> myTime = new ArrayList<>();
        Subscription subscription = subscriptionService.findById(id);

        List<TimeTable> timeTableList = timeTableService.getAllTimeTable();

        for (TimeTable timeTable : timeTableList) {
            if (timeTable.getS().getIdSchool().equals(subscription.getSchool().getIdSchool()) && timeTable.getC().getIdCategory().equals(subscription.getCategories().getIdCategory()) && timeTable.getSubscriptionTypeList().contains(subscription.getSubscriptionType())) {
                myTime.add(timeTable);
            }
        }

        model.addAttribute("myTime", myTime);
        model.addAttribute("subscription", subscription);
        model.addAttribute("student", subscription.getStudent());

        return "user/myTime";
    }

    @GetMapping(value = "/personList/{subscription}")
    private String getPersonByStudent(@PathVariable("subscription") Long id, Model model) {

        Subscription subscription = subscriptionService.findById(id);

        List<StudentRelation> studentRelationList = studentRelationService.getAllStudentRelation();

        Map<String, ContactPerson> map = new HashMap<>();
        for (StudentRelation studentRelation : studentRelationList) {
            if (studentRelation.getStudentRelationPK().getIdStudent().equals(subscription.getStudent().getIdStudent())) {
                map.put(getRelationShip(studentRelation), contactPersonService.findById(studentRelation.getStudentRelationPK().getIdContactPerson()));
            }
        }
        model.addAttribute("personList", map);
        model.addAttribute("subscription", subscription);

        return "user/personList";
    }

    private String getRelationShip(StudentRelation studentRelation) {
        String lien = "";
        if (studentRelation.getRelationship() != null) {
            if (studentRelation.getRelationship().equals(RelationType.FATHER.toString())) {
                lien = "Pére";
            }
            if (studentRelation.getRelationship().equals(RelationType.MOTHER.toString())) {
                lien = "Mére";
            }
            if (studentRelation.getRelationship().equals(RelationType.OTHERS.toString())) {
                lien = "Autre";
            }
        }
        return lien;
    }

    private Student initializeStudent(StudentDTO studentDTO, City c) throws ParseException {
        Student student = new Student();
        student.setName(studentDTO.getName());
        if (studentDTO.getEmail() != "") {
            student.setEmail(studentDTO.getEmail());
        }
        if (studentDTO.getGsm() != "") {
            student.setGsm(Integer.parseInt(studentDTO.getGsm()));
        }

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

    public Map<Long, String> getAllPostalCodeByCity() {
        Map<Long, String> map = new HashMap<>();

        List<City> cityList = cityService.getAllCity();
        for (City city : cityList) {
            map.put(city.getIdCity(), city.getPostalCode());
        }
        return map;
    }
}
