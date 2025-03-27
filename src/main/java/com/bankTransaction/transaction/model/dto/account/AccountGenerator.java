package com.bankTransaction.transaction.model.dto.account;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
public class AccountGenerator {
    public static Account createDefaultAccount (Customer customer){
        log.trace("Creating default account for customer: {}", customer.getEmail());
        var account =  Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.valueOf(100))
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
        log.debug("Default account created: {}", account.getAccountNumber());
        return account;
    }

    public static String generateAccountNumber () {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }
}
