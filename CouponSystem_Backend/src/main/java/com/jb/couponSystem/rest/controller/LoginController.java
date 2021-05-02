package com.jb.couponSystem.rest.controller;

import com.google.gson.Gson;
import com.jb.couponSystem.data.entity.ClientType;
import com.jb.couponSystem.rest.ClientSession;
import com.jb.couponSystem.rest.LoginSystem;
import com.jb.couponSystem.rest.controller.ex.InvalidLoginException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * A controller for all the login actions.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    /**
     * The length of the token.
     */
    private static final int LENGTH_TOKEN = 15;
    /**
     * The max time for deleting inactive sessions in milliseconds.
     */
    public static final long MAX_TIME_FOR_DELETING_SESSION_IN_MILLIS = 1_800_000;

    private final LoginSystem loginSystem;
    private final Map<String, ClientSession> tokenMap;
    private static final Gson GSON = new Gson();

    /**
     * Login a client into the system, creating a {@link ClientSession} with the details of the client,
     * and a random token, and putting them together in the HashMap of tokens.
     *
     * @param email    the email of the client.
     * @param password the password of the client.
     * @param type     the {@link ClientType} of the client.
     * @return a {@link ResponseEntity} with the token that created for use for future actions in system in this session.
     * @throws InvalidLoginException if there is mismatch with the provided credentials.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password, @RequestParam ClientType type)
            throws InvalidLoginException {

        ClientSession session = loginSystem.createSession(email, password, type);
        String token = generateToken();
        tokenMap.put(token, session);
        return ResponseEntity.ok(GSON.toJson(token));

    }

    /**
     * Logout the system.
     *
     * @param token the token of the current session to logout.
     */
    @DeleteMapping("/logout")
    public void logout(@RequestParam String token) {
        tokenMap.remove(token);
    }

    /**
     * A scheduled task that run in fixed rate to delete all inactive sessions.
     */
    @Scheduled(fixedRate = MAX_TIME_FOR_DELETING_SESSION_IN_MILLIS)
    public void logoutIfNoAction() {
        Iterator<Map.Entry<String, ClientSession>> iterator = tokenMap.entrySet().iterator();
        while (iterator.hasNext()) {

            ClientSession value = iterator.next().getValue();
            if (System.currentTimeMillis() - value.getLastAccessedMillis() >= MAX_TIME_FOR_DELETING_SESSION_IN_MILLIS)
                iterator.remove();
        }
    }

    /**
     * Generating a new random token.
     *
     * @return the token.
     */
    private String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, LENGTH_TOKEN);
    }
}