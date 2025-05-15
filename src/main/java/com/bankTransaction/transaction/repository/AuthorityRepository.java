package com.bankTransaction.transaction.repository;

import com.bankTransaction.transaction.model.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findByAuthority(String authority);

}
