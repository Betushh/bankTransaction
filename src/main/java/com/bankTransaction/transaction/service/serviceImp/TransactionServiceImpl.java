package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.enumeration.TransactionType;
import com.bankTransaction.transaction.exception.MismatchException;
import com.bankTransaction.transaction.exception.NotFoundException;
import com.bankTransaction.transaction.exception.OperationExpiredException;
import com.bankTransaction.transaction.exception.OperationFailedException;
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
        log.info("Creating {} transaction of {} for account {}", transactionType, amount, accountNumber);
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account == null) {
            log.error("Account {} not found", accountNumber);
            throw new NotFoundException("Account not found");
        }

        Transaction transaction = Transaction.builder()
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(transactionType)
                .amount(amount)
                .account(account)
                .build();

        account.setTransaction(List.of(transaction));
        return transactionMapper.toTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDto topUpBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {

        log.info("Processing top-up transaction: {} with status: {}", transactionId, transactionStatus);
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
        {
            log.error("Transaction not found with ID: {}", transactionId);
            return new NotFoundException("Transaction not found");
        });

        if (!(transaction.getTransactionType() == TransactionType.TOP_UP)) {
            log.warn("Transaction type mismatch for ID: {}", transactionId);
            throw new MismatchException("Transaction type is not matched");
        }

        if(!(transaction.getTransactionStatus().equals(TransactionStatus.PENDING))){
            log.warn("Transaction is already done: {}", transactionId);
            throw new OperationExpiredException("Transaction operation is already done");
        }

        String accountNumber = transaction.getAccount().getAccountNumber();
        var amount = transaction.getAmount();

        switch (transactionStatus) {
            case SUCCESS -> {
                log.info("Top-up successful for account: {} with amount: {}", accountNumber, amount);
                accountService.increaseAccountBalance(accountNumber, amount);
            }
            case FAILED -> {
                log.error("Top-up failed for transaction ID: {}", transactionId);
                throw new OperationFailedException("Operation failed");
            }
            case PENDING -> log.info("Transaction is pending for ID: {}", transactionId);
        }

        transaction.setTransactionStatus(transactionStatus);
        transactionRepository.save(transaction);
        log.info("Transaction {} updated to status: {}", transactionId, transactionStatus);
        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto purchaseBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {

        log.info("Processing purchase transaction: {} with status: {}", transactionId, transactionStatus);
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new NotFoundException("Transaction not found"));

        if (!(transaction.getTransactionType() == TransactionType.PURCHASE)) {
            log.warn("Transaction type mismatch for ID: {}", transactionId);
            throw new MismatchException("Transaction type is not matched");
        }

        if(!(transaction.getTransactionStatus().equals(TransactionStatus.PENDING))){
            log.warn("Transaction is already done: {}", transactionId);
            throw new OperationExpiredException("Transaction operation is already done");
        }

        String accountNumber = transaction.getAccount().getAccountNumber();
        var amount = transaction.getAmount();

        switch (transactionStatus) {
            case SUCCESS -> {
                log.info("Purchase successful for account: {} with amount: {}", accountNumber, amount);
                accountService.decreaseAccountBalance(accountNumber, amount);
            }
            case FAILED -> {
                log.error("Purchase failed for transaction ID: {}", transactionId);
                throw new OperationFailedException("Operation failed");
            }
            case PENDING -> log.info("Transaction is pending for ID: {}", transactionId);
        }

        transaction.setTransactionStatus(transactionStatus);
        transactionRepository.save(transaction);
        log.info("Transaction {} updated to status: {}", transactionId, transactionStatus);
        return transactionMapper.toTransactionDto(transaction);
    }

    @Override
    public TransactionDto refundBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {

        log.info("Processing refund transaction: {} with status: {}", transactionId, transactionStatus);
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() ->
                new NotFoundException("Transaction not found"));


        if (!(transaction.getTransactionType() == TransactionType.PURCHASE)) {
            log.warn("Transaction type cannot be refunded for ID: {}", transactionId);
            throw new MismatchException("Transaction type cannot be refunded");
        }

        if(!(transaction.getTransactionStatus().equals(TransactionStatus.SUCCESS))){
            log.warn("Refund operation has already done: {}", transactionId);
            throw new OperationExpiredException("Refund operation has already done");
        }

        if (transactionRepository.findAll().stream()
                .anyMatch(t -> t.getAccount().getId().equals(transaction.getAccount().getId()) &&
//                        t.getOriginalTransaction().getId().equals(transaction.getOriginalTransaction().getId()) &&
                        t.getTransactionType().equals(TransactionType.REFUND) &&
                        t.getTransactionStatus().equals(TransactionStatus.SUCCESS))) {
            throw new OperationFailedException("Refund has already been processed for this transaction.");
        }

        String accountNumber = transaction.getAccount().getAccountNumber();
        var amount = transaction.getAmount();

        Transaction refundedTransaction = Transaction.builder()
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.REFUND)
                .amount(transaction.getAmount())
                .account(transaction.getAccount())
                .build();

        switch (transactionStatus) {
            case SUCCESS -> {
                log.info("Refund successful for account: {} with amount: {}", accountNumber, amount);
                accountService.refundAccountBalance(accountNumber, amount);
            }
            case FAILED -> {
                log.error("Purchase failed for transaction ID: {}", transactionId);
                throw new OperationFailedException("Operation failed");
            }
            case PENDING -> log.info("Transaction is pending for ID: {}", transactionId);
        }

        refundedTransaction.setTransactionStatus(transactionStatus);
        transactionRepository.save(refundedTransaction);
        log.info("Transaction {} updated to status: {}", transactionId, transactionStatus);
        return transactionMapper.toTransactionDto(refundedTransaction);
    }

    @Override
    public void delete(Integer id) {

    }


}

