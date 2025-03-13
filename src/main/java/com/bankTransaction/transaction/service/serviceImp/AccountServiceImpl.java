package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.mapper.AccountMapper;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.account.AccountDto;
import com.bankTransaction.transaction.model.dto.account.AddAccountRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.AccountService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<AccountDto> getList() {
        return accountMapper.toAccountDtoList(accountRepository
                .findAll().stream()
                .filter(account -> AccountStatus.ACTIVE
                        .equals(account.getAccountStatus()))
                .toList());
    }


    @Override
    public List<AccountDto> getActiveAccountsByCustomerId(Integer id) {
        return customerRepository.findById(id)
                .map(customer -> customer.getAccounts().stream()
                        .filter(account -> AccountStatus.ACTIVE.equals(account.getAccountStatus()))
                        .map(accountMapper::toAccountDto) // Convert to DTOs
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    @Override
    public AccountDto add(AddAccountRequestDto addAccountRequestDto) {
        var customer = customerRepository.findById(addAccountRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("no no no"));

        Account account = getAccount(customer);

        customer.setAccounts(List.of(account));
        accountRepository.save(account);
        return accountMapper.toAccountDto(account);
    }

    @Override
    public List<AccountDto> getAccountsByCustomerId(Integer id) {
        return customerRepository.findById(id)
                .map(customer -> customer.getAccounts().stream()
                        .map(accountMapper::toAccountDto)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    @Override
    public AccountDto increaseAccountBalance(String accountNumber, BigDecimal amount) {
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        return accountMapper.toAccountDto(account);
    }

    @Override
    public AccountDto decreaseAccountBalance(String accountNumber, BigDecimal amount) {
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("no no no");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        return accountMapper.toAccountDto(account);
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    private Account getAccount(Customer customer) {
        return Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.valueOf(100))
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }


}
