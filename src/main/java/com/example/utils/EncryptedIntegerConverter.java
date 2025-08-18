package com.example.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;

@Converter
@Component
public class EncryptedIntegerConverter implements AttributeConverter<Integer, String> {

    private static final String ALGORITHM = "AES";
    private static final byte[] ENCRYPTION_KEY = "YourSecretKey123".getBytes();
    private final SecretKey secretKey;

    public EncryptedIntegerConverter() {
        this.secretKey = new SecretKeySpec(ENCRYPTION_KEY, ALGORITHM);
    }

    @Override
    public String convertToDatabaseColumn(Integer attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(attribute);
            byte[] encryptedValue = cipher.doFinal(buffer.array());
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting value", e);
        }
    }

    @Override
    public Integer convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return ByteBuffer.wrap(decryptedValue).getInt();
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting value", e);
        }
    }
}
