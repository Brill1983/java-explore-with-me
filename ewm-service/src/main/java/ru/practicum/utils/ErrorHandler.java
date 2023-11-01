package ru.practicum.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.exceptions.BadParameterException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ElementNotFoundException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleMethodArgumentNotValidExc(MethodArgumentNotValidException e) {

        String errors = getErrors(e);

        String status = HttpStatus.BAD_REQUEST.name();
        String parameterName = e.getParameter().getParameter().getName();
        String className = e.getParameter().getContainingClass().getName();
        String methodName = e.getParameter().getMethod().getName();

        String reason = "В метод: " + methodName + ", класса: " + className + ", в параметр: " + parameterName +
                ", передан неправильный аргумент";

        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleMethodArgumentTypeMismatchExc(MethodArgumentTypeMismatchException e) {

        String errors = getErrors(e);

        String status = HttpStatus.BAD_REQUEST.name();
        String parameterName = e.getParameter().getParameter().getName();
        String className = e.getParameter().getContainingClass().getName();
        String methodName = e.getParameter().getMethod().getName();

        String reason = "В метод: " + methodName + ", класса: " + className + ", в параметр: " + parameterName +
                ", передан неправильный аргумент";

        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public AppiError handleConstraintViolationExc(ConstraintViolationException e) {

        String errors = getErrors(e);
        String status = HttpStatus.CONFLICT.name();
        String reason = "Нарушение целостности данных";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleMissingArgumentExc(MissingServletRequestParameterException e) {

        String errors = getErrors(e);
        String status = HttpStatus.BAD_REQUEST.name();
        String reason = "Отсутствует аргумент параметра пути";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppiError handleOtherExc(Throwable e) {
        log.info("Error: ", e);
        String errors = getErrors(e);
        String reason = "Unexpected error";
        return new AppiError(errors, e.getMessage(), reason, HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppiError handleNotFoundExc(ElementNotFoundException e) {

        String errors = getErrors(e);
        String reason = "The required object was not found.";
        log.info("Element not found: {}", e.getMessage());
        return new AppiError(errors, e.getMessage(), reason, HttpStatus.NOT_FOUND.name(), LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public AppiError handleConflictExistExc(RuntimeException e) {

        String errors = getErrors(e);
        String status = HttpStatus.CONFLICT.name();
        String reason = "Конфликт запроса и базы данных";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppiError handleBadParameterExc(BadParameterException e) {

        String errors = getErrors(e);
        String status = HttpStatus.BAD_REQUEST.name();
        String reason = "Передан неправильный параметр запроса";
        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new AppiError(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    private String getErrors(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
