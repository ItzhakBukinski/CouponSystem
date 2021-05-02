package com.jb.couponSystem.service;

import com.jb.couponSystem.data.entity.*;
import com.jb.couponSystem.data.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Autowired))
public class UserServiceProvider implements UserService {
    private final UserRepository repository;

    @Override
    public List<? extends User> getAllUsers() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends User> getUserById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<? extends User> createUser(String name, String email, String password, ClientType type) {
        switch (type) {
            case COMPANY:
                Company company = new Company(name, email, password);
                return isCompanyExists(company) ?
                        Optional.empty()
                        :
                        Optional.of(repository.save(company));

            case CUSTOMER:
                Customer customer = new Customer(name, email, password);
                return isCustomerOrAdminExists(customer) ?
                        Optional.empty()
                        :
                        Optional.of(repository.save(customer));
            default:
                return Optional.empty();

        }
    }

    @Override
    public Optional<? extends User> updateUser(User user) {
        /*In case of company*/
        if (user.getType() == ClientType.COMPANY) {
            Company company = (Company) user;

            return repository.findById(company.getId())
                    .filter(optionalCompany -> !isCompanyExists(company))
                    .stream()
                    .peek(originalCompany -> {
                        if (originalCompany != null) {
                            /*Set the coupons of original company to new one*/
                            company.setCoupons(((Company) originalCompany).getCoupons());
                            repository.save(company);
                        }
                    })
                    .findAny();
        }

        /*In case of customer or admin*/
        return repository.findById(user.getId())
                .filter(optionalCustomer -> !isCustomerOrAdminExists(user))
                .stream()
                .peek(originalUser -> {
                    if (originalUser != null) {
                        /*In case of customer set the coupons of original customer to new one*/
                        if (user instanceof Customer)
                            ((Customer) user).setCoupons(((Customer) originalUser).getCoupons());
                        repository.save(user);
                    }
                })
                .findAny();
    }

    @Override
    public void deleteUser(long userId) {

        repository.findById(userId).ifPresent(repository::delete);
    }

    /**
     * Checking if a company's details exist when creating a new company or updating one.
     * That is performed by comparing its details to all others users in system
     * (except this company itself in case of updating).
     * Exist company means that the name or the email are already exists in system for another user/s.
     * Note: in the comparing process all whiteSpaces are removed from both compared details.
     *
     * @param company the company that is being checked.
     * @return true if found another user in system with one or more of the details, otherwise false.
     */
    private boolean isCompanyExists(Company company) {
        return getAllUsers().stream()
                /*ignoring the company that we are checking in case of updating*/
                .filter(c -> c.getId() != company.getId())
                .anyMatch(c ->
                        c.getName().replaceAll("\\s+", "")
                                .equals(company.getName().replaceAll("\\s+", ""))
                                || c.getEmail().replaceAll("\\s+", "")
                                .equals(company.getEmail().replaceAll("\\s+", "")));
    }

    /**
     * Checking if a customer or admin details exists when creating a new such user or updating one.
     * That is performed by comparing its details to all others users in system
     * (except this user itself in case of updating).
     * Exist admin or customer means that the email is already exists in system for another user.
     * Note: in the comparing process all whiteSpaces are removed from both compared details.
     *
     * @param user the customer or admin that is being checked.
     * @return true if found another user in system with this email, otherwise false.
     */
    private boolean isCustomerOrAdminExists(User user) {

        String cleanEmail = user.getEmail().replaceAll("\\s+", "");
        return getAllUsers().stream()
                /*ignoring the customer or admin that we are checking in case of updating*/
                .filter(c -> c.getId() != user.getId())
                .map(User::getEmail)
                .map((email -> email.replaceAll("\\s+", "")))
                .anyMatch(cleanEmail::equals);
    }
}
