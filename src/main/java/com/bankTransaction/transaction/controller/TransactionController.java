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

    @PostMapping("/{accountNumber}")
    public TransactionDto createTransaction(String accountNumber, BigDecimal amount, TransactionType transactionType) {
        return transactionService.createTransaction(accountNumber, amount, transactionType);
    }

    @PutMapping("/TOP_UP/{accountNumber}/amount/{transactionStatus}")
    public TransactionDto topUpBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {
        return transactionService.topUpBalanceTransaction(transactionId,transactionStatus);
    }

    @PutMapping("/PURCHASE/{accountNumber}/amount/{transactionStatus}")
    public TransactionDto purchaseBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {
        return transactionService.purchaseBalanceTransaction(transactionId, transactionStatus);
    }


}
