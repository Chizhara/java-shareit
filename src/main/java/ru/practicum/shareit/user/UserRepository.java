package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IntersectionValuesException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    private long sec = 0;

    public Optional<User> findById(long userId) {
        log.debug("Called method getAll of class UserRepository with args: userId = {};", userId);
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException(userId);
        }
        return Optional.of(user);
    }

    public Collection<User> getAll() {
        log.debug("Called method getAll of class UserRepository with args: ;");
        return users.values();
    }

    public Optional<User> save(User user) {
        log.debug("Called method save of class UserRepository with args: user = {};", user);
        user.setId(sec + 1);
        User userTemp = putUser(user);
        sec++;
        return Optional.of(userTemp);
    }

    public Optional<User> update(User user) {
        log.debug("Called method update of class UserService with args: user = {};", user);
        return Optional.of(updateUserFields(user));
    }


    public Optional<User> delete(long userId) {
        log.debug("Called method delete of class UserRepository with args: userId = {};", userId);
        return Optional.of(users.remove(userId));
    }

    private User putUser(User user) {
        log.debug("Called method putUser of class UserRepository with args: user = {};", user);
        validateUser(user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private User updateUserFields(User user) {
        log.trace("Called method updateUserFields of class UserRepository with args: user = {};", user);
        User userUpdated = users.get(user.getId());
        if (user.getEmail() != null) {
            validateUser(user);
            userUpdated.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userUpdated.setName(user.getName());
        }
        return userUpdated;
    }

    private void validateUser(User user) {
        log.trace("Called method validateUser of class UserRepository with args: user = {}", user);
        if (users.values().stream()
                .anyMatch(userTemp -> user.getEmail().equals(userTemp.getEmail())
                        && !userTemp.getId().equals(user.getId()))) {
            throw new IntersectionValuesException(user.getEmail());
        }
    }
}
