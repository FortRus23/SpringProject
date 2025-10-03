package ru.sakhapov.demo.store.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.sakhapov.demo.api.dto.UserEvent;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        javaMailSender.send(mail);
    }

    @KafkaListener(topics = "email_topic", groupId = "notification-service")
    public void handleUserEvent(UserEvent event) {
        String message;
        if ("CREATE".equals(event.getOperation())) {
            message = "Здравствуйте! Ваш аккаунт был успешно создан.";
        } else if ("DELETE".equals(event.getOperation())) {
            message = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return;
        }

        sendEmail(event.getEmail(), "Уведомление", message);
    }
}
