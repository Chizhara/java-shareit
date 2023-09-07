package ru.practicum.shareit.exception;

public class NotAvailableException extends ActionRejectedException {
    public NotAvailableException(Object value) {
        super("Not available " + value);
    }
}
