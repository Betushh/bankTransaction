package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;
import com.bankTransaction.transaction.service.TransactionService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{accountNumber}")
    public TransactionDto createTransaction(@PathVariable String accountNumber,
                                            @RequestParam @NotNull(message = "Amount is required")
                                            @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount,
                                            @RequestParam @NotNull(message = "Transaction type is required") TransactionType transactionType) {
        return transactionService.createTransaction(accountNumber, amount, transactionType);
    }

    @PutMapping("/topUp/{transactionId}/{transactionStatus}")
    public TransactionDto topUpBalanceTransaction(@PathVariable @NotNull(message = "Transaction ID is required") Long transactionId,
                                                  @PathVariable TransactionStatus transactionStatus) {
        return transactionService.topUpBalanceTransaction(transactionId, transactionStatus);
    }

    @PutMapping("/purchase/{transactionId}/{transactionStatus}")
    public TransactionDto purchaseBalanceTransaction(@PathVariable @NotNull(message = "Transaction ID is required") Long transactionId,
                                                     @PathVariable TransactionStatus transactionStatus) {
        return transactionService.purchaseBalanceTransaction(transactionId, transactionStatus);
    }

    @PutMapping("/refund/{transactionId}/{transactionStatus}")
    public TransactionDto refundBalanceTransaction(@PathVariable @NotNull(message = "Transaction ID is required") Long transactionId,
                                                   @PathVariable TransactionStatus transactionStatus) {
        return transactionService.refundBalanceTransaction(transactionId, transactionStatus);
    }


}
