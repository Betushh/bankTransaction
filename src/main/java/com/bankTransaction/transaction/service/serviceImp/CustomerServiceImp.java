package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.account.AddAccountRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.CustomerService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountRepository accountRepository;

    @Override
    public List<CustomerDto> getList() {
        return customerMapper.toCustomerDtoList(customerRepository.findAll());
    }

    @Override
    public CustomerDto getCustomerByID(Integer id) {
        return customerMapper.toCustomerDto(customerRepository
                .findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public CustomerDto add(AddCustomerRequestDto addCustomerRequestDto) {

        var customer = customerMapper.toCustomer(addCustomerRequestDto);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("no no no");
        }

        Account defaultAccount = getDefaultAccount(customer);
        customer.setAccounts(List.of(defaultAccount));

        var savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto update(Integer id, UpdateCustomerRequestDto updateCustomerRequestDto) {
        var updatedCustomer = getUpdatedCustomer(id, updateCustomerRequestDto);
        customerRepository.save(updatedCustomer);
        log.info("updated customer = {}", updatedCustomer);
        return customerMapper.toCustomerDto(updatedCustomer);
    }

    @Override
    public void delete(Integer id) {
        customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        customerRepository.deleteById(id);
    }

    private Customer getUpdatedCustomer(Integer id, UpdateCustomerRequestDto updateCustomerRequestDto) {
        var customer = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        Optional.ofNullable(updateCustomerRequestDto.getFirstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(updateCustomerRequestDto.getLastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(updateCustomerRequestDto.getEmail()).ifPresent(customer::setEmail);
        Optional.ofNullable(updateCustomerRequestDto.getPhone()).ifPresent(customer::setPhone);
        Optional.ofNullable(updateCustomerRequestDto.getDateOfBirth()).ifPresent(customer::setDateOfBirth);
        return customer;
    }

    private Account getDefaultAccount(Customer customer) {//create
        return Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.valueOf(100))
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }
}
