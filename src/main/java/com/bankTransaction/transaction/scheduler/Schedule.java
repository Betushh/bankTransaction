package com.bankTransaction.transaction.scheduler;

import com.bankTransaction.transaction.enumeration.TransactionStatus;
import com.bankTransaction.transaction.model.entity.Transaction;
import com.bankTransaction.transaction.repository.TransactionRepository;
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


    @Scheduled(cron = "0 39 15 * * ?", zone = "Asia/Baku")
    public void scheduleCron() {
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> TransactionStatus.PENDING.equals(transaction.getTransactionStatus()))
                .peek(transaction -> transaction.setTransactionStatus(TransactionStatus.SUCCESS))
                .toList();

        transactionRepository.saveAll(transactions);

        log.info("Fixed cron - " + System.currentTimeMillis() / 1000);
    }
}
