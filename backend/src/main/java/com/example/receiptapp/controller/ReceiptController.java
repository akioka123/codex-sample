package com.example.receiptapp.controller;

import com.example.receiptapp.entity.Receipt;
import com.example.receiptapp.service.ReceiptService;
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
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
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
        return receiptService.listReceipts(
                auth.getName(), start, end, tag, minAmount, maxAmount);
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
        return receiptService.saveReceipt(auth.getName(), receipt);
    }

    /**
     * Deletes a receipt.
     *
     * @param id receipt identifier
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
    }
}
