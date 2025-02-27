package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.CustomerService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<Customer> getList() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerByID(Integer id, String name) {
        return null;
    }

    @Override
    @Transactional
    public Customer add(Customer customer) {
       Customer savedCustomer =  customerRepository.save(customer);

        Account defaultAccount = new Account();
        defaultAccount.setAccountNumber(generateAccountNumber());
        defaultAccount.setBalance(BigDecimal.valueOf(0.0));
        defaultAccount.setAccountStatus(AccountStatus.ACTIVE);
        defaultAccount.setCustomer(savedCustomer);

        accountRepository.save(defaultAccount);
        return savedCustomer;
    }

    @Override
    public Customer update(Integer id, Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("no no no");
        }
        log.info("updated customer = {}",customer);
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Integer id) {

    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}
