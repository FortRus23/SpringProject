package ru.sakhapov.demo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserDto {

    Long id;

    String name;

    String email;

    int age;

    @JsonProperty("created_at")
    Instant createdAt;
}
