package com.example.models;

import com.example.exceptions.InsufficientFundsException;
import com.example.utils.Status;
import com.example.utils.TransactionType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Table(name = "TRANSACTIONS", indexes = {
        @Index(name = "idx_transaction_date", columnList = "transaction_date"),
        @Index(name = "idx_transaction_status", columnList = "status"),
        @Index(name = "idx_transaction_from_account", columnList = "from_account_id"),
        @Index(name = "idx_transaction_to_account", columnList = "to_account_id")
})
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
    private Status status;

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

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "completed_date")
    private Instant completedDate;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = Instant.now();
        }
        if (status == null) {
            status = Status.PENDING;
        }
        generateReferenceNumber();
    }

    @PreUpdate
    protected void onUpdate() {
        if (Status.COMPLETED.equals(status) && completedDate == null) {
            completedDate = Instant.now();
        }
    }

    private void generateReferenceNumber() {
        if (referenceNumber == null) {
            referenceNumber = String.format("TXN%s%d",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                    ThreadLocalRandom.current().nextInt(1000, 9999));
        }
    }

    public void complete() throws InsufficientFundsException {
        validateTransaction();
        this.status = Status.COMPLETED;
        this.completedDate = Instant.now();
        processTransaction();
    }

    public void fail(String reason) {
        this.status = Status.FAILED;
        this.description = reason;
    }

    private void validateTransaction() throws InsufficientFundsException {
        if (Status.COMPLETED.equals(this.status)) {
            throw new IllegalStateException("Transaction already completed");
        }
        if (Status.FAILED.equals(this.status)) {
            throw new IllegalStateException("Cannot complete failed transaction");
        }
        if (fromAccount == null || toAccount == null) {
            throw new IllegalStateException("Invalid transaction accounts");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }
        validateAccountBalance();
    }

    private void validateAccountBalance() throws InsufficientFundsException {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source account");
        }
    }

    @Transactional
    protected void processTransaction() throws InsufficientFundsException {
        fromAccount.debit(amount);
        toAccount.credit(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @ToString.Include(name = "amount")
    private String maskAmount() {
        return amount != null ? amount.toString() : "null";
    }

    @Data
    @Builder
    public static class TransactionSummary {
        private final Long id;
        private final BigDecimal amount;
        private final TransactionType type;
        private final Status status;
        private final String referenceNumber;
        private final Instant transactionDate;
    }
}
