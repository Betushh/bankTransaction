package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.mapper.TransactionMapper;
import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;
import com.bankTransaction.transaction.model.entity.Transaction;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.TransactionRepository;
import com.bankTransaction.transaction.service.AccountService;
import com.bankTransaction.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;

    @Override
    public TransactionDto createTransaction(String accountNumber, BigDecimal amount,
                                            TransactionType transactionType, TransactionStatus transactionStatus) {

        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionType(transactionType);
        transaction.setAccount(account);
        transaction = transactionRepository.save(transaction);

        if (transactionStatus == TransactionStatus.SUCCESS) {
            if (transactionType == TransactionType.TOP_UP) {
                accountService.increaseAccountBalance(accountNumber, amount);
            } else if (transactionType == TransactionType.PURCHASE) {
                accountService.decreaseAccountBalance(accountNumber, amount);
            }


        }
        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto topUpBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus) {

        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionType(TransactionType.TOP_UP);
        transaction.setAccount(account);
        transaction = transactionRepository.save(transaction);

        switch (transactionStatus) {
            case SUCCESS -> accountService.increaseAccountBalance(accountNumber, amount);
            case FAILED -> throw new IllegalArgumentException("no no no");
            case PENDING -> log.info("pending");
        }

        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto purchaseBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus) {

        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionType(TransactionType.TOP_UP);
        transaction.setAccount(account);
        transaction = transactionRepository.save(transaction);

        switch (transactionStatus) {
            case SUCCESS -> accountService.decreaseAccountBalance(accountNumber, amount);
            case FAILED -> throw new IllegalArgumentException("no no no");
            case PENDING -> log.info("pending");
        }

        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto refundBalanceTransaction(String accountNumber, BigDecimal amount, TransactionStatus transactionStatus) {

        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (!(AccountStatus.ACTIVE.equals(account.getAccountStatus()))) {
            throw new IllegalArgumentException("no no no");
        }

        return null;
    }

    @Override
    public TransactionDto changePaymentStatus(Long transactionId, TransactionStatus transactionStatus) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(IllegalArgumentException::new);
        transaction.setTransactionStatus(transactionStatus);
        return transactionMapper.toTransactionDto(transaction);
    }

}

