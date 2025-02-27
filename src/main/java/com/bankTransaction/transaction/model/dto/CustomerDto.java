package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.CustomerStatus;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.BaseEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@RequiredArgsConstructor
public class CustomerDto extends BaseEntity {

    private String firsName;
    private String lastName;
    private String mail;
    private String phoneNumber;
    private CustomerStatus customerStatus;
    private List<Account> accounts;
}
