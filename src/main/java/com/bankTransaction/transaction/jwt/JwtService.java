package com.bankTransaction.transaction.jwt;

import com.bankTransaction.transaction.model.entity.Customer;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String issueToken(Customer customer);

    Claims verifyToken(String token);

}
