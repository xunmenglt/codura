package com.xunmeng.codura.constants.enums;

public enum FimTemplateType implements Type{
    CODELLAME("codellama-fim"),
    CODEQWEN("codeqwen-fim"),
    USECHAT("fillcode-use-chat");
    

    private final String type;

    FimTemplateType(String type){
        this.type=type;
    }

    public Object V() {
        return type;
    }
    
}
