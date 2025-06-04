package com.example.receiptapp.repository;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link Receipt}.
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(
            AppUser owner,
            LocalDate start,
            LocalDate end,
            String tag,
            int minAmount,
            int maxAmount);
}
