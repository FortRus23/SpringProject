package ru.sakhapov.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.demo.api.dto.UserDto;
import ru.sakhapov.demo.api.exception.ErrorDto;
import ru.sakhapov.demo.store.entity.User;
import ru.sakhapov.demo.store.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static final Instant INSTANT = Instant.parse("2025-09-28T10:00:00Z");

    @Test
    void shouldReturnUserDto_whenUserExists() throws Exception {

        User user = new User(null, "Test", "test@example.com", 25, INSTANT);
        user = userRepository.save(user);

        MvcResult res = mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn();

        UserDto dto = objectMapper.readValue(res.getResponse().getContentAsString(), UserDto.class);

        assertEquals(user.getId(), dto.getId());
        assertEquals("Test", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(25, dto.getAge());
        assertEquals(INSTANT, dto.getCreatedAt());

    }

    @Test
    void shouldReturnNotFoundError_whenUserNotExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorDto error = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorDto.class
        );

        assertEquals("User not found", error.getError());
        assertEquals("Account with this id '999' not found.", error.getErrorDescription());
    }

    @Test
    void getUsers_shouldReturnList() throws Exception {
        userRepository.save(new User(null, "Test", "test@mail.com", 25, INSTANT));
        userRepository.save(new User(null, "John", "doe@mail.com", 25, INSTANT));

        MvcResult res = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDto> users = objectMapper.readValue(
                res.getResponse().getContentAsString(),
                new TypeReference<List<UserDto>>() {}
        );

        assertEquals(2, users.size());
        assertEquals("Test", users.get(0).getName());
        assertEquals(INSTANT, users.get(0).getCreatedAt());
        assertEquals("John", users.get(1).getName());
        assertEquals(INSTANT, users.get(1).getCreatedAt());
    }

    @Test
    void shouldReturnTrue_whenUserIsCreated() throws Exception {
        UserDto request = new UserDto(null, "Test", "test@mail.com", 25, null);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Boolean> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertTrue(response.get("answer"));

        assertEquals(1, userRepository.count());
        User saved = userRepository.findAll().get(0);
        assertEquals("Test", saved.getName());
        assertEquals("test@mail.com", saved.getEmail());
    }

    @Test
    void shouldReturnTrue_whenUserIsUpdated() throws Exception {
        User user = userRepository.save(new User(null, "Test", "test@mail.com", 25, INSTANT));

        UserDto request = new UserDto(null, "Updated", "updated@mail.com", 25, null);

        MvcResult result = mockMvc.perform(patch("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Boolean> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertTrue(response.get("answer"));

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("Updated", updated.getName());
        assertEquals("updated@mail.com", updated.getEmail());
    }

    @Test
    void shouldReturnTrue_whenUserDeleted() throws Exception {
        User user = userRepository.save(new User(null, "Delete", "delete@mail.com", 25, INSTANT));

        MvcResult result = mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Boolean> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertTrue(response.get("answer"));

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }


}
