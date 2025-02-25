package com.bankTransaction.transaction.model.entity;

import com.bankTransaction.transaction.enumeration.CardStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "card_number", nullable = false)
    private Long cardNumber;

    @Column(name = "cardholder_name", nullable = false)
    private Long cardHolderName;

    @Column(name = "cvv", nullable = false)
    private Long cvv;

    @Enumerated
    @Column(name = "card_status", nullable = false)
    private CardStatus cardStatus;


}
