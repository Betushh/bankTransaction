package com.bankTransaction.transaction.model.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddCustomerRequestDto {

    private String firsName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
//password????
}
