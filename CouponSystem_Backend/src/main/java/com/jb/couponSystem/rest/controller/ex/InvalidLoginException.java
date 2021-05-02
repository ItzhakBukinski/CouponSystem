package com.jb.couponSystem.rest.controller.ex;

/**
 * Thrown to indicate that the login credentials of the client who is trying to get into the system are wrong,
 * or the token and the {@link com.jb.couponSystem.rest.ClientSession} are no longer valid.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
