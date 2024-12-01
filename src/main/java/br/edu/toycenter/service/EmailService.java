package br.edu.toycenter.service;

import br.edu.toycenter.controller.dto.request.EmailRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(EmailRequestDTO email) {
        var message = new SimpleMailMessage();

        message.setFrom("joaomoreiraneris0@gmail.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);
    }
}