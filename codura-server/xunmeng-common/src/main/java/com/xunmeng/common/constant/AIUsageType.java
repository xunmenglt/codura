package com.xunmeng.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum AIUsageType {
    CODE_COMPLETION("Code_completion"),
    CODE_QA("Code_question_and_answer"),
    TEST_CASE_WRITING("Test_case_writing"),
    VARIABLE_TYPE_DECLARATION("Variable_type_declaration"),
    CODE_EXPLANATION("Code_content_explanation"),
    DOCUMENTATION_WRITING("Development_documentation_writing"),
    CODE_REFACTORING("Code_content_refactoring"),
    QUICK_CODE_INSERTION("Quick_code_insertion");

    private final String value;

    AIUsageType(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AIUsageType fromValue(String value) {
        for (AIUsageType type : AIUsageType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
