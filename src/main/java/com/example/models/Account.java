package com.example.models;

import com.example.exceptions.InsufficientFundsException;
import com.example.utils.AccountType;
import com.example.utils.Currency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNTS", indexes = {@Index(name = "idx_account_number", columnList = "account_number", unique = true), @Index(name = "idx_account_iban", columnList = "iban_code", unique = true)})
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    @Pattern(regexp = "^[0-9]{8,20}$")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Currency currency;

    @Column(name = "iban_code", nullable = false, unique = true, length = 34)
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$")
    private String ibanCode;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    @JsonManagedReference
    private Card card;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

    public void debit(BigDecimal amount) throws InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }


}
