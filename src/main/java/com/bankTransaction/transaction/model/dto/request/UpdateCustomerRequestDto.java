package com.bankTransaction.transaction.model.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
}
