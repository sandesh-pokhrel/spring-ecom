package com.kavka.apiservices.exception;

import com.kavka.apiservices.common.Status;
import com.kavka.apiservices.filter.InvalidJWTTokenException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExHandler {

    private final Status status;

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Status> exceptionForHttpClientError(HttpClientErrorException ex) {
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(status);
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> userExistsException(UserExistsException ex) {
        status.setExMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Status> userNotFoundException(UserNotFoundException ex) {
        ex.printStackTrace();
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Status> genericNotFoundException(NotFoundException ex) {
        ex.printStackTrace();
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> invalidOperationException(InvalidOperationException ex) {
        ex.printStackTrace();
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
    }

    @ExceptionHandler(InvalidUserCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Status> invalidUserCredentialException(InvalidUserCredentialException ex) {
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(status);
    }

    @ExceptionHandler(InvalidJWTTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> invalidJwtTokenException(InvalidJWTTokenException ex) {
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        ex.printStackTrace();
        status.setExMessage("Database constriant violated. Please check the log.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        StringBuilder exceptionMessageBuilder = new StringBuilder(Strings.EMPTY);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (fieldErrors.size() > 0) exceptionMessageBuilder.append(fieldErrors.get(0).getDefaultMessage());
        else exceptionMessageBuilder.append(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        //fieldErrors.forEach(fieldError -> exceptionMessageBuilder.append(fieldError.getDefaultMessage()));
        status.setExMessage(exceptionMessageBuilder.toString());
        return ResponseEntity.badRequest().body(status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Status> methodArgumentNotValidException(ConstraintViolationException ex) {
        ex.printStackTrace();
        StringBuilder exceptionMessageBuilder = new StringBuilder(Strings.EMPTY);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        exceptionMessageBuilder.append(((ConstraintViolationImpl<?>)Arrays.asList(constraintViolations.toArray()).get(0)).getMessage());
        //fieldErrors.forEach(fieldError -> exceptionMessageBuilder.append(fieldError.getDefaultMessage()));
        status.setExMessage(exceptionMessageBuilder.toString());
        return ResponseEntity.badRequest().body(status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Status> globalExceptionThrown(Exception ex) {
        ex.printStackTrace();
        status.setExMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
    }
}
