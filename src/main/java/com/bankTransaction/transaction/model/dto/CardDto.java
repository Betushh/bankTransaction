package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.CardStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CardDto {

    private Long id;
    private Long cardNumber;
    private Long cardHolderName;
    private Long cvv;
    private CardStatus cardStatus;

//    private List<Transaction> transactions;
}
