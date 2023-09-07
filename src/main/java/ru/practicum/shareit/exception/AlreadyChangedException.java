package ru.practicum.shareit.exception;

public class AlreadyChangedException extends ActionRejectedException {
    public AlreadyChangedException(Object value) {
        super("Status already changed " + value);
    }
}
