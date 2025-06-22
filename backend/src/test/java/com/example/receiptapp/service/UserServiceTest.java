package com.example.receiptapp.service;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<AppUser> userCaptor;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(repository, passwordEncoder);
    }

    @Test
    void registerEncodesPasswordAndSavesUser() {
        AppUser user = new AppUser();
        user.setPassword("plain");
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(repository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser saved = userService.register(user);

        verify(passwordEncoder).encode("plain");
        verify(repository).save(userCaptor.capture());
        assertEquals("encoded", userCaptor.getValue().getPassword());
        assertEquals("encoded", saved.getPassword());
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        when(repository.findByUsername("name")).thenReturn(Optional.empty());
        userService.findByUsername("name");
        verify(repository).findByUsername("name");
    }
}
