package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "account_status")
    private AccountStatus accountStatus;

}
