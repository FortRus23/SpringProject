package ru.sakhapov.demo.store.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic emailTopic() {
        return new NewTopic("email_topic", 1, (short) 1);
    }
}