package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.exception.InsufficientBalanceException;
import com.bankTransaction.transaction.exception.MismatchException;
import com.bankTransaction.transaction.exception.NotFoundException;
import com.bankTransaction.transaction.mapper.AccountMapper;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.account.AccountDto;
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

    @Override
    public List<AccountDto> getList() {

        log.info("Fetching all active accounts");
        var accounts = accountRepository.findActiveAccounts();
        log.info("{} active accounts found", accounts.size());
        return accountMapper.toAccountDtoList(accounts);

    }

    @Override
    public List<AccountDto> getActiveAccountsByCustomerId(Integer id) {

        log.info("Fetching active accounts for customer ID: {}", id);
        var accounts = accountRepository.findActiveAccounts(id).orElseThrow(() -> {
                    log.warn("Customer with ID {} not found", id);
                    return new NotFoundException("Accounts not found");
                });
        log.info("{} active accounts found", accounts.size());
        return accountMapper.toAccountDtoList(accounts);

    }

    @Override
    public AccountDto add(Integer customerId) {

        log.info("Creating new account for customer ID: {}", customerId);
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer with ID {} not found", customerId);
                    return new NotFoundException("Customer not found");
                });

        Account account = createDefaultAccount(customer);
        customer.setAccounts(List.of(account));
        accountRepository.save(account);

        log.info("New account created: {}", account.getAccountNumber());
        return accountMapper.toAccountDto(account);

    }

    @Override
    public List<AccountDto> getAccountsByCustomerId(Integer id) {

        log.info("Fetching all accounts for customer ID: {}", id);
        var accounts = customerRepository.findById(id)
                .map(customer -> customer.getAccounts().stream()
                        .toList()).orElseThrow(() -> {
                    log.warn("Customer with ID {} not found", id);
                    return new NotFoundException("Accounts not found");
                });
        return accountMapper.toAccountDtoList(accounts);

    }

    @Override
    public AccountDto increaseAccountBalance(String accountNumber, BigDecimal amount) {

        log.info("Increasing balance for account: {} by amount: {}", accountNumber, amount);
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            log.warn("Account {} is not active", accountNumber);
            throw new MismatchException("Account is not Active");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        log.info("New balance for account {}: {}", accountNumber, account.getBalance());
        return accountMapper.toAccountDto(account);

    }

    @Override
    public AccountDto decreaseAccountBalance(String accountNumber, BigDecimal amount) {

        log.info("Decreasing balance for account: {} by amount: {}", accountNumber, amount);
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            log.warn("Account {} is not active", accountNumber);
            throw new MismatchException("Account is not Active");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            log.error("Insufficient balance for account: {}", accountNumber);
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        log.info("New balance for account {}: {}", accountNumber, account.getBalance());
        return accountMapper.toAccountDto(account);

    }

    @Override
    public AccountDto refundAccountBalance(String accountNumber, BigDecimal amount) {

        log.info("Refunding balance for account: {} by amount: {}", accountNumber, amount);
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            log.warn("Account {} is not active", accountNumber);
            throw new MismatchException("Account is not Active");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        log.info("New balance for account {}: {}", accountNumber, account.getBalance());
        return accountMapper.toAccountDto(account);

    }

    @Override
    public void delete(Integer id) {

        log.info("Deleting account with ID: {}", id);
        accountRepository.deleteById(id);
        log.info("Account with ID {} deleted successfully", id);

    }

    private Account createDefaultAccount(Customer customer) {

        log.trace("Creating default account for customer: {}", customer.getEmail());
        var account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.valueOf(100))
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
        log.debug("Default account created: {}", account.getAccountNumber());
        return account;

    }

    private  String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }

}
