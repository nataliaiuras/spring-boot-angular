package com.example.services.impl;

import com.example.dtos.TransactionDto;
import com.example.exceptions.domain.transaction.TransactionNotFound;
import com.example.mapers.TransactionMapper;
import com.example.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;


    public TransactionDto getTransactionById(Long id) {
        return transactionMapper.toTransactionDto(transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFound(id)));
    }


    public Set<TransactionDto> getAllTransactionsByAccountId(Long accountId) {
        return null;
    }
}
