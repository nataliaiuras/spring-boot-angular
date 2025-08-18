package com.example.dtos;

import com.example.models.Account;
import com.example.models.Customer;
import com.example.utils.Status;
import com.example.utils.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
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
    private Customer fromCustomer;
    private Account fromAccount;
    private Customer toCustomer;
    private Account toAccount;
    private String referenceNumber;
    private String description;
    private Instant lastModifiedDate;
    private Instant completedDate;
    private Long version;

}