package ru.sakhapov.demo.store.kafka;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class KafkaProducer {

    private static final String TOPIC = "email_topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String operation, String email) {
        String event = String.format("{\"operation\":\"%s\",\"email\":\"%s\"}", operation, email);
        kafkaTemplate.send(TOPIC, event);
    }
}
