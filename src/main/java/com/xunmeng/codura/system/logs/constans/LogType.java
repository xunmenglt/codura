package com.xunmeng.codura.system.logs.constans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LogType {
    INFO("info"),
    WARNING("warning"),
    ERROR("error");
    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LogType fromValue(String value) {
        for (LogType type : LogType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
    LogType(String value){
        this.value=value;
    } 
}
