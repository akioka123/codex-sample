package com.example.receiptapp.service;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.entity.Receipt;
import com.example.receiptapp.repository.AppUserRepository;
import com.example.receiptapp.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ReceiptService}.
 */
@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;
    @Mock
    private AppUserRepository userRepository;
    @Captor
    private ArgumentCaptor<Receipt> receiptCaptor;

    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        receiptService = new ReceiptService(receiptRepository, userRepository);
    }

    @Test
    void listReceiptsDelegatesToRepository() {
        AppUser user = new AppUser();
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        List<Receipt> expected = Collections.emptyList();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        when(receiptRepository.findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(user, start, end, "tag", 1, 10))
                .thenReturn(expected);

        List<Receipt> result = receiptService.listReceipts("user", start, end, "tag", 1, 10);

        assertSame(expected, result);
    }

    @Test
    void saveReceiptSetsOwnerAndSaves() {
        AppUser user = new AppUser();
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        Receipt receipt = new Receipt();
        when(receiptRepository.save(any(Receipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Receipt saved = receiptService.saveReceipt("user", receipt);

        verify(receiptRepository).save(receiptCaptor.capture());
        assertSame(user, receiptCaptor.getValue().getOwner());
        assertSame(user, saved.getOwner());
    }

    @Test
    void deleteReceiptDelegatesToRepository() {
        receiptService.deleteReceipt(1L);
        verify(receiptRepository).deleteById(1L);
    }
}
