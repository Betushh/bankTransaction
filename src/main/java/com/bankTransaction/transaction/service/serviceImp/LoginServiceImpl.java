//package com.bankTransaction.transaction.service.serviceImp;
//
//import com.bankTransaction.transaction.exception.NotFoundException;
//import com.bankTransaction.transaction.jwt.JwtService;
//import com.bankTransaction.transaction.model.dto.login.LoginRequestDto;
//import com.bankTransaction.transaction.model.dto.login.LoginResponseDto;
//import com.bankTransaction.transaction.repository.CustomerRepository;
//import com.bankTransaction.transaction.service.LoginService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class LoginServiceImpl implements LoginService {
//
//    private final AuthenticationManager authenticationManager;
//    private final CustomerRepository customerRepository;
//    private final JwtService jwtService;
//
//
//    @Override
//    public LoginResponseDto login(LoginRequestDto loginRequest) {
//
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(), loginRequest.getPassword()));
//
//       var customer = customerRepository.findByUsername(loginRequest.getUsername())
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
////        customer.setIssueToken(null);
////        String token = jwtService.issueToken(customer);
////       customer.setIssueToken(token);
//        customerRepository.save(customer);
//
//        log.info("User {} logged in, new token issued.", customer.getUsername());
//        return new LoginResponseDto(token);
//
//    }
//
//    @Override
//    public void logout(String username) {
//
//        var customer = customerRepository.findByUsername(username)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
////        customer.setIssueToken(null);
//        customerRepository.save(customer);
//
//        log.info("User {} logged out, token invalidated.", username);
//
//    }
//
//}