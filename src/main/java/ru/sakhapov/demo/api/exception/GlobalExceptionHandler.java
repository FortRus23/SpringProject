package ru.sakhapov.demo.api.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .error("Bad request")
                .errorDescription(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorDto);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUsernameNotFoundException(UserNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Email already exist");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(AgeMustBePositiveException.class)
    public ResponseEntity<ErrorDto> handleAgeMustBePositiveException(AgeMustBePositiveException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Age error");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }


}