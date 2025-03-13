package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@Data
@RequiredArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @NotNull
    @Enumerated
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    private BigDecimal amount;

    @NotNull
    @Enumerated
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;


}
