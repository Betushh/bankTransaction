package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findCustomerByFirstName(String firstname);

    Boolean existsById(Long id);

}
