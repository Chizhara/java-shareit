package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Called method getUsers of class UserController with args: ");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @NotNull Integer userId) {
        log.info("Called method getUser of class UserController with args: userId = {};", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequestDto userDto) {
        log.info("Called method addUser of class UserController with args: userDto = {};", userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @NotNull Integer userId,
                                     @RequestBody UserRequestDto userDto) {
        log.info("Called method updateUser of class UserController " +
                "with args: userId = {}; userDto = {};", userId, userDto);
        return userClient.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @NotNull Integer userId) {
        log.info("Called method deleteUser of class UserController with args: userId = {};", userId);
        return userClient.deleteUser(userId);
    }

}

