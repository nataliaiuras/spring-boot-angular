package com.example.service;

import com.example.util.enums.CurrencyType;

import java.math.BigDecimal;

public interface CurrencyExchangeService {

    BigDecimal getExchangeRate(CurrencyType fromCurrency, CurrencyType toCurrency);

    BigDecimal convertAmount(BigDecimal amount, CurrencyType fromCurrency, CurrencyType toCurrency);

    BigDecimal calculateConversionFee(BigDecimal amount, CurrencyType fromCurrency, CurrencyType toCurrency);

}