package br.edu.toycenter.business;

import br.edu.toycenter.api.request.EmailRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(EmailRequestDTO email) {
        var message = new SimpleMailMessage();

        message.setFrom("joaomoreiraneris0@gmail.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);
    }
}