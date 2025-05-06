package com.linkedin_clone_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Method to send email
    public void sendEmail(String to, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);  // Recipient email
        message.setSubject(subject);  // Subject of the email
        message.setText(messageBody);  // Email body

        // Send email
        javaMailSender.send(message);
    }
}