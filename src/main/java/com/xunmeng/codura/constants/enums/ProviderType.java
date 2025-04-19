package com.xunmeng.codura.constants.enums;

public enum ProviderType implements Type {
    CHAT("chat"),
    FIM("fim");
    
    private String type;
    ProviderType(String type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
}
