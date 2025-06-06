package com.bankTransaction.transaction.scheduler;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.model.entity.Transaction;
import com.bankTransaction.transaction.repository.TransactionRepository;
import com.bankTransaction.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class Schedule {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;


    @Scheduled(cron = "0 58 11 * * ?", zone = "Asia/Baku")
    @Transactional
    public void scheduleCron() {
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> TransactionStatus.PENDING.equals(transaction.getTransactionStatus()))
                .toList();

        for (Transaction transaction : transactions) {
            switch (transaction.getTransactionType()) {
                case TOP_UP ->
                        transactionService.topUpBalanceTransaction(transaction.getId(), TransactionStatus.SUCCESS);
                case PURCHASE ->
                        transactionService.purchaseBalanceTransaction(transaction.getId(), TransactionStatus.SUCCESS);
                case REFUND ->
                        transactionService.refundBalanceTransaction(transaction.getId(), TransactionStatus.SUCCESS);
            }
        }

        transactionRepository.saveAll(transactions);
        log.info("Transactions are made - " + System.currentTimeMillis() / 1000);
    }
}
