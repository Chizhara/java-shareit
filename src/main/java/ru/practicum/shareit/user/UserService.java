package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return userRepository.findAll();
    }

    @Transactional
    public User addUser(User user) {
        log.info("Called method addUser of class UserService with args: user = {};", user);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user, long userId) {
        log.info("Called method updateUser of class UserService with args: userId = {}, user = {};", userId, user);
        User updatedUser = getUser(userId);
        updateUserFields(updatedUser, user);
        return userRepository.save(updatedUser);
    }

    @Transactional
    public User deleteUser(long userId) {
        log.info("Called method deleteUser of class UserService with args: userId = {};", userId);
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    public void validateUserExistById(long userId) {
        log.info("Called method validateUserById of class UserService with args: userId = {};", userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(userId);
        }
    }

    private void updateUserFields(User updatedUser, User userData) {
        log.trace("Called method updateUserFields of class UserService with args: " +
                "updatedUser = {}; userData = {};", updatedUser, userData);
        if (userData.getEmail() != null) {
            updatedUser.setEmail(userData.getEmail());
        }
        if (userData.getName() != null) {
            updatedUser.setName(userData.getName());
        }
    }
}
