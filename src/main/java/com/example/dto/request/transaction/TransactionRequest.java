package com.example.dto.request.transaction;

import com.example.entity.Account;
import com.example.util.enums.CurrencyType;
import com.example.util.enums.OperationType;
import com.example.util.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@Getter
public class TransactionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 100)
    @NotNull
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(name = "source_amount", nullable = false, precision = 19, scale = 4)
    @Positive
    private BigDecimal sourceAmount;

    @Column(name = "source_currency", length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyType sourceCurrency;

    @Column(name = "destination_currency", length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyType destinationCurrency;

    @Column(name = "conversion_rate", precision = 19, scale = 8)
    private BigDecimal conversionRate;

    @Column(name = "conversion_fee", precision = 19, scale = 4)
    private BigDecimal conversionFee;

    @Column(name = "converted_amount", precision = 19, scale = 4)
    private BigDecimal convertedAmount;

    @Column(name = "operation_fee", precision = 19, scale = 4)
    private BigDecimal operationFee;

    @Column(name = "destination_amount", nullable = false, precision = 19, scale = 4)
    @Positive
    private BigDecimal destinationAmount;

    @Column(name = "reference_number", length = 50)
    @Size(max = 50)
    private String referenceNumber;

    @Column(name = "description", length = 255)
    @Size(max = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private TransactionStatus status;

    @Column(name = "transaction_date", nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Instant transactionDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    private Long version;
}
