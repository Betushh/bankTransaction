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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;

    @Override
    public TransactionDto createTransaction(String accountNumber, BigDecimal amount, TransactionType transactionType) {
        var account = accountRepository.findAccountByAccountNumber(accountNumber);

        Transaction transaction = new Transaction();
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        account.setTransaction(List.of(transaction));

        return transactionMapper.toTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDto topUpBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(IllegalAccessError::new);

        if (!(transaction.getTransactionType() == TransactionType.TOP_UP)) {
            throw new IllegalArgumentException("no no no");
        }

        String accountNumber = transaction.getAccount().getAccountNumber();
        var amount = transaction.getAmount();

        switch (transactionStatus) {
            case SUCCESS -> accountService.increaseAccountBalance(accountNumber, amount);
            case FAILED -> throw new IllegalArgumentException("no no no");
            case PENDING -> log.info("pending");
        }

        transaction.setTransactionStatus(transactionStatus);
        transactionRepository.save(transaction);
        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto purchaseBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(IllegalAccessError::new);

        if (!(transaction.getTransactionType() == TransactionType.PURCHASE)) {
            throw new IllegalArgumentException("no no no");
        }

        String accountNumber = transaction.getAccount().getAccountNumber();
        var amount = transaction.getAmount();

        switch (transactionStatus) {
            case SUCCESS -> accountService.decreaseAccountBalance(accountNumber, amount);

            case FAILED -> throw new IllegalArgumentException("no no no");
            case PENDING -> log.info("pending");
        }

        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
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


}

