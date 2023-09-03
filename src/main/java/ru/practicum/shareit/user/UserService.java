package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(long userId) {
        log.info("Called method getUser of class UserService with args: userId = {};", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public Collection<User> getUsers() {
        log.info("Called method getUsers of class UserService with args: ;");
        return userRepository.getAll();
    }

    public User addUser(User user) {
        log.info("Called method addUser of class UserService with args: user = {};", user);
        return userRepository.save(user)
                .orElseThrow(() -> new NotFoundException(user));
    }

    public User updateUser(User user, long userId) {
        log.info("Called method updateUser of class UserService with args: userId = {}, user = {};", userId, user);
        user.setId(userId);
        return userRepository.update(user)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public User deleteUser(long userId) {
        log.info("Called method deleteUser of class UserService with args: userId = {};", userId);
        return userRepository.delete(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

}
