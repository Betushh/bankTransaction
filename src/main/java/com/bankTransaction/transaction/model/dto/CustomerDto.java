package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.CustomerStatus;
import com.bankTransaction.transaction.model.entity.Account;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@RequiredArgsConstructor
public class CustomerDto {

    private Long id;
    private String firsName;
    private String lastName;
    private String middleName;
    private String mail;
    private String phoneNumber;
    private CustomerStatus customerStatus;

 //   private List<Account> accounts;
}
