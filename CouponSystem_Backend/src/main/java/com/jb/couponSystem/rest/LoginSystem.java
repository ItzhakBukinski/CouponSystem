package com.jb.couponSystem.rest;

import com.jb.couponSystem.data.entity.ClientType;
import com.jb.couponSystem.data.entity.User;
import com.jb.couponSystem.data.repository.UserRepository;
import com.jb.couponSystem.rest.controller.ex.InvalidLoginException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * An helper for the LoginController for login into the system for all clients.
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class LoginSystem {
    private final UserRepository userRepo;


    /**
     * Creating a new {@link ClientSession} for a client.
     *
     * @param email    the email of the client as in DataBase.
     * @param password the password of the client as in DataBase.
     * @param type     the {@link ClientType} of client.
     * @return a new {@link ClientSession} with the id of the client and his {@link ClientType}.
     * @throws InvalidLoginException if there is mismatch with the provided credentials.
     */
    public ClientSession createSession(String email, String password, ClientType type) throws InvalidLoginException {
        Optional<? extends User> optUser = userRepo.findByEmailAndPassword(email, password);

        if (optUser.isPresent() && optUser.get().getType() == type)
            return ClientSession.create(optUser.get().getId());
        throw new InvalidLoginException("Unable to login with provided credentials");
    }
}