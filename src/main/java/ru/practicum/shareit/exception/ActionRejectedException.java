package ru.practicum.shareit.exception;

public class ActionRejectedException extends RuntimeException {
    public ActionRejectedException(String msg) {
        super(msg);
    }
}
