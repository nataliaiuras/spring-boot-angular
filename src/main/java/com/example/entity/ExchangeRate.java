package com.example.entity;

import com.example.util.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "exchange_rates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "base_currency", nullable = false)
    private CurrencyType baseCurrency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_currency", nullable = false)
    private CurrencyType targetCurrency;
    
    @Column(name = "rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal rate;
    
    @Column(name = "effective_date", nullable = false)
    private Instant effectiveDate;
    
    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;
    
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
    
    @Version
    private Long version;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
}