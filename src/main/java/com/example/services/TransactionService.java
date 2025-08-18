package com.example.services;

import com.example.dtos.AccountDto;
import com.example.dtos.TransactionDto;
import com.example.dtos.request.TransactionRequestDto;
import com.example.exceptions.AppException;
import com.example.mapers.AccountMapper;
import com.example.mapers.TransactionMapper;
import com.example.repository.TransactionRepository;
import com.example.utils.Status;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;

    public TransactionService(AccountService accountService, CustomerService customerService, TransactionMapper transactionMapper, TransactionRepository transactionRepository, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.accountMapper = accountMapper;
    }

    public List<TransactionDto> allTransactions() {
        return transactionMapper.toTransactionDtos(transactionRepository.findAll());
    }

    public TransactionDto getTransactionById(Long id) {
        return transactionMapper.toTransactionDto(transactionRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public TransactionDto createTransaction(@Valid TransactionDto current) {
        return transactionMapper.toTransactionDto(transactionRepository.save(transactionMapper.toTransaction(current)));
    }

    public TransactionDto transferBetweenOwnBank(AccountDto fromAccount, AccountDto toAccount, BigDecimal amount) {

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new AppException("Not enough balance", HttpStatus.BAD_REQUEST);
        }

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new AppException("Different currencies", HttpStatus.BAD_REQUEST);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountService.patchAccount(fromAccount.getId(), fromAccount);

       // toAccount.setBalance(toAccount.getBalance(). amount);
        accountService.patchAccount(toAccount.getId(), toAccount);

        TransactionDto current = new TransactionDto();

        current.setAmount(amount);

        current.setFromCustomer(current.getFromCustomer());
        current.setFromAccount(current.getFromAccount());

        current.setToCustomer(current.getToCustomer());
        current.setToAccount(current.getToAccount());

        current.setStatus(Status.APPROVED);
        current.setTransactionDate(new Date());

        return createTransaction(current);

    }

    public List<TransactionDto> getAllTransactionsByAccountId(Long accountId) {
        return allTransactions().stream()
                .filter(current -> current.getFromAccount().getId()
                        .equals(accountId)).toList();
    }

    public TransactionDto transfer(Long accountId, @Valid TransactionRequestDto transactionRequestDto) {



        AccountDto fromAccount = accountService.getAccount(accountId);
    /*    Long clientId = fromAccount.get
        ClientDto fromClient = clientService.getClient();
        AccountDto toAccount = transactionRequestDto.getAccountNumber();



        double amount = transactionRequestDto.getAmount();
        if (fromAccount.getBalance() < amount) {
            throw new AppException("Not enough balance", HttpStatus.BAD_REQUEST);
        }
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new AppException("Different currencies", HttpStatus.BAD_REQUEST);
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountService.patchAccount(fromAccount.getId(), fromAccount);

        toAccount.setBalance(toAccount.getBalance() + amount);
        accountService.patchAccount(toAccount.getId(), toAccount);

        TransactionDto current = new TransactionDto();

        current.setAmount(amount);

        current.setFromClient(current.getFromClient());
        current.setFromAccount(current.getFromAccount());

        current.setToClient(current.getToClient());
        current.setToAccount(current.getToAccount());

        current.setStatus(Status.APPROVED);
        current.setTransactionDate(new Date());

        return createTransaction(current);*/
        return null;

    }
}
