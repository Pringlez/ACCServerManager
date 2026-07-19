package org.accmanager.service.services.email;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender javaMailSender;

    @Value("${mail.test:false}")
    private Boolean mailTest = false;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        if (mailTest) {
            logger.error(email.getText());
        } else {
            try {
                javaMailSender.send(email);
            } catch (Exception e) {
                logger.error("Unable to send email! Future emails will be logged - no retry!", e);
                mailTest = true;
                logger.error(email.getText());
            }
        }
    }

    @PostConstruct
    public void checkMailConnection() {
        if (mailTest) {
            return;
        }
        new Thread(() -> {
            try {
                if (javaMailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
                    ((org.springframework.mail.javamail.JavaMailSenderImpl) javaMailSender).testConnection();
                }
            } catch (Exception e) {
                logger.warn("Mail server connection check failed on startup (Invalid credentials or SMTP server down). Mail sender will fall back to logging emails. Details: {}", e.getMessage());
                mailTest = true;
            }
        }).start();
    }
}