package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select * from customer where username = :username ", nativeQuery = true)
    Optional<Customer> findByUsername(@Param("username") String username);

    Boolean existsByEmail(String email);

}
