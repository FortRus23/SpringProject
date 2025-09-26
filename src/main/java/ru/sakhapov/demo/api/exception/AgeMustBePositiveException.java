package ru.sakhapov.demo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgeMustBePositiveException extends RuntimeException{
    public AgeMustBePositiveException() {
        super("Age must be > 0");
    }
}
