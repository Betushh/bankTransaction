package com.bankTransaction.transaction.model.entity;


import com.bankTransaction.transaction.enumeration.CustomerStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firsName;
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Column(name = "middlename")
    private String middleName;

    @Column(nullable = false)
    private String mail;

    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;

    @Enumerated
    @Column(name = "customer_status", nullable = false)
    private CustomerStatus customerStatus;
}
