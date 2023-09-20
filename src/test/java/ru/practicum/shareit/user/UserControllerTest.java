package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    UserService userService;

    private UserDto userDtoTestData;
    private UserDto userDtoInvalidTestData;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private User userTestData;
    private List<User> usersTestData;

    @BeforeEach
    public void init() {

        userTestData = TestData.getUsers()[0];
        usersTestData = List.of(TestData.getUsers());

        userDtoTestData = UserDto.builder()
                .id(1L)
                .name("User A")
                .email("mail@mail.ru")
                .build();
        userDtoInvalidTestData = UserDto.builder()
                .id(1L)
                .name(" ")
                .email(" ")
                .build();

    }

    @Test
    public void shouldSaveNewUser() throws Exception {
        when(userService.addUser(any()))
                .thenReturn(userTestData);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoTestData.getName())))
                .andExpect(jsonPath("$.email", is(userDtoTestData.getEmail())));
    }

    @Test
    public void shouldReturn400WhenSaveNewInvalidUser() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoInvalidTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        when(userService.updateUser(any(), anyLong()))
                .thenReturn(userTestData);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoTestData.getName())))
                .andExpect(jsonPath("$.email", is(userDtoTestData.getEmail())));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        when(userService.deleteUser(1L))
                .thenReturn(userTestData);

        mvc.perform(delete("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoTestData.getName())))
                .andExpect(jsonPath("$.email", is(userDtoTestData.getEmail())));
    }

    @Test
    public void shouldGetUser() throws Exception {
        when(userService.getUser(1L))
                .thenReturn(userTestData);

        mvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoTestData.getName())))
                .andExpect(jsonPath("$.email", is(userDtoTestData.getEmail())));
    }

    @Test
    public void shouldReturn404WhenGetUser() throws Exception {
        when(userService.getUser(1L))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/users/1")
                        .accept(MediaType.ALL))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetUsers() throws Exception {
        when(userService.getUsers())
                .thenReturn(usersTestData);

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(usersTestData.size())))
                .andExpect(jsonPath("$.[0].id", is(userDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDtoTestData.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDtoTestData.getEmail())));
    }

}
