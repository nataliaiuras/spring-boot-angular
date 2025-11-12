package com.example.mapper;


import com.example.dto.response.transaction.TransactionDetailResponse;
import com.example.dto.response.transaction.TransactionResponse;
import com.example.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toTransactionResponse(Transaction transaction);

    TransactionDetailResponse toTransactionDetailResponse(Transaction savedTransaction);

}
