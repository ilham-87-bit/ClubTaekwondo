package com.clubtaekwondo.club.mail;

import com.clubtaekwondo.club.controller.contact.Contact;
import com.clubtaekwondo.club.model.School;
import com.clubtaekwondo.club.model.Subscription;
import com.clubtaekwondo.club.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;


@Component
public class MailConstructor {
    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public final String MAIL_ADMIN = "clubtaekwondo.asbl@gmail.com";

    public SimpleMailMessage constructResetTokenEmail(
            String lien, User user) {

        String message = "\n Veuillez activer votre compte en cliquant a ce lien :\n" + lien;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Taekwondo- Nouvelle inscription ");
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    public SimpleMailMessage constructResetPasswordEmail(
            String lien, User user) {

        String message = "\n Veuillez confirmer la demande de changement de mot de passe en cliquant a ce lien :\n" + lien;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Taekwondo- Changement de mot de passe  ");
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    public SimpleMailMessage constructNoValidationEmail(User user, Subscription subscription) {

        String message = "\n Il y a un problème au niveau de votre abonnement, veuillez nous contacter sur cette adresse mail pour plus d'information.\n  Concernant :" + subscription.getStudent().getFirstName() + ' ' + subscription.getStudent().getName();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Problème de validation");
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    public SimpleMailMessage constructContactEmail(Contact contact) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(MAIL_ADMIN);
        email.setSubject(contact.getSubject());
        email.setText(contact.getEmail() + " - " + contact.getMessage());
        email.setFrom(env.getProperty(contact.getEmail()));
        return email;
    }

    public SimpleMailMessage constructSchoolDelete(User user, School school) {

        String message = "\n Bonjour  " + user.getFirstName().toLowerCase(Locale.ROOT) + ' ' + user.getLastName().toUpperCase(Locale.ROOT) + "," + "\n Nous vous informons que l'école : " + school.getName() + ' ' + school.getAddress().getStreet() + ' ' + school.getAddress().getNumber() + " - " + school.getAddress().getCity().getPostalCode() + ' ' + school.getAddress().getCity().getCityName() + "\n n'appartient plus au club, si vous voulez continuer vous avez la possibilité de choisir une autre école. Le cas échéant, vous serez remboursé pour le reste. \n  Cordialement,";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Changement d'école");
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
