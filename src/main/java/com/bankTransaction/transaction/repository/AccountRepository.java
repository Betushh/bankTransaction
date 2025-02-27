package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
