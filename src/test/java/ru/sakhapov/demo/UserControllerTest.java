package ru.sakhapov.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.sakhapov.demo.api.UserController;
import ru.sakhapov.demo.api.dto.UserDto;
import ru.sakhapov.demo.api.exception.UserNotFoundException;
import ru.sakhapov.demo.store.service.UserService;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final Instant INSTANT = Instant.parse("2025-09-28T10:00:00Z");

    @Test
    void shouldReturnUserDto_whenUserExists() throws Exception {
        UserDto userDto = new UserDto(1L, "Test", "test@example.com", 25, INSTANT);
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.created_at").value(INSTANT.toString()));

    }

    @Test
    void shouldReturnNotFoundError_whenUserNotExist() throws Exception {
        when(userService.getUserById(99L))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"))
                .andExpect(jsonPath("$.error_description")
                        .value("Account with this id '99' not found."));
    }

    @Test
    void getUsers_shouldReturnList() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "Test", "test@mail.com", 25, INSTANT),
                new UserDto(2L, "John", "doe@mail.com", 25, INSTANT)
        );
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test"))
                .andExpect(jsonPath("$[0].created_at").value(INSTANT.toString()))
                .andExpect(jsonPath("$[1].name").value("John"))
                .andExpect(jsonPath("$[1].created_at").value(INSTANT.toString()));
    }

    @Test
    void shouldReturnTrue_whenUserIsCreated() throws Exception {
        UserDto request = new UserDto(null, "Test", "test@mail.com", 25, null);
        doNothing().when(userService).createUser(any(UserDto.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(true));
    }

    @Test
    void shouldReturnTrue_whenUserIsUpdated() throws Exception {
        UserDto request = new UserDto(null, "Updated", "updated@mail.com", 25, null);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(true));
    }

    @Test
    void shouldReturnTrue_whenUserIsDeleted() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(true));
    }

}
