package com.example.receiptapp.service;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user related operations.
 */
@Service
public class UserService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new {@code UserService} instance.
     *
     * @param repository      user repository
     * @param passwordEncoder password encoder
     */
    public UserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param user user to register
     * @return saved user
     */
    public AppUser register(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    /**
     * Finds a user by username.
     *
     * @param username username to search
     * @return optional user
     */
    public Optional<AppUser> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
