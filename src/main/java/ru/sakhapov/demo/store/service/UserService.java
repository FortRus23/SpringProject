package ru.sakhapov.demo.store.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.sakhapov.demo.api.dto.UserDto;
import ru.sakhapov.demo.api.dto.UserDtoFactory;
import ru.sakhapov.demo.api.exception.AgeMustBePositiveException;
import ru.sakhapov.demo.api.exception.EmailAlreadyExistsException;
import ru.sakhapov.demo.api.exception.UserNotFoundException;
import ru.sakhapov.demo.store.entity.User;
import ru.sakhapov.demo.store.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(UserDtoFactory::makeUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDtoFactory::makeUserDto)
                .toList();
    }

    public void createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException(userDto.getEmail());
        }

        if(userDto.getAge() < 0) {
            throw new AgeMustBePositiveException();
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .age(userDto.getAge())
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);
    }

    public void updateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userDto.getAge() < 0) {
            throw new AgeMustBePositiveException();
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new EmailAlreadyExistsException(userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getAge() > 0) {
            user.setAge(userDto.getAge());
        }

        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}
