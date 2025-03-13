package com.bankTransaction.transaction.mapper;

import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;
import com.bankTransaction.transaction.model.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toTransactionDto(Transaction transaction);
//
//    Transaction toTransaction();
//
//    List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList);
}
