package de.neuefische.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBindExceptionExceptions(MethodArgumentNotValidException exception) {
        return new ErrorMessage(exception.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementExceptions(NoSuchElementException exception) {
        return new ErrorMessage(exception.getMessage() + "GLOBAL");
    }
}
