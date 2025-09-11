package com.example.utils;

public enum TransferType {
    INTERNAL,        // Between own accounts
    DOMESTIC,        // To another client in same bank
    WIRE_DOMESTIC,   // Domestic wire transfer
    WIRE_INTERNATIONAL // International wire transfer
}
