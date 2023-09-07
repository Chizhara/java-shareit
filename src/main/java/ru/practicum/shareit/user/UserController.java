package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Called method getUsers of class UserController with args: ");
        return toUserDto(userService.getUsers());
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable @NotNull Integer userId) {
        log.info("Called method getUser of class UserController with args: userId = {};", userId);
        return toUserDto(userService.getUser(userId));
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Called method addUser of class UserController with args: userDto = {};", userDto);
        return toUserDto(userService.addUser(toUser(userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable @NotNull Integer userId,
                              @RequestBody UserDto userDto) {
        log.info("Called method updateUser of class UserController " +
                "with args: userId = {}; userDto = {};", userId, userDto);
        return toUserDto(userService.updateUser(toUser(userDto), userId));
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable @NotNull Integer userId) {
        log.info("Called method deleteUser of class UserController with args: userId = {};", userId);
        return toUserDto(userService.deleteUser(userId));
    }

}

