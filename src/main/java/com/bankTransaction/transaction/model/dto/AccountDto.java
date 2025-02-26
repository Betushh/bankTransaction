package com.bankTransaction.transaction.model.dto;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.model.entity.Card;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDto {

    private Long id;
    private Long accountNumber;
    private BigDecimal balance;
    private AccountStatus accountStatus;
 //   private List<Card> cards;
}
