package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bank.iban")
@Data
public class IbanConfig {

    private String countryCode = "RO";
    private String bankCode = "DEMO";
    private int accountNumberLength = 12;

}
