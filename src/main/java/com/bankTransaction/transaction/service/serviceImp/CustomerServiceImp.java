package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.CustomerRepository;
import com.bankTransaction.transaction.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getList() {
        return customerMapper.toStudentDtoList(customerRepository.findAll());
    }

    @Override
    public Customer getCustomerByID(Integer id, String name) {
        return null;
    }

    @Override
    public CustomerDto add(AddCustomerRequestDto addCustomerRequestDto) {
        var customer = customerMapper.toCustomer(addCustomerRequestDto);
        var savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
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
        return "ACC" + UUID.randomUUID();
    }
}
