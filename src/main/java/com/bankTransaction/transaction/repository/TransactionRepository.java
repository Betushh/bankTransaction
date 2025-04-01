package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

//    @EntityGraph(value = "Transaction.account", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Transaction> findById(Long transactionId);

//    //    @EntityGraph(value = "studentTasks")
//    Optional<Transaction> findBy
}
