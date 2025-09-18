package com.example.config.service;

import com.example.config.IbanConfig;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class IbanGeneratorService {

    private final AccountRepository accountRepository;
    private final IbanConfig ibanConfig;

    public IbanGeneratorService(AccountRepository accountRepository, IbanConfig ibanConfig) {
        this.accountRepository = accountRepository;
        this.ibanConfig = ibanConfig;
    }

     public String generateIban(String accountNumber) {
        String paddedAccountNumber = String.format("%0" + ibanConfig.getAccountNumberLength()
                + "d", Long.parseLong(accountNumber));
        String bban = ibanConfig.getBankCode() + paddedAccountNumber;
        String checkDigits = calculateIbanCheckDigits(ibanConfig.getCountryCode(), bban);
        return ibanConfig.getCountryCode() + checkDigits + bban;
    }

    private String calculateIbanCheckDigits(String countryCode, String bban) {
        // Move country code and initial check digits to end
        String rearranged = bban + countryCode + "00";

        // Replace letters with numbers (A=10, B=11, ..., Z=35)
        StringBuilder numericString = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numericString.append(Character.getNumericValue(c));
            } else {
                numericString.append(c);
            }
        }

        // Calculate mod 97
        int remainder = calculateMod97(numericString.toString());
        int checkDigits = 98 - remainder;

        return String.format("%02d", checkDigits);
    }

    private int calculateMod97(String numericString) {
        // Process in chunks to handle large numbers
        int remainder = 0;
        for (int i = 0; i < numericString.length(); i += 7) {
            int endIndex = Math.min(i + 7, numericString.length());
            String chunk = remainder + numericString.substring(i, endIndex);
            remainder = Integer.parseInt(chunk) % 97;
        }
        return remainder;
    }

    // Utility method to validate IBAN
    public boolean isValidIban(String iban) {
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return false;
        }

        // Remove spaces and convert to uppercase
        iban = iban.replaceAll("\\s", "").toUpperCase();

        // Rearrange: move first 4 characters to end
        String rearranged = iban.substring(4) + iban.substring(0, 4);

        // Replace letters with numbers
        StringBuilder numericString = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numericString.append(Character.getNumericValue(c));
            } else {
                numericString.append(c);
            }
        }

        // Check if mod 97 equals 1
        return calculateMod97(numericString.toString()) == 1;
    }

}

