package com.example.entity;

import com.example.util.enums.CurrencyType;
import com.example.util.enums.OperationType;
import com.example.util.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "TRANSACTIONS", indexes = {@Index(name = "idx_transaction_date", columnList = "transaction_date"),
        @Index(name = "idx_transaction_status", columnList = "status"), @Index(name = "idx_transaction_source_account",
        columnList = "source_account_id"), @Index(name = "idx_transaction_destination_account",
        columnList = "destination_account_id")})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    @NotNull
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
    @NotNull
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

    @Version
    private Long version;


}
