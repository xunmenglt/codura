package com.xunmeng.codura.constants.enums;

public enum OpenApiType implements Type {
    OPENAI("openai"),
    VLLM("vllm");

    private String type;
    OpenApiType(String type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
}
