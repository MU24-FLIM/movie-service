package com.example.Movie;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ServiceUnavailableException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler
    public String customBadRequestException(BadRequestException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler
    public String customEntityNotFoundException(EntityNotFoundException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @org.springframework.web.bind.annotation.ExceptionHandler
    public String customInternalServerError(ServiceUnavailableException e){
        return e.getMessage();
    }

    //Handle duplicate entries for example
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @org.springframework.web.bind.annotation.ExceptionHandler
    public String customInternalServerError(SQLIntegrityConstraintViolationException e){
        return e.getMessage();
    }

}
