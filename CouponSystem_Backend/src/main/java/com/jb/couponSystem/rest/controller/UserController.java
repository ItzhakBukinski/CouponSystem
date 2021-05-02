package com.jb.couponSystem.rest.controller;

import com.jb.couponSystem.data.entity.ClientType;
import com.jb.couponSystem.data.entity.User;
import com.jb.couponSystem.rest.ClientSession;
import com.jb.couponSystem.rest.controller.ex.*;
import com.jb.couponSystem.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jb.couponSystem.data.entity.ClientType.COMPANY;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class UserController {
    private final UserService service;
    private final Map<String, ClientSession> tokensMap;

    /**
     * Creating a new user to the system.
     *
     * @param type     the type of the user.
     * @param name     the name of the user.
     * @param email    the email of the user.
     * @param password the password of the user.
     * @return a {@link ResponseEntity} with the new user.
     * @throws UnableToCRUDException if fails to create the user.
     */
    @PostMapping("/create")
    public ResponseEntity<? extends User> createUser(@RequestParam ClientType type,
                                                     @RequestParam String name,
                                                     @RequestParam String email,
                                                     @RequestParam String password)
            throws UnableToCRUDException {
        Optional<? extends User> optCompany = service.createUser(name, email, password, type);

        if (optCompany.isPresent())
            return ResponseEntity.ok(optCompany.get());
        throw new UnableToCRUDException(
                type == COMPANY ?
                        "Cannot create new company because the name or email are already exists! please try another details!" :
                        "Cannot create new customer because email is already exists! please try another details!"
        );
    }

    /**
     * Getting all users in system.
     *
     * @param token the token of the current session of the admin.
     * @return a {@link ResponseEntity} with a list of the users.
     * @throws InvalidLoginException when the {@link ClientSession} of the admin and its token are no longer valid.
     */
    @GetMapping("/get-users")
    public ResponseEntity<List<? extends User>> getUsers(@RequestParam String token)
            throws InvalidLoginException {
        beginSession(token);
        List<? extends User> users = service.getAllUsers();

        return ResponseEntity.ok(users);
    }

    /**
     * Getting the user that holds a session.
     *
     * @param token the token which is the key for the session.
     * @return a {@link ResponseEntity} with the user.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @GetMapping("get-my-user")
    public ResponseEntity<? extends User> getMyUser(@RequestParam String token) throws InvalidLoginException {
        long userId = beginSession(token);
        Optional<? extends User> optionalUser = service.getUserById(userId);

        return ResponseEntity.ok(optionalUser.orElseThrow());
    }

    /**
     * Updating a user.
     *
     * @param token the token of the current session of the user.
     * @param user  the updated user.
     * @return a {@link ResponseEntity} with the updated user.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     * @throws UnableToCRUDException if fails to update the user.
     */
    @PatchMapping("/update")
    public ResponseEntity<? extends User> updateUser(@RequestParam String token, @RequestBody User user)
            throws InvalidLoginException, UnableToCRUDException {
        beginSession(token);
        Optional<? extends User> optUser = service.updateUser(user);

        if (optUser.isPresent())
            return ResponseEntity.ok(optUser.get());
        throw new UnableToCRUDException("Cannot update user either because it is not exists " +
                "or because the new details belongs to another existing user");
    }

    /**
     * Deleting a user.
     *
     * @param token  the token of the current session of the user.
     * @param userId the id of the user.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam String token, @RequestParam long userId)
            throws InvalidLoginException {
        beginSession(token);
        service.deleteUser(userId);
    }

    /**
     * Beginning the process of a session for a user, by getting his {@link ClientSession}
     * that linked to the his token and updating the last time access.
     *
     * @param token the token of the current session of the user.
     * @return the id of the user that holds the given token.
     * @throws InvalidLoginException when the {@link ClientSession} of the user and its token are no longer valid.
     */
    private long beginSession(String token) throws InvalidLoginException {
        ClientSession clientSession = tokensMap.get(token);

        if (clientSession == null)
            throw new InvalidLoginException("Your login time had expired! please log in again");

        clientSession.access();
        return clientSession.getClientId();
    }
}
