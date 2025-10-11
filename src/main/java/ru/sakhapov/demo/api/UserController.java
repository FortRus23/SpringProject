package ru.sakhapov.demo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.demo.api.dto.UserDto;
import ru.sakhapov.demo.store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User API", description = "Операции по управлению юзерами")
public class UserController {

    UserService userService;

    @Operation(summary = "Получить юзера по id", description = "Возвращает данные юзера для указанного id")
    @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        EntityModel<UserDto> model = EntityModel.of(userDto);

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(id))
                .withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers())
                .withRel("all-users"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Получить всех юзеров")
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getUsers() {
        List<EntityModel<UserDto>> users = userService.getUsers().stream()
                .map(user -> EntityModel.of(user,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserById(user.getId())).withSelfRel()))
                .toList();

        return CollectionModel.of(users,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers())
                        .withSelfRel());
    }

    @Operation(summary = "Создать нового юзера")
    @PostMapping
    public AckDto createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return AckDto.makeDefault(true);
    }

    @Operation(summary = "Обновить юзера по id")
    @PatchMapping("/{id}")
    public AckDto updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        userService.updateUserById(id, userDto);
        return AckDto.makeDefault(true);
    }

    @Operation(summary = "Удалить юзера по id")
    @DeleteMapping("/{id}")
    public AckDto deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return AckDto.makeDefault(true);
    }
}
