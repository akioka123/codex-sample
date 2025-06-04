package com.example.receiptapp.controller;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling user registration.
 */
@RestController
@RequestMapping("/api")
public class UserController {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param user user to register
     * @return saved user
     */
    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }
}
