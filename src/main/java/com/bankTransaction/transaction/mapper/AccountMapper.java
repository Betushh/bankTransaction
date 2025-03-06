package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.AccountDto;
import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.account.AddAccountRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    Account toAccount(AddAccountRequestDto addAccountRequestDto);


    List<AccountDto> toAccountDtoList(List<Account> accountList);
}
