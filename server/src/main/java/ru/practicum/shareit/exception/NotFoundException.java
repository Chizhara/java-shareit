package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Object value) {
        super("Not found searched object by value: " + value);
    }
}
