package com.example.utils.mask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaskSensitive {
    /**
     * Type of sensitive data for appropriate masking strategy
     */
    SensitiveDataType type() default SensitiveDataType.GENERIC;

    /**
     * Custom mask pattern (overrides type-based masking)
     */
    String customMask() default "";

    /**
     * Whether to mask completely or partially (for card numbers, etc.)
     */
    boolean fullMask() default true;

    enum SensitiveDataType {
        GENERIC,        // Default: ****
        PIN,           // ***
        CVV,           // ***
        PASSWORD,      // *******
        CARD_NUMBER,   // ****-****-****-1234
        EMAIL,         // u***@***.com
        PHONE          // ***-***-1234
    }
}