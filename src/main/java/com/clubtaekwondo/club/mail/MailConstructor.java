package com.clubtaekwondo.club.mail;

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
import java.util.Locale;


@Component
public class MailConstructor {
    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public  SimpleMailMessage constructResetTokenEmail(
            String lien, User user){

        String message = "\n Veuillez activer votre compte en cliquant a   ce lien :\n"+lien;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Taekwondo- Nouvelle inscription ");
        email.setText(message);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    /*public MailConstructor(Environment env) {
        this.env = env;
    }*/

}
