package com.example.receiptapp.controller;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.entity.Receipt;
import com.example.receiptapp.repository.AppUserRepository;
import com.example.receiptapp.repository.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for {@link ReceiptController}.
 */
@WebMvcTest(ReceiptController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiptRepository receiptRepository;

    @MockBean
    private AppUserRepository userRepository;

    /**
     * Verifies searching receipts with parameters.
     */
    @Test
    void listSearchesReceipts() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("alice");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        Receipt r = new Receipt();
        r.setId(2L);
        r.setTag("food");
        r.setAmount(500);
        r.setDate(LocalDate.now());
        when(receiptRepository.findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(
                eq(user), any(LocalDate.class), any(LocalDate.class), eq("food"), eq(0), eq(1000)))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/api/receipts")
                        .with(user("alice"))
                        .param("start", "2024-05-01")
                        .param("end", "2024-05-31")
                        .param("tag", "food")
                        .param("minAmount", "0")
                        .param("maxAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value("food"));

        verify(receiptRepository).findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(
                eq(user), any(LocalDate.class), any(LocalDate.class), eq("food"), eq(0), eq(1000));
    }

    /**
     * Ensures saving a receipt assigns the authenticated owner.
     */
    @Test
    void saveAssignsOwner() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("alice");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(receiptRepository.save(any(Receipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/receipts")
                        .with(user("alice"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tag\":\"food\",\"amount\":100}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Receipt> captor = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).save(captor.capture());
        assertEquals(user, captor.getValue().getOwner());
    }

    /**
     * Ensures deleting a receipt calls the repository.
     */
    @Test
    void deleteRemovesReceipt() throws Exception {
        mockMvc.perform(delete("/api/receipts/5"))
                .andExpect(status().isOk());
        verify(receiptRepository).deleteById(5L);
    }
}
