package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@RequiredArgsConstructor
public class CustomerDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
   private LocalDate dateOfBirth;
    private List<Account> accounts;


}
