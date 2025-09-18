package com.example.models.dtos.transaction;

import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import com.example.utils.enums.TransactionStatus;
import com.example.utils.enums.TransactionType;
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
    private CustomerOverviewDto fromCustomer;
    private AccountOverviewDto fromAccount;
    private CustomerOverviewDto toCustomer;
    private AccountOverviewDto toAccount;
    private String description;

}