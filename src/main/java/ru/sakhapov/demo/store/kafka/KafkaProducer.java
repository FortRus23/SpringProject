package ru.sakhapov.demo.store.kafka;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import ru.sakhapov.demo.store.entity.UserEvent;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class KafkaProducer {

    private static final String TOPIC = "email_topic";

    KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendEvent(UserEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
