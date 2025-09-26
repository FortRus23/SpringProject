package ru.sakhapov.demo.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
