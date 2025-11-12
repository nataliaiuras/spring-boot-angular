package com.example.entity;

import com.example.util.enums.CardStatus;
import com.example.util.enums.CurrencyType;
import com.example.util.mask.MaskSensitive;
import com.example.util.mask.SensitiveDataSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARDS", indexes = {@Index(name = "idx_card_number", columnList = "card_number", unique = true)})
@EntityListeners(AuditingEntityListener.class)
public class Card implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, unique = true, length = 19)
    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CARD_NUMBER, fullMask = false)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String cardNumber;

    @Column(name = "card_holder", nullable = false, length = 100)
    private String cardHolder;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "cvv_code", nullable = false)
    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CVV)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private int cvvCode;

    @Column(name = "pin", nullable = false)
    @MaskSensitive(type = MaskSensitive.SensitiveDataType.PIN)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private int pin;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;


}
