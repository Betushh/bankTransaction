package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.exception.AlreadyExistException;
import com.bankTransaction.transaction.exception.NotFoundException;
import com.bankTransaction.transaction.jwt.JwtService;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerSyncRequest;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.model.security.Authority;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.AuthorityRepository;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.CustomerService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Override
    public List<CustomerDto> getList() {

        log.info("Fetching all customers");
        var customers = customerRepository.findAll();
        log.info("Fetched {} customers", customers.size());
        return customerMapper.toCustomerDtoList(customers);

    }

    @Override
    public CustomerDto getCustomerByID(Integer id) {

        log.info("Fetching customer with ID: {}", id);
        var customer = customerRepository
                .findById(id).orElseThrow(() -> {
                    log.warn("Customer with ID {} not found", id);
                    return new NotFoundException("Customer not found");
                });
        log.info("Customer found: {}", customer.getEmail());
        return customerMapper.toCustomerDto(customer);

    }

    @Override
    public CustomerDto add(AddCustomerRequestDto addCustomerRequestDto) {

        log.info("Adding new customer with email: {}", addCustomerRequestDto.getEmail());
        var customer = customerMapper.toCustomer(addCustomerRequestDto);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            log.warn("Customer with email {} already exists", customer.getEmail());
            throw new AlreadyExistException("Customer is already exist");
        }

        Customer newCustomer = buildNewCustomer(addCustomerRequestDto);
        Authority authority = authorityRepository.findByAuthority("CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Authority 'CUSTOMER' not found"));
        newCustomer.setAuthorities(Set.of(authority));

        String token = jwtService.issueToken(newCustomer);
        newCustomer.setIssueToken(token);

        var savedCustomer = customerRepository.save(newCustomer);

        log.info("New customer added successfully: {}", savedCustomer.getId());
        return customerMapper.toCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto update(Integer id, UpdateCustomerRequestDto updateCustomerRequestDto) {

        var updatedCustomer = getUpdatedCustomer(id, updateCustomerRequestDto);
        customerRepository.save(updatedCustomer);
        log.info("Updated customer = {}", updatedCustomer);
        return customerMapper.toCustomerDto(updatedCustomer);

    }

    @Override
    public void delete(Integer id) {

        log.info("Deleting customer with ID: {}", id);
        customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tried to delete non-existing customer with ID {}", id);
                    return new NotFoundException("Customer not found");
                });

        customerRepository.deleteById(id);
        log.info("Customer with ID {} deleted successfully", id);

    }

    @Override
    public CustomerDto syncCustomer(CustomerSyncRequest request) {

        log.info("Syncing customer with username: {}", request.getUsername());
        var customer = customerRepository.findByUsername(request.getUsername());
        if (customer.isPresent()) {
            log.info("Customer already exists: {}", request.getUsername());
            return customerMapper.toCustomerDto(customer.get());
        }else{
            throw  new NotFoundException("Customer not found");
        }

    }

    private Customer getUpdatedCustomer(Integer id, UpdateCustomerRequestDto dto) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with ID: " + id));

//        if (customerRepository.existsByEmail(customer.getEmail())) {
//            log.warn("Customer with email {} already exists", customer.getEmail());
//            throw new AlreadyExistException("Customer is already exist");
//        }

        updateIfPresent(dto.getFirstName(), customer::setFirstName);
        updateIfPresent(dto.getLastName(), customer::setLastName);
        updateIfPresent(dto.getEmail(), customer::setEmail);
        updateIfPresent(dto.getPhone(), customer::setPhone);
        updateIfPresent(dto.getDateOfBirth(), customer::setDateOfBirth);
        return customer;

    }

    private <T> void updateIfPresent(T value, Consumer<T> updater) {

        if (value != null) {
            updater.accept(value);
        }

    }

    private Customer buildNewCustomer(AddCustomerRequestDto request) {

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setUsername(request.getUsername());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setIsAccountNonExpired(true);
        customer.setIsEnabled(true);
        customer.setIsAccountNonLocked(true);
        customer.setIsCredentialsNonExpired(true);

        Account defaultAccount = createDefaultAccount(customer);
        customer.setAccounts(List.of(defaultAccount));

        return customer;

    }

    private Account createDefaultAccount(Customer customer) {

        log.trace("Creating default account for customer: {}", customer.getEmail());
        var account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.valueOf(100))
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
        log.debug("Default account created: {}", account.getAccountNumber());
        return account;

    }

    private  String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }

}
