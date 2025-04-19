package com.xunmeng.codura.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtils {
    public static String convert2Json(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        return json;
    }
    public static <T> T convert2Object(String json,Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        T value = mapper.readValue(json, clazz);
        return value;
    }

    public static <T> T tree2Object(JsonNode jsonNode, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T value = mapper.treeToValue(jsonNode, clazz);
        return value;
    }

    public static <T> T convert2Object(String jsonStr, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        T value = null;
        try {
            value = mapper.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
}
