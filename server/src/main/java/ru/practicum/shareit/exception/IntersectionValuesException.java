package ru.practicum.shareit.exception;

public class IntersectionValuesException extends RuntimeException {
    public IntersectionValuesException(Object value) {
        super("Founded intersection of value " + value);
    }

}
