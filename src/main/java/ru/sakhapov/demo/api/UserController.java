package ru.sakhapov.demo.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.demo.api.dto.UserDto;
import ru.sakhapov.demo.store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public AckDto createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return AckDto.makeDefault(true);
    }

    @PatchMapping("/{id}")
    public AckDto updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        userService.updateUserById(id, userDto);
        return AckDto.makeDefault(true);
    }

    @DeleteMapping("/{id}")
    public AckDto deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return AckDto.makeDefault(true);
    }
}
