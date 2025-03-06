package com.bankTransaction.transaction.service;
import com.bankTransaction.transaction.model.dto.AccountDto;
import com.bankTransaction.transaction.model.entity.Account;
import java.util.List;

public interface AccountService {

    List<AccountDto> getList();

    Account getAccountByID(Integer id);

    Account add(Account account);

    Account update(Integer id, Account account);

    void delete(Integer id);
}
