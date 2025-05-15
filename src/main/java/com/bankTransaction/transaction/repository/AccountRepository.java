package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findAccountByAccountNumber(String accountNumber);

    @Query(value = "select * from accounts where account_status = 'ACTIVE'", nativeQuery = true)
   List<Account> findActiveAccounts();

    @Query(value = "select * from accounts where account_status = 'ACTIVE' and customer_id= :customerId", nativeQuery = true)
   Optional<List<Account>> findActiveAccounts(@Param("customerId")Integer customerId);

}
