package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import com.bankTransaction.transaction.model.entity.Card;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransactionDto extends BaseEntity {

    private TransactionType transactionType;

    private Double amount;
    private TransactionStatus transactionStatus;
    private Account sender;
    private Account receiver;
    private Card senderCard;
    private Card receiverCard;
}
