package com.bankTransaction.transaction.model.dto.account;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Customer;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class AddAccountRequestDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
//    private String phone;//--
}
