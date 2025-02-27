package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity{

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private Double balance;

    @Enumerated
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Card> linkedCards;

}
