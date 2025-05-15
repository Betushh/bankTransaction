package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.model.dto.account.AccountDto;
import com.bankTransaction.transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getList() {
        return accountService.getList();
    }

    @GetMapping("/activeAccounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getActiveAccountsByCustomerId(@PathVariable Integer id) {
        return accountService.getActiveAccountsByCustomerId(id);
    }

    @PostMapping("/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto add(@PathVariable Integer customerId) {
        return accountService.add(customerId);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getAccountsByCustomerId(@PathVariable Integer id) {
        return accountService.getAccountsByCustomerId(id);
    }

    @PutMapping("/increase/{accountNumber}/balance")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto increaseAccountBalance(@PathVariable String accountNumber,
                                             @RequestParam BigDecimal amount) {
        return accountService.increaseAccountBalance(accountNumber, amount);
    }

    @PutMapping("/decrease/{accountNumber}/balance")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto decreaseAccountBalance(@PathVariable String accountNumber,
                                             @RequestParam BigDecimal amount) {
        return accountService.decreaseAccountBalance(accountNumber, amount);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        accountService.delete(id);
    }

}
