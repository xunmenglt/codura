package com.xunmeng.codura.service.constant;

public enum ProviderType {
    CHAT(0), FIM(1);

    private final int value;
    ProviderType(int value) { this.value = value; }
    public int getValue() { return value; }
    public static ProviderType from(int value) {
        for (ProviderType type : values()) {
            if (type.value == value) return type;
        }
        throw new IllegalArgumentException("未知业务类型: " + value);
    }
}
