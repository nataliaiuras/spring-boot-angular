package com.example.utils.mapers;


import com.example.models.dtos.transaction.TransactionDto;
import com.example.models.dtos.transaction.TransactionOverviewDto;
import com.example.models.dtos.transaction.TransferResponseDto;
import com.example.models.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    Transaction toTransaction(TransactionDto transactionDto);

    TransactionDto toTransactionDto(Transaction transaction);

    Set<TransactionDto> toTransactionDtos(Set<Transaction> transactions);

    TransactionOverviewDto toTransactionOverviewDto(Transaction transaction);

    TransferResponseDto toTransferResponseDto(Transaction savedTransaction);
}
