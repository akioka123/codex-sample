package com.example.receiptapp.controller;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.service.UserService;
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
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param user user to register
     * @return saved user
     */
    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.register(user);
    }
}
