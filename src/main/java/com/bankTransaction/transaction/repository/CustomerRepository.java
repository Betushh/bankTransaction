package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
