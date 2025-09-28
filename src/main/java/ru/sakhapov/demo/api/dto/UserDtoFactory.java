package ru.sakhapov.demo.api.dto;

import ru.sakhapov.demo.store.entity.User;

public class UserDtoFactory {

    public static UserDto makeUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
