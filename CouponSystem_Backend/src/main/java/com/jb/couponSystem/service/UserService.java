package com.jb.couponSystem.service;

import com.jb.couponSystem.data.entity.ClientType;
import com.jb.couponSystem.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {


    /**
     * Finds all the users in system - this act is forbidden for a company or a customer.
     *
     * @return a list of the users sorted by user names.
     */
    List<? extends User> getAllUsers();

    /**
     * Finds a single user by its id.
     *
     * @param id the id of the user.
     * @return an {@link Optional}. if found- contains a user, otherwise empty.
     */
    Optional<? extends User> getUserById(long id);

    /**
     * Creating a new user in the system. There is no way to create a new admin, just new company or customer.
     * A new admin can be added only manually to the database by the developers.
     * In case of company it cannot accept name or email that are already belongs to another user, and in case
     * of customer it cannot accept an email that are already belongs to another user.
     *
     * @param name     the name of the user.
     * @param email    the email of the user.
     * @param password the password of the user.
     * @param type     the {@link ClientType} of the user.
     * @return an {@link Optional}. if succeed- contains an user, otherwise empty.
     */
    Optional<? extends User> createUser(String name, String email, String password, ClientType type);

    /**
     * Updating a user in the system.
     * Here are the same laws as of creating new user (concerning the name and email),
     * except that an admin can also update itself.
     * Note: the id cannot be changed ,and is used to find the original user in system.
     *
     * @param user the user with the updated details.
     * @return an {@link Optional}. if succeed- contains the new user, otherwise empty.
     */
    Optional<? extends User> updateUser(User user);

    /**
     * Deleting a user from the system. If the user does not exists nothing will be deleted.
     * A company or a customer can only delete themself. An admin can delete every user except another admin.
     *
     * @param userId the id of user to delete.
     */
    void deleteUser(long userId);


}
