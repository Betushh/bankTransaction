package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.request.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.CustomerService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Builder
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getList() {
        return customerMapper.toStudentDtoList(customerRepository.findAll());
    }

    @Override
    public CustomerDto getCustomerByID(Integer id) {
        return customerMapper.toCustomerDto(customerRepository
                .findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public CustomerDto add(AddCustomerRequestDto addCustomerRequestDto) {
        var customer = customerMapper.toCustomer(addCustomerRequestDto);
        if (customerRepository.existsById(customer.getId())) {
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

    private Customer getUpdatedCustomer(Integer id, UpdateCustomerRequestDto customer) {
        var optionalCustomer = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        String firstname = optionalCustomer.getFirstName().equalsIgnoreCase(customer.getFirstName())
                ? optionalCustomer.getFirstName() : customer.getFirstName();
        String lastname = optionalCustomer.getLastName().equalsIgnoreCase(customer.getLastName()) ?
                optionalCustomer.getLastName() : customer.getLastName();
        String email = optionalCustomer.getEmail().equalsIgnoreCase(customer.getEmail()) ?
                optionalCustomer.getEmail() : customer.getEmail();
        String phone = optionalCustomer.getPhone().equalsIgnoreCase(customer.getPhone()) ?
                optionalCustomer.getPhone() : customer.getPhone();
        LocalDate dateOfBirth = optionalCustomer.getDateOfBirth().equals(customer.getDateOfBirth()) ?
                optionalCustomer.getDateOfBirth() : customer.getDateOfBirth();
        optionalCustomer.setFirstName(firstname);
        optionalCustomer.setLastName(lastname);
        optionalCustomer.setEmail(email);
        optionalCustomer.setPhone(phone);
        optionalCustomer.setDateOfBirth(dateOfBirth);
        return optionalCustomer;
    }

    private Account getDefaultAccount(Customer customer){
        return Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(20);
    }
}
