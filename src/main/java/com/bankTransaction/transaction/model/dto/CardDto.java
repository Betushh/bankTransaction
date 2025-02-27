package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.CardStatus;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.model.entity.Transaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CardDto extends BaseEntity {

    private Long cardNumber;
    private Long cardHolderName;
    private Long cvv;
    private CardStatus cardStatus;
    private Account account;
    private List<Transaction> transactions;
}
