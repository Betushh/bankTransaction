package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    Customer toCustomer(AddCustomerRequestDto addCustomerRequestDto);

    List<CustomerDto> toCustomerDtoList(List<Customer> customerList);


}
