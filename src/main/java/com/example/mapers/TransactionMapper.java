package com.example.mapers;


import com.example.dtos.TransactionDto;
import com.example.dtos.overview.TransactionOverviewDto;
import com.example.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Transaction toTransaction(TransactionDto transactionDto);

    TransactionDto toTransactionDto(Transaction transaction);

    Set<TransactionDto> toTransactionDtos(Set<Transaction> transactions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateTransaction(@MappingTarget Transaction target, Transaction source);

    TransactionOverviewDto toTransactionOverviewDto(Transaction transaction);

    Set<TransactionOverviewDto> toTransactionOverviewDtos(Set<Transaction> transactions);

}
