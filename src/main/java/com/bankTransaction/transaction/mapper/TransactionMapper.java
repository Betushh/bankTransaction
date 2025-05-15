package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;
import com.bankTransaction.transaction.model.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toTransactionDto(Transaction transaction);

}
