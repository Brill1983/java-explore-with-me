package ru.practicum.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.ElementNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_FORMAT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleBadParameterExc(BadParameterException e) {
//        log.info("Validation: {}", e.getMessage());
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidExc(MethodArgumentNotValidException e) {

        String errors = getErrors(e);

        String status = HttpStatus.BAD_REQUEST.name();
        String parameterName = e.getParameter().getParameter().getName();
        String className = e.getParameter().getContainingClass().getName();
        String methodName = e.getParameter().getMethod().getName();

        String reason = "В метод: " + methodName + ", класса: " + className + ", в параметр: " + parameterName +
                ", передан неправильный аргумент";

        log.info("Validation message: {}, status: {}, response: {}", e.getMessage(), status, reason);
        return new ErrorResponse(errors, e.getMessage(), reason, status, LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExc(Throwable e) {
        log.info("Error: ", e);
        String errors = getErrors(e);
        String reason = "Unexpected error";
        return new ErrorResponse(errors, e.getMessage(), reason, HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExc(ElementNotFoundException e) {

        String errors = getErrors(e);
        String reason = "The required object was not found.";

        log.info("Element not found: {}", e.getMessage());
        return new ErrorResponse(errors, e.getMessage(), reason, HttpStatus.NOT_FOUND.name(), LocalDateTime.now().format(DATE_FORMAT));
    }

    private String getErrors(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
