package com.bankTransaction.transaction.model.dto.customer;

import com.bankTransaction.transaction.model.entity.Account;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private List<Account> accounts;

}
