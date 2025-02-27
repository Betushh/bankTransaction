package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @Enumerated
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    private Double amount;

    @Enumerated
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiver;

    @ManyToOne
    @JoinColumn(name = "sender_card_id")
    private Card senderCard;

    @ManyToOne
    @JoinColumn(name = "receiver_card_id")
    private Card receiverCard;

    @OneToOne
    @JoinColumn(name = "source_account")
    private Account sourceAccount;

    @OneToOne
    @JoinColumn(name = "source_card")
    private Card sourceCard;




}
