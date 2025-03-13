package com.bankTransaction.transaction.service;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    TransactionDto createTransaction(String accountNumber, BigDecimal amount,
                                     TransactionType transactionType, TransactionStatus transactionStatus);

    TransactionDto topUpBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus);

    TransactionDto purchaseBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus);

    TransactionDto refundBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus);

    TransactionDto changePaymentStatus(Long transactionId, TransactionStatus transactionStatus);

}
