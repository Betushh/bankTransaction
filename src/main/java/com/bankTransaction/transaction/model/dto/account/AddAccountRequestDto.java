package com.bankTransaction.transaction.model.dto.account;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Customer;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class AddAccountRequestDto {

    private String accountNumber;
    private BigDecimal balance;
    private Customer customer;
    private AccountStatus accountStatus;
}
