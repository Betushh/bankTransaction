package com.bankTransaction.transaction.jwt.jwtImpl;

import com.bankTransaction.transaction.jwt.JwtService;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.model.security.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${issue.key}")
    private String issueKey;


    @PostConstruct
    public Key secrectKey() {
        byte[] keyBytes = Decoders.BASE64.decode(issueKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String issueToken(Customer customer) {

        List<String> authorities = customer
                .getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .toList();

        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(customer.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1))))
                .addClaims(Map.of("roles", authorities))
                .setHeader(Map.of("type", "JWT"))
                .signWith(secrectKey(), SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    @Override
    public Claims verifyToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }

        return Jwts.parser()
                .setSigningKey(secrectKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
