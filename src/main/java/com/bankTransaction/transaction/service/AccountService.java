package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.dto.account.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<AccountDto> getList();

    List<AccountDto> getActiveAccountsByCustomerId(Integer id);

    AccountDto add(Integer customerId);

    List<AccountDto> getAccountsByCustomerId(Integer id);

    AccountDto increaseAccountBalance(String accountNumber, BigDecimal amount);

    AccountDto decreaseAccountBalance(String accountNumber, BigDecimal amount);

    AccountDto refundAccountBalance(String accountNumber, BigDecimal amount);

    void delete(Integer id);

}
