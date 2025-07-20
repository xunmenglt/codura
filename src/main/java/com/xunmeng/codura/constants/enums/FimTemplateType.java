package com.xunmeng.codura.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FimTemplateType implements Type {
    CODELLAME("codellama-fim"),
    CODEQWEN("codeqwen-fim"),
    USECHAT("fillcode-use-chat");

    private final String type;

    FimTemplateType(String type) {
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
    public static FimTemplateType fromString(String value) {
        for (FimTemplateType item : FimTemplateType.values()) {
            if (item.type.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown FimTemplateType: " + value);
    }
}
