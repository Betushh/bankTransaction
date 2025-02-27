package com.bankTransaction.transaction.service;

import com.bankTransaction.transaction.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getList();

    Customer getCustomerByID(Integer id, String name);

    Customer add(Customer customer);

    Customer update(Integer id, Customer customer);

    void delete(Integer id);
}
