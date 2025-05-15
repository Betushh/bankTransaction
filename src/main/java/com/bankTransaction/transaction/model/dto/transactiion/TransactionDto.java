package com.bankTransaction.transaction.model.dto.transactiion;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionDto extends BaseEntity {

    private Long id;
    private TransactionType transactionType;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;

}
