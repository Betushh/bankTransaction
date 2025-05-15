package com.bankTransaction.transaction.model.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomerRequestDto {

    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(
            regexp = "^(\\+994|994|0)(10|50|51|55|70|77)[0-9]{7}$",
            message = "Phone number is invalid"
    )
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

}
