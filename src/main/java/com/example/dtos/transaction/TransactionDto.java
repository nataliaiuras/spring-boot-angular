package com.example.dtos.transaction;

import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.customer.CustomerRequestDto;
import com.example.utils.TransactionStatus;
import com.example.utils.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class TransactionDto {
    private Long id;
    private String referenceNumber;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private Date transactionDate;
    private CustomerRequestDto fromCustomer;
    private AccountOverviewDto fromAccount;
    private CustomerRequestDto toCustomer;
    private AccountOverviewDto toAccount;
    private String description;

}