package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class TransactionDto {

    private Long id;
    private TransactionType transactionType;

    private BigDecimal amount;
    private TransactionStatus transactionStatus;
    private String accountSender;
    private String accountReceiver;

//    private Card senderCard;

//    private Card receiverCard;
}
