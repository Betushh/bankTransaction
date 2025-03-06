package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    Customer toCustomer(AddCustomerRequestDto addCustomerRequestDto);

    List<CustomerDto> toStudentDtoList(List<Customer> customerList);


}
