package com.xunmeng.codura.setting.provider;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.constants.enums.*;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import com.xunmeng.codura.utils.CodeBundle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelProvider implements Provider {
    @UIComponent(label = "名称",componentType = ComponentType.JTEXTFILED,order = 0)
    @JsonProperty("name")
    private String label;
    @UIComponent(label = "业务类型",componentType = ComponentType.JRADIOBUTTON,tabs = ProviderType.class,order = 1,disabled = true)
    @JsonProperty("businessType")
    private ProviderType Type;
    @UIComponent(label = "模型名称",componentType = ComponentType.JCOMBOBOX,tabs = ModeType.class,order = 2)
    @JsonProperty("modelName")
    private String modelName;
    @UIComponent(label = "openapi类型",componentType = ComponentType.JCOMBOBOX,tabs = OpenApiType.class)
    @JsonProperty("openaiType")
    private OpenApiType openApiType;
    @UIComponent(label = "服务主机地址", componentType = ComponentType.JTEXTFILED, order = 3, disabled = true)
    private String hostName = "127.0.0.1";

    @UIComponent(label = "服务协议", componentType = ComponentType.JRADIOBUTTON, tabs = ProtocolType.class, order = 2, disabled = true)
    private ProtocolType protocol = ProtocolType.HTTP;

    @UIComponent(label = "服务端口", componentType = ComponentType.JTEXTFILED, order = 5, disabled = true)
    private Integer port = 80;

    @UIComponent(label = "服务路径",componentType = ComponentType.JTEXTFILED,order = 6)
    @JsonProperty("path")
    private String Path;
    @UIComponent(label = "API-KEY",componentType = ComponentType.JTEXTFILED,order = 7)
    @JsonProperty("apiKey")
    private String apiKey;



    @Override
    public String ID() {
        return CodeBundle.message("code.configurable.fim.provider.name");
    }
}
