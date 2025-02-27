package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import com.bankTransaction.transaction.model.entity.Card;
import com.bankTransaction.transaction.model.entity.Customer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class AccountDto extends BaseEntity {

    private Long accountNumber;
    private Double balance;
    private AccountStatus accountStatus;
    private Customer customer;
    private List<Card> linkedCards;
}
