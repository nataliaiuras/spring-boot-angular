package com.example.util.mask;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class SensitiveDataSerializer extends JsonSerializer<Object> implements ContextualSerializer {
    private final MaskSensitive.SensitiveDataType type;
    private final String customMask;
    private final boolean fullMask;

    public SensitiveDataSerializer() {
        this.type = MaskSensitive.SensitiveDataType.GENERIC;
        this.customMask = "";
        this.fullMask = true;
    }

    public SensitiveDataSerializer(MaskSensitive.SensitiveDataType type, String customMask, boolean fullMask) {
        this.type = type;
        this.customMask = customMask;
        this.fullMask = fullMask;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String maskedValue;
        if (!customMask.isEmpty()) {
            maskedValue = customMask;
        } else {
            maskedValue = maskByType(value.toString(), type, fullMask);
        }

        gen.writeString(maskedValue);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        MaskSensitive annotation = property.getAnnotation(MaskSensitive.class);
        if (annotation != null) {
            return new SensitiveDataSerializer(
                    annotation.type(),
                    annotation.customMask(),
                    annotation.fullMask()
            );
        }
        return this;
    }

    private String maskByType(String value, MaskSensitive.SensitiveDataType type, boolean fullMask) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        return switch (type) {
            case PIN -> "****";
            case CVV -> "***";
            case PASSWORD -> "*".repeat(Math.min(value.length(), 8));
            case CARD_NUMBER -> maskCardNumber(value, fullMask);
            case EMAIL -> maskEmail(value, fullMask);
            case PHONE -> maskPhone(value, fullMask);
            case GENERIC -> "****";
        };
    }

    private String maskCardNumber(String cardNumber, boolean fullMask) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****-****-****-****";
        }

        if (fullMask) {
            return "****-****-****-****";
        } else {
            // Show only last 4 digits
            String cleaned = cardNumber.replaceAll("[^0-9]", "");
            if (cleaned.length() >= 4) {
                String lastFour = cleaned.substring(cleaned.length() - 4);
                return "****-****-****-" + lastFour;
            }
            return "****-****-****-****";
        }
    }

    private String maskEmail(String email, boolean fullMask) {
        if (fullMask) {
            return "****@****.***";
        }

        if (email.contains("@")) {
            String[] parts = email.split("@");
            String username = parts[0];
            String domain = parts[1];

            String maskedUsername = username.length() > 2
                    ? username.charAt(0) + "*".repeat(username.length() - 2) + username.charAt(username.length() - 1)
                    : "***";

            String maskedDomain = domain.contains(".") && domain.length() > 4
                    ? "***.***"
                    : "***";

            return maskedUsername + "@" + maskedDomain;
        }
        return "****@****.***";
    }

    private String maskPhone(String phone, boolean fullMask) {
        if (fullMask) {
            return "***-***-****";
        }

        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() >= 4) {
            String lastFour = cleaned.substring(cleaned.length() - 4);
            return "***-***-" + lastFour;
        }
        return "***-***-****";
    }
}

