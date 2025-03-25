package com.bankTransaction.transaction.service;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;

import java.math.BigDecimal;

public interface TransactionService {

    TransactionDto createTransaction(String accountNumber, BigDecimal amount, TransactionType transactionType);

    TransactionDto topUpBalanceTransaction(Long transactionId, TransactionStatus transactionStatus);

    TransactionDto purchaseBalanceTransaction(Long transactionId, TransactionStatus transactionStatus);

    TransactionDto refundBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus);


}
