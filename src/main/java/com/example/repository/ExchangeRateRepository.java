
package com.example.repository;

import com.example.models.entities.ExchangeRate;
import com.example.utils.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    
    @Query("""
        SELECT er FROM ExchangeRate er 
        WHERE er.baseCurrency = :baseCurrency 
        AND er.targetCurrency = :targetCurrency 
        AND er.effectiveDate <= :effectiveDate 
        AND er.isActive = true 
        ORDER BY er.effectiveDate DESC 
        LIMIT 1
        """)
    Optional<ExchangeRate> findLatestRateBycurrencies(
        @Param("baseCurrency") CurrencyType baseCurrency,
        @Param("targetCurrency") CurrencyType targetCurrency,
        @Param("effectiveDate") Instant effectiveDate
    );
}