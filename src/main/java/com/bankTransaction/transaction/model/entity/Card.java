package com.bankTransaction.transaction.model.entity;

import com.bankTransaction.transaction.enumeration.CardStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "cards")
public class Card extends BaseEntity{

    @Column(name = "card_number", nullable = false)
    private Long cardNumber;

    @Column(name = "cardholder_name", nullable = false)
    private Long cardHolderName;

    @Column(name = "cvv", nullable = false)
    private Long cvv;

    @Enumerated
    @Column(name = "card_status", nullable = false)
    private CardStatus cardStatus;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "senderCard",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
