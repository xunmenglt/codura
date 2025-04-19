package com.xunmeng.codura.constants.enums;

public enum ProtocolType implements Type {
    HTTP("http"),
    HTTPS("https");
    
    private String type;
    ProtocolType(String type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
}
