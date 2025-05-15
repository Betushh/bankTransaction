package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerSyncRequest;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import java.util.List;

public interface CustomerService {

    List<CustomerDto> getList();

    CustomerDto getCustomerByID(Integer id);

    CustomerDto add(AddCustomerRequestDto addCustomerRequestDto);

    CustomerDto update(Integer id, UpdateCustomerRequestDto updateCustomerRequestDto);

    void delete(Integer id);

    CustomerDto syncCustomer(CustomerSyncRequest request);

}
