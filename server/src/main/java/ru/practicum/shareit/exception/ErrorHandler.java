package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIntersectionValuesException(final IntersectionValuesException e) {
        log.error("ERROR Intersections conflict 409! {}", e.getMessage());
        return new ErrorResponse("Ошибка пересечений: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("ERROR Not Found 404! {}", e.getMessage());
        return new ErrorResponse("Ошибка не найденного объекта: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(final ActionRejectedException e) {
        log.error("ERROR Not available 400! {}", e.getMessage());
        return new ErrorResponse("Действие отклонено: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.error("ERROR Unhandled Error 500! {}", e.getClass());
        Arrays.stream(e.getStackTrace()).forEach(System.out::println);
        return new ErrorResponse("Непредвиденная ошибка: " + e.getClass());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("ERROR DataIntegrityViolationException 409! {}", e.getMessage());
        Arrays.stream(e.getStackTrace()).forEach(System.out::println);
        return new ErrorResponse("Конфликт с базой данных: " + e.getMessage());
    }

}
