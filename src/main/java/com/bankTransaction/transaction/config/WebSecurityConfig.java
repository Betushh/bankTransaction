package com.bankTransaction.transaction.config;

import com.bankTransaction.transaction.filter.JwtAuthFilter;
import com.bankTransaction.transaction.filter.JwtAuthFilterConfigurerAdapter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilterConfigurerAdapter jwtAuthFilterConfigurerAdapter;
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs*/**",
            "/customers/sync",
            "/customers/add",
            "/auth/login"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/customers/add",
            "/customers/{id}",
            "/customers/delete/{id}",
            "/accounts",
            "/accounts/activeAccounts/{id}",
            "/accounts/{customerId}",
            "/accounts/increase/{accountNumber}/balance",
            "/accounts/decrease/{accountNumber}/balance",
            "/accounts/delete/{id}",
            "/transactions/refund/{transactionId}/{transactionStatus}"

    };

    private static final String[] CUSTOMER_ENDPOINTS = {
            "/customers",
            "/customers/update/{id}",
            "/accounts/{customerId}",
            "/transactions/topUp/{transactionId}/{transactionStatus}",
            "/transactions/purchase/{transactionId}/{transactionStatus}"
    };


    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.apply(jwtAuthFilterConfigurerAdapter);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
                    auth.requestMatchers(ADMIN_ENDPOINTS).hasAnyAuthority("ADMIN");
                    auth.requestMatchers(CUSTOMER_ENDPOINTS).hasAnyAuthority("CUSTOMER");
                    auth.requestMatchers("auth/logout").hasAnyAuthority("ADMIN", "CUSTOMER");
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized access. Please login.\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden - Access is denied\"}");
                        }))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


}
