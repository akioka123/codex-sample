package com.example.receiptapp.service;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.entity.Receipt;
import com.example.receiptapp.repository.AppUserRepository;
import com.example.receiptapp.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for receipt related operations.
 */
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final AppUserRepository userRepository;

    /**
     * Creates a new {@code ReceiptService} instance.
     *
     * @param receiptRepository receipt repository
     * @param userRepository    user repository
     */
    public ReceiptService(ReceiptRepository receiptRepository, AppUserRepository userRepository) {
        this.receiptRepository = receiptRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lists receipts using search conditions.
     *
     * @param username  owner username
     * @param start     start date
     * @param end       end date
     * @param tag       tag partial match
     * @param minAmount minimum amount
     * @param maxAmount maximum amount
     * @return list of receipts
     */
    public List<Receipt> listReceipts(String username,
                                      LocalDate start,
                                      LocalDate end,
                                      String tag,
                                      int minAmount,
                                      int maxAmount) {
        AppUser user = userRepository.findByUsername(username).orElseThrow();
        return receiptRepository.findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(
                user, start, end, tag, minAmount, maxAmount);
    }

    /**
     * Saves a receipt for a user.
     *
     * @param username owner username
     * @param receipt  receipt to save
     * @return saved receipt
     */
    public Receipt saveReceipt(String username, Receipt receipt) {
        AppUser user = userRepository.findByUsername(username).orElseThrow();
        receipt.setOwner(user);
        return receiptRepository.save(receipt);
    }

    /**
     * Deletes a receipt.
     *
     * @param id receipt identifier
     */
    public void deleteReceipt(Long id) {
        receiptRepository.deleteById(id);
    }
}
