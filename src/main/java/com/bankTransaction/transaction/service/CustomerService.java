package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.request.UpdateCustomerRequestDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getList();

    CustomerDto getCustomerByID(Integer id);

    CustomerDto add(AddCustomerRequestDto addCustomerRequestDto);

    CustomerDto update(Integer id, UpdateCustomerRequestDto updateCustomerRequestDto);

    void delete(Integer id);
}
