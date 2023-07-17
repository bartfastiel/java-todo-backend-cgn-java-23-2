package de.neuefische.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Todo not found!")
public class NoSuchTodoException extends RuntimeException {

    public NoSuchTodoException() {
    }

    public NoSuchTodoException(String s, Throwable cause) {
        super(s, cause);
    }

    public NoSuchTodoException(Throwable cause) {
        super(cause);
    }

    public NoSuchTodoException(String s) {
        super(s);
    }
}
