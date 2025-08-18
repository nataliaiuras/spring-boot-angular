package com.example.mapers;


import com.example.dtos.TransactionDto;
import com.example.dtos.overview.TransactionOverviewDto;

import com.example.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toTransaction(TransactionDto transactionDto);

    TransactionDto toTransactionDto(Transaction transaction);

    List<TransactionDto> toTransactionDtos(List<Transaction> transactions);

    void updateTransaction(@MappingTarget Transaction target, Transaction source);

    TransactionOverviewDto toTransactionOverviewDto(Transaction transaction);

    List<TransactionOverviewDto> toTransactionOverviewDtos(List<Transaction> transactions);

}
