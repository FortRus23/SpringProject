package ru.sakhapov.demo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserDto {

    @Schema(accessMode = READ_ONLY)
    Long id;

    String name;

    String email;

    int age;

    @Schema(accessMode = READ_ONLY)
    @JsonProperty("created_at")
    Instant createdAt;
}
