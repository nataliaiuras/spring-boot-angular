package com.example.mapers;


import com.example.dtos.transaction.TransactionDto;
import com.example.dtos.transaction.TransactionOverviewDto;
import com.example.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Transaction toTransaction(TransactionDto transactionDto);

    TransactionDto toTransactionDto(Transaction transaction);

    Set<TransactionDto> toTransactionDtos(Set<Transaction> transactions);

    TransactionOverviewDto toTransactionOverviewDto(Transaction transaction);
}
