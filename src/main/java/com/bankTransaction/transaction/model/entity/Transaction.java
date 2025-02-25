package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    private BigDecimal amount;

    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "account_sender", nullable = false)
    private String accountSender;

    @Column(name = "account_receiver", nullable = false)
    private String accountReceiver;



}
