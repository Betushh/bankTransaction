package com.bankTransaction.transaction.service.serviceImp;

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

        log.info("Initiating '{}' transaction of amount {} for account '{}'", transactionType, amount, accountNumber);
        var account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account == null) {
            log.error("Account '{}' not found", accountNumber);
            throw new NotFoundException("Account not found");
        }

        Transaction transaction = Transaction.builder()
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(transactionType)
                .amount(amount)
                .account(account)
                .build();

        account.setTransaction(List.of(transaction)); // This might be unnecessary
        Transaction saved = transactionRepository.save(transaction);

        log.info("Transaction created with ID: {}", saved.getId());
        return transactionMapper.toTransactionDto(saved);

    }

    @Override
    public TransactionDto topUpBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {
        Transaction transaction = validateTransaction(transactionId, TransactionType.TOP_UP, TransactionStatus.PENDING);
        return handleTransaction(transaction, transactionStatus, true);
    }

    @Override
    public TransactionDto purchaseBalanceTransaction(Long transactionId, TransactionStatus transactionStatus) {
        Transaction transaction = validateTransaction(transactionId, TransactionType.PURCHASE, TransactionStatus.PENDING);
        return handleTransaction(transaction, transactionStatus, false);
    }

    @Override
    public TransactionDto refundBalanceTransaction(Long transactionId, TransactionStatus status) {

        log.info("Processing refund for transaction ID: {} with status: {}", transactionId, status);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        String accountNumber = transaction.getAccount().getAccountNumber();
        BigDecimal amount = transaction.getAmount();

        if (transaction.getTransactionType() == TransactionType.TOP_UP) {
            log.warn("Refund not allowed for TOP_UP transactions (ID: {})", transactionId);
            throw new MismatchException("TOP_UP transactions cannot be refunded");
        }

        switch (transaction.getTransactionType()) {
            case PURCHASE -> {
                if (transaction.getTransactionStatus() != TransactionStatus.SUCCESS) {
                    log.warn("Refund rejected: PURCHASE transaction must be SUCCESS (ID: {})", transactionId);
                    throw new MismatchException("Refund allowed only for successful PURCHASE transactions");
                }
                if (status == TransactionStatus.SUCCESS) {
                    accountService.refundAccountBalance(accountNumber, amount);
                    transaction.setTransactionStatus(TransactionStatus.REFUNDED);
                    log.info("Purchase refunded successfully: {} → {}", accountNumber, amount);
                } else {
                    handleInvalidRefundStatus(status, transactionId);
                }
            }

            case REFUND -> {
                if (transaction.getTransactionStatus() != TransactionStatus.PENDING) {
                    log.warn("Refund already handled or invalid state (ID: {})", transactionId);
                    throw new OperationExpiredException("Refund is already processed or invalid");
                }
                if (status == TransactionStatus.SUCCESS) {
                    accountService.refundAccountBalance(accountNumber, amount);
                    transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                    log.info("Refund successful: {} → {}", accountNumber, amount);
                } else {
                    handleInvalidRefundStatus(status, transactionId);
                }
            }
        }

        transactionRepository.save(transaction);
        return transactionMapper.toTransactionDto(transaction);

    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting transaction with ID: {}", id);
        transactionRepository.deleteById(id);
        log.info("Transaction with ID {} deleted successfully", id);
    }

    private Transaction validateTransaction(Long id, TransactionType expectedType, TransactionStatus expectedStatus) {

        log.debug("Validating transaction ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        if (transaction.getTransactionType() != expectedType) {
            throw new MismatchException("Expected transaction type: " + expectedType);
        }

        if (transaction.getTransactionStatus() != expectedStatus) {
            throw new OperationExpiredException("Transaction status must be: " + expectedStatus);
        }

        return transaction;

    }

    private TransactionDto handleTransaction(Transaction transaction, TransactionStatus status, boolean isTopUp) {

        String accountNumber = transaction.getAccount().getAccountNumber();
        BigDecimal amount = transaction.getAmount();
        Long id = transaction.getId();

        switch (status) {
            case SUCCESS -> {
                if (isTopUp) {
                    accountService.increaseAccountBalance(accountNumber, amount);
                    log.info("Top-up completed for account {} with amount {}", accountNumber, amount);
                } else {
                    accountService.decreaseAccountBalance(accountNumber, amount);
                    log.info("Purchase completed for account {} with amount {}", accountNumber, amount);
                }
            }
            case FAILED -> {
                log.error("Transaction failed (ID: {})", id);
                throw new OperationFailedException("Transaction failed");
            }
            case PENDING -> log.info("Transaction is still pending (ID: {})", id);
            default -> log.warn("Unhandled transaction status: {}", status);
        }

        transaction.setTransactionStatus(status);
        transactionRepository.save(transaction);
        log.info("Transaction ID {} updated to status {}", id, status);
        return transactionMapper.toTransactionDto(transaction);

    }

    private void handleInvalidRefundStatus(TransactionStatus status, Long transactionId) {
        switch (status) {
            case FAILED -> throw new OperationFailedException("Refund failed");
            case REFUNDED -> throw new OperationExpiredException("Already refunded");
            case PENDING -> log.info("Refund is pending (ID: {})", transactionId);
            default -> throw new MismatchException("Invalid refund operation status");
        }
    }

}