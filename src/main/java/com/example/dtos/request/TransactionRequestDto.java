package com.example.dtos.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class TransactionRequestDto {

    private String transactionType;
    private double amount;
    private String currency;
    private String firstName;
    private String lastName;
    private String bankName;
    private Long bicCode;
    private Long swiftCode;
    private Long accountNumber;
    private Long ibanCode;
}
