package com.example.entities;

import com.example.utils.AccountType;
import com.example.utils.AppConstants;
import com.example.utils.CurrencyType;
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
    private CurrencyType currency;

    @Column(name = "iban_code", nullable = false, unique = true, length = 34)
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$")
    private String ibanCode;

    @Column(name = "current_balance")
    @PositiveOrZero
    private BigDecimal currentBalance;

    @Column(name = "available_balance")
    @PositiveOrZero
    private BigDecimal availableBalance;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit = AppConstants.DEFAULT_AMOUNT_CREDIT_LIMIT;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "daily_transfer_limit")
    @PositiveOrZero
    private BigDecimal dailyTransferLimit = AppConstants.DEFAULT_AMOUNT_DAILY_LIMIT;

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


}
