package com.example.utils.encription;

import com.fasterxml.jackson.databind.util.StdConverter;

public class EncryptedIntegerJsonConverter  {

    private EncryptedIntegerJsonConverter() {}

    public static class Serializer extends StdConverter<Integer, Integer> {
        @Override
        public Integer convert(Integer value) {
            return value;
        }
    }

    public static class Deserializer extends StdConverter<Integer, Integer> {
        @Override
        public Integer convert(Integer value) {
            return value;
        }
    }

}
