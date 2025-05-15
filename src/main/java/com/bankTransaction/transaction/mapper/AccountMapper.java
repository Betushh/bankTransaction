package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.account.AccountDto;
import com.bankTransaction.transaction.model.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    List<AccountDto> toAccountDtoList(List<Account> accountList);

}
