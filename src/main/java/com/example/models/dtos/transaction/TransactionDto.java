package com.example.models.dtos.transaction;

import com.example.models.dtos.account.AccountOverviewDto;
import com.example.utils.enums.OperationType;
import com.example.utils.enums.TransactionStatus;
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
    private OperationType operationType;
    private TransactionStatus status;
    private Date transactionDate;
    private AccountOverviewDto sourceAccount;
    private AccountOverviewDto destinationAccount;
    private String description;

}