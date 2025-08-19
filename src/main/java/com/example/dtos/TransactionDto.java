package com.example.dtos;

import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.utils.Status;
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
    private BigDecimal amount;
    private TransactionType type;
    private Status status;
    private Date transactionDate;
    private CustomerOverviewDto fromCustomer;
    private AccountOverviewDto fromAccount;
    private CustomerOverviewDto toCustomer;
    private AccountOverviewDto toAccount;
    private String referenceNumber;
    private String description;

}