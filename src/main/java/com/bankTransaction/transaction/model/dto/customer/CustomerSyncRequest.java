package com.bankTransaction.transaction.model.dto.customer;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerSyncRequest {

    private String firstName;

    private String lastName;

    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Pattern(
            regexp = "^(\\+994|994|0)(10|50|51|55|70|77)[0-9]{7}$",
            message = "Phone number is invalid"
    )
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

}
