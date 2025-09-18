package com.example.models.entities;

import com.example.utils.enums.TransactionStatus;
import com.example.utils.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "TRANSACTIONS", indexes = {@Index(name = "idx_transaction_date", columnList = "transaction_date"), @Index(name = "idx_transaction_status", columnList = "status"), @Index(name = "idx_transaction_from_account", columnList = "from_account_id"), @Index(name = "idx_transaction_to_account", columnList = "to_account_id")})
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

    @Column(nullable = false, precision = 19, scale = 4)
    @NotNull
    @Positive
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private TransactionStatus status;

    @Column(name = "transaction_date", nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Instant transactionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_customer_id", nullable = false)
    @NotNull
    private Customer fromCustomer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_account_id", nullable = false)
    @NotNull
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_customer_id")
    private Customer toCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(name = "reference_number", length = 50)
    @Size(max = 50)
    private String referenceNumber;

    @Column(name = "description", length = 255)
    @Size(max = 255)
    private String description;

    @Column(name = "completed_date")
    private Instant completedDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;


}
