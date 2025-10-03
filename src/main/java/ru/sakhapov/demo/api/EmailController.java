package ru.sakhapov.demo.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sakhapov.demo.api.dto.UserEvent;
import ru.sakhapov.demo.store.kafka.KafkaProducer;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {

    KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public AckDto sendEmail(
            @RequestParam("email") String email,
            @RequestParam("operation") String operation) {

        UserEvent event = new UserEvent(operation, email);
        kafkaProducer.sendEvent(event);

        return AckDto.makeDefault(true);
    }
}
