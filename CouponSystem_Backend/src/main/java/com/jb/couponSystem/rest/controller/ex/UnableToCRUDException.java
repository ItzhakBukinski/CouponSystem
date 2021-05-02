package com.jb.couponSystem.rest.controller.ex;

/**
 * Thrown to indicate that there is a problem with a CRUD action that is being tried to get acted.
 */
public class UnableToCRUDException extends Exception {
    public UnableToCRUDException(String msg) {
        super(msg);
    }
}
