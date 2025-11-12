package com.example.service.impl;

import com.example.entity.ExchangeRate;
import com.example.repository.ExchangeRateRepository;
import com.example.service.CurrencyExchangeService;
import com.example.util.enums.CurrencyType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Service
@AllArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    
    private final ExchangeRateRepository exchangeRateRepository;
    
    // Conversion fee percentage (e.g., 0.5%)
    private static final BigDecimal CONVERSION_FEE_RATE = new BigDecimal("0.005");
    
    @Override
    public BigDecimal getExchangeRate(CurrencyType fromCurrency, CurrencyType toCurrency) {
        if (fromCurrency == toCurrency) {
            return BigDecimal.ONE;
        }
        
        return exchangeRateRepository
            .findLatestRateBycurrencies(fromCurrency, toCurrency, Instant.now())
            .map(ExchangeRate::getRate)
            .orElseThrow(() -> new RuntimeException(
                "Exchange rate not found for " + fromCurrency + " to " + toCurrency));
    }
    
    @Override
    public BigDecimal convertAmount(BigDecimal amount, CurrencyType fromCurrency, CurrencyType toCurrency) {
        if (fromCurrency == toCurrency) {
            return amount;
        }
        
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateConversionFee(BigDecimal amount, CurrencyType fromCurrency, CurrencyType toCurrency) {
        if (fromCurrency == toCurrency) {
            return BigDecimal.ZERO;
        }
        
        return amount.multiply(CONVERSION_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}