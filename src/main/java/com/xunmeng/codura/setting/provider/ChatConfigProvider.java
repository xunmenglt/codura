package com.xunmeng.codura.setting.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.constants.enums.ComponentType;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 补全配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatConfigProvider implements Provider{
    @UIComponent(label = "最大生成token数",componentType = ComponentType.JTEXTFILED)
    @JsonProperty("maxTokens")
    private Integer numPredict;
    
    @UIComponent(label = "温度（temperature）",componentType = ComponentType.JTEXTFILED)
    @JsonProperty("temperature")
    private Double temperature=0.75;
    
    @UIComponent(label = "最大历史对话数（history message length）",componentType = ComponentType.JTEXTFILED)
    @JsonProperty("historyTurns")
    private Integer messageLength=10;
    
    

    public static ChatConfigProvider defaultProvider() {
        ChatConfigProvider provider = new ChatConfigProvider();
        provider.setNumPredict(512);
        provider.setTemperature(0.75);
        provider.setMessageLength(10);
        return provider;
    }
    
    
    @Override
    public String ID() {
        return "";
    }
}
