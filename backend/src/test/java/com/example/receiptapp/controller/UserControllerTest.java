package com.example.receiptapp.controller;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link UserController}.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * Ensures the password is encoded and the user saved.
     */
    @Test
    void registerEncodesPasswordAndSavesUser() throws Exception {
        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(repository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\",\"password\":\"plain\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("hashed"));

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(passwordEncoder).encode("plain");
        verify(repository).save(captor.capture());
        assertEquals("hashed", captor.getValue().getPassword());
    }
}
