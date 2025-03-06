package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.mapper.AccountMapper;
import com.bankTransaction.transaction.model.dto.AccountDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.service.AccountService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class AccountServiceImpl implements AccountService {

   private final AccountRepository accountRepository;
   private final AccountMapper accountMapper;

    @Override
    public List<AccountDto> getList() {
        return accountMapper.toAccountDtoList(accountRepository
                .findAll().stream()
                .filter(account -> account
                        .getAccountStatus()
                        .name()
                        .equalsIgnoreCase("Active"))
                .toList());
    }

    @Override
    public Account getAccountByID(Integer id) {
        return null;
    }

    @Override
    public Account add(Account account) {
        return null;
    }

    @Override
    public Account update(Integer id, Account account) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
