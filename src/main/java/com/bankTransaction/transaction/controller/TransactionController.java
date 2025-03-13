package com.bankTransaction.transaction.controller;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;

import com.bankTransaction.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{accountNumber}/amount/transactionType/transactionStatus")
    public TransactionDto createTransaction(@PathVariable String accountNumber,
                                            @RequestParam BigDecimal amount,
                                            @RequestParam TransactionType transactionType,
                                            @RequestParam TransactionStatus transactionStatus) {
        return transactionService.createTransaction(accountNumber, amount, transactionType, transactionStatus);
    }

    @PutMapping("/TOP_UP/{accountNumber}/amount/{transactionStatus}")
    public TransactionDto topUpBalanceTransaction(@PathVariable String accountNumber,
                                                  @RequestParam BigDecimal amount,
                                                  @PathVariable TransactionStatus transactionStatus) {
        return transactionService.topUpBalanceTransaction(accountNumber, amount, transactionStatus);
    }

    @PutMapping("/PURCHASE/{accountNumber}/amount/{transactionStatus}")
    public TransactionDto purchaseBalanceTransaction(@PathVariable String accountNumber,
                                                     @RequestParam BigDecimal amount,
                                                     @PathVariable TransactionStatus transactionStatus) {
        return transactionService.purchaseBalanceTransaction(accountNumber, amount, transactionStatus);
    }

//    @PutMapping("/{transactionId}/transactionStatus")
//    public TransactionDto changePaymentStatus(@PathVariable Long transactionId, @RequestParam TransactionStatus transactionStatus) {
//       return changePaymentStatus(transactionId, transactionStatus);
//    }

}
