package com.jb.couponSystem.rest;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * An object which contains the details of a client during a session.
 * The details are his id and the last time he did some action in the system.
 * This object with a token that is being created in the login process are saved in a HashMap contains all the current
 * sessions in system.
 * The time of last action is saved in order to have the ability to delete this session from the HashMap
 * if no action was took in a stable period of time.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class ClientSession {
    private final long clientId;
    private long lastAccessedMillis;


    /**
     * Creating a new session with the current time of the creation.
     *
     * @param clientId the id of client as in DataBase.
     * @return a new {@link ClientSession}.
     */
    public static ClientSession create(long clientId) {
        return new ClientSession(clientId, System.currentTimeMillis());
    }

    /**
     * Invoked every time when doing an action during a session,
     * in order to update the last time detail to the current time of action.
     */
    public void access() {
        lastAccessedMillis = System.currentTimeMillis();
    }


}

