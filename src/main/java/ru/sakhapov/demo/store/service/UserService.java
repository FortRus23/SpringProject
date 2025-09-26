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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserDtoFactory.makeUserDto(user);
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(UserDtoFactory::makeUserDto).toList();
    }

    public void createUser(UserDto userDto) {
        if (userDto.getEmail() != null) {
            if(userRepository.existsByEmail(userDto.getEmail())){
                throw new EmailAlreadyExistsException(userDto.getEmail());
            }
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

        if (userDto.getEmail() != null) {
            if(userRepository.existsByEmail(userDto.getEmail())){
                throw new EmailAlreadyExistsException("Email already exists");
            } else if (userDto.getAge() < 0) {
                throw new AgeMustBePositiveException();
            }
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());

        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
