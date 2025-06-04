package com.example.receiptapp.controller;

import com.example.receiptapp.entity.AppUser;
import com.example.receiptapp.entity.Receipt;
import com.example.receiptapp.repository.AppUserRepository;
import com.example.receiptapp.repository.ReceiptRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller handling receipt operations.
 */
@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {
    private final ReceiptRepository receiptRepository;
    private final AppUserRepository userRepository;

    public ReceiptController(ReceiptRepository receiptRepository, AppUserRepository userRepository) {
        this.receiptRepository = receiptRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lists receipts using search conditions.
     *
     * @param auth authentication
     * @param start start date
     * @param end end date
     * @param tag tag partial match
     * @param minAmount minimum amount
     * @param maxAmount maximum amount
     * @return list of receipts
     */
    @GetMapping
    public List<Receipt> list(Authentication auth,
                              @RequestParam LocalDate start,
                              @RequestParam LocalDate end,
                              @RequestParam(defaultValue = "") String tag,
                              @RequestParam(defaultValue = "0") int minAmount,
                              @RequestParam(defaultValue = "1000000") int maxAmount) {
        AppUser user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return receiptRepository.findByOwnerAndDateBetweenAndTagContainingAndAmountBetween(
                user, start, end, tag, minAmount, maxAmount);
    }

    /**
     * Adds or updates a receipt.
     *
     * @param auth authentication
     * @param receipt receipt to save
     * @return saved receipt
     */
    @PostMapping
    public Receipt save(Authentication auth, @RequestBody Receipt receipt) {
        AppUser user = userRepository.findByUsername(auth.getName()).orElseThrow();
        receipt.setOwner(user);
        return receiptRepository.save(receipt);
    }

    /**
     * Deletes a receipt.
     *
     * @param id receipt identifier
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        receiptRepository.deleteById(id);
    }
}
