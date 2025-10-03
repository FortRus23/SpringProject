package ru.sakhapov.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.sakhapov.demo.api.dto.UserEvent;
import ru.sakhapov.demo.store.kafka.KafkaProducer;

import java.util.Objects;

import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "email_topic" })
@EnableKafka
public class EmailServiceTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void whenUserCreate_thenEmailSent() throws InterruptedException {
        UserEvent event = new UserEvent("CREATE", "test@mail.com");

        kafkaProducer.sendEvent(event);
        Thread.sleep(2000);

        verify(javaMailSender).send(org.mockito.ArgumentMatchers.argThat(
                (SimpleMailMessage mail) ->
                {
                    Assertions.assertNotNull(mail.getTo());
                    if (!mail.getTo()[0].equals("test@mail.com") ||
                            !Objects.equals(mail.getSubject(), "Уведомление")) return false;
                    Assertions.assertNotNull(mail.getText());
                    return mail.getText().contains("Ваш аккаунт был успешно создан");
                }
        ));
    }

    @Test
    void whenUserDelete_thenEmailSent() throws InterruptedException {
        UserEvent event = new UserEvent("DELETE", "delete@mail.com");

        kafkaProducer.sendEvent(event);
        Thread.sleep(2000);

        verify(javaMailSender).send(org.mockito.ArgumentMatchers.argThat(
                (SimpleMailMessage mail) ->
                {
                    Assertions.assertNotNull(mail.getTo());
                    if (!mail.getTo()[0].equals("delete@mail.com")) return false;
                    Assertions.assertNotNull(mail.getSubject());
                    if (!mail.getSubject().equals("Уведомление")) return false;
                    Assertions.assertNotNull(mail.getText());
                    return mail.getText().contains("Ваш аккаунт был удалён");
                }
        ));
    }
}
