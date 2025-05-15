package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Optional<Transaction> findById(Long transactionId);

}
