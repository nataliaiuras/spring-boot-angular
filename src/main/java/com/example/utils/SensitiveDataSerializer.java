package com.example.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class SensitiveDataSerializer extends JsonSerializer<Object> implements ContextualSerializer {
    private final String maskWith;

    public SensitiveDataSerializer() {
        this.maskWith = "****";
    }

    public SensitiveDataSerializer(String maskWith) {
        this.maskWith = maskWith;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(maskWith);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        MaskSensitive annotation = property.getAnnotation(MaskSensitive.class);
        if (annotation != null) {
            return new SensitiveDataSerializer(annotation.maskWith());
        }
        return this;
    }
}
