package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.model.dto.account.AccountDto;
import com.bankTransaction.transaction.model.dto.account.AddAccountRequestDto;
import com.bankTransaction.transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getList() {
        return accountService.getList();
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getActiveAccountsByCustomerId(@RequestParam Integer id) {
        return accountService.getActiveAccountsByCustomerId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto add(AddAccountRequestDto addAccountRequestDto) {
        return accountService.add(addAccountRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getAccountsByCustomerId(@PathVariable Integer id) {
        return accountService.getAccountsByCustomerId(id);
    }

    @PutMapping("/increase/{accountNumber}/balance")
    public AccountDto increaseAccountBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        return accountService.increaseAccountBalance(accountNumber, amount);
    }

    @PutMapping("/decrease/{accountNumber}/balance")
    public AccountDto decreaseAccountBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        return accountService.decreaseAccountBalance(accountNumber, amount);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        accountService.delete(id);
    }
}
