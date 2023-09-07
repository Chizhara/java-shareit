package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        log.trace("Called method toUserDto of class UserMapper with args: user = {};", user);
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static Collection<UserDto> toUserDto(Collection<User> userDtos) {
        log.trace("Called method toUserDto of class UserMapper with args: userDtos = {};", userDtos);
        return userDtos.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public static User toUser(UserDto userDto) {
        log.trace("Called method toUser of class UserMapper with args: userDto = {};", userDto);
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static Collection<User> toUser(Collection<UserDto> users) {
        log.trace("Called method toUser of class UserMapper with args: users = {};", users);
        return users.stream().map(UserMapper::toUser).collect(Collectors.toList());
    }
}
