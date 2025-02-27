package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class TransactionDto extends BaseEntity {

    private TransactionType transactionType;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;

}
