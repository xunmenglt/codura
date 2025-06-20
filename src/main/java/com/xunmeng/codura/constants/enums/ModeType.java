package com.xunmeng.codura.constants.enums;

public enum ModeType implements Type{
    /*开源模型Qwen*/
    QWEN_7B_CHAT("Qwen-7B-Chat"),
    QWEN2_7B_CHAT("Qwen2-7B-Instruct"),
    QWEN2_72B_CHAT("qwen2-72b-instruct"),
    QWEN25_7B_CHAT("qwen2.5-7b-instruct"),
    QWEN25_14B_CHAT("qwen2.5-14b-instruct"),
    QWEN25_32B_CHAT("Qwen2.5-32B-Instruct"),
    QWEN25_72B_CHAT("qwen2.5-72b-instruct"),
    CODEQWEN_CHAT("CodeQwen1.5-7B-Chat"),
    QWEN_CODER_2_5_1P5B_CHAT("qwen2.5-coder-1.5b-instruct"),
    QWEN_CODER_2_5_7B_CHAT("qwen2.5-coder-7b-instruct"),
    QWEN_CODER_2_5_14B_CHAT("qwen2.5-coder-14b-instruct"),
    QWEN_CODER_2_5_32B_CHAT("Qwen2.5-Coder-32B-Instruct"),
    /*开源模型CodeQwen*/
    CODEQWEN_FIM("CodeQwen1.5-7B"),
    QWEN_CODER_2_5_1P5B("Qwen2.5-Coder-1.5B"),
    QWEN_CODER_2_5_7B("Qwen2.5-Coder-7B"),
    QWEN_CODER_2_5_32B("Qwen2.5-Coder-32B"),
    DEEPSEEK_R1("deepseek-reasoner"),
    DEEPSEEK_CHAT("deepseek-chat"),

    /*开源模型LLama*/
    CODELLAMA_7B_CHAT("CodeLlama-7b-Instruct-hf"),
    CODELLAMA_7B_FIM("codellama-7b"),
    CODELLAMA_13B_FIM("CodeLlama-13b-hf"),
    CODELLAMA_34B_FIM("CodeLlama-34b-hf"),
    
    /*闭源模型*/
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_4_O("gpt-4o"),
    GPT_4_O_MINI("gpt-4o-mini");
    

    private final String type;

    ModeType(String type){
        this.type=type;
    }

    @Override
    public Object V() {
        return type;
    }
}
