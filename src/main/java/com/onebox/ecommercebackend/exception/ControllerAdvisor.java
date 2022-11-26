package com.onebox.ecommercebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(CartNorFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleCartNotFound(CartNorFoundException ex) {

        //Right now we return a 404 with an empty body and log the error, but this can be changed here in the future
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleHttpMessageNotReadableRequest(HttpMessageNotReadableException ex) {

        //Return 400 with empty body and log the error
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleBadArgumentRequest(MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<FieldError> fieldErrors) {

        Error error = new Error(BAD_REQUEST.value(), "Validation error");
        fieldErrors.forEach(fieldError -> error.addFieldError(fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getDefaultMessage()));
        return error;
    }

    static class Error {
        private final int status;
        private final String message;
        private final List<FieldError> fieldErrors = new ArrayList<>();

        Error(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public void addFieldError(String objectName, String path, String message) {
            FieldError error = new FieldError(objectName, path, message);
            fieldErrors.add(error);
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }
}
