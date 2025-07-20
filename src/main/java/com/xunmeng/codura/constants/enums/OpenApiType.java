package com.xunmeng.codura.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OpenApiType implements Type {
    OPENAI("openai"),
    VLLM("vllm");

    private final String type;

    OpenApiType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public Object V() {
        return type;
    }

    @JsonCreator
    public static OpenApiType fromString(String value) {
        for (OpenApiType item : OpenApiType.values()) {
            if (item.type.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown OpenApiType: " + value);
    }
}
