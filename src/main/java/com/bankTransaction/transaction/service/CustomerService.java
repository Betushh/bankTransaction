package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getList();

    Customer getCustomerByID(Integer id, String name);

    CustomerDto add(AddCustomerRequestDto addCustomerRequestDto);

    Customer update(Integer id, Customer customer);

    void delete(Integer id);
}
