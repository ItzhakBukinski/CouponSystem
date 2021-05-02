package com.jb.couponSystem.rest.controller;

import com.jb.couponSystem.rest.ClientErrorResponse;
import com.jb.couponSystem.rest.controller.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A controller for exception handling.
 */
@RestControllerAdvice
public class ClientControllerAdvice {
    /**
     * Handler for unauthorized logins.
     *
     * @param e the exception.
     * @return a {@link ClientErrorResponse} with appropriated message.
     */
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ClientErrorResponse handleUnauthorizedLogin(InvalidLoginException e) {
        return ClientErrorResponse.createErrorMessageOfNow(e.getMessage());
    }

    /**
     * Handler for CRUD exceptions.
     *
     * @param e the exception.
     * @return a {@link ClientErrorResponse} with appropriated message.
     */
    @ExceptionHandler(UnableToCRUDException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ClientErrorResponse handleNoContent(Exception e) {
        return ClientErrorResponse.createErrorMessageOfNow(e.getMessage());
    }
}
