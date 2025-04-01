package com.bankTransaction.transaction.model.dto.customer;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddCustomerRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
//password????
}
