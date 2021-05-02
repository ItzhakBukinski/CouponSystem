package com.jb.couponSystem.rest;


import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * An helper to create a message for exceptions in system.
 * It contains the message of the error and a timestamp of the time of message.
 */
@Data
public class ClientErrorResponse {
    private final String message;
    private final LocalDateTime timeOfMessage;

    private ClientErrorResponse(String message, long timestamp) {
        this.message = message;
        timeOfMessage = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Creating a new ClientErrorResponse with the current time of the creation.
     *
     * @param message the message.
     * @return a new {@link ClientErrorResponse}.
     */
    public static ClientErrorResponse createErrorMessageOfNow(String message) {
        return new ClientErrorResponse(message, System.currentTimeMillis());
    }
}
