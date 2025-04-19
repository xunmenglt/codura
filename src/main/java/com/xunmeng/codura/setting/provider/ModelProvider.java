package com.xunmeng.codura.setting.provider;


import com.xunmeng.codura.constants.enums.*;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import com.xunmeng.codura.utils.CodeBundle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModelProvider implements Provider {
    @UIComponent(label = "名称",componentType = ComponentType.JTEXTFILED,order = 0)
    private String label;
    @UIComponent(label = "业务类型",componentType = ComponentType.JRADIOBUTTON,tabs = ProviderType.class,order = 1,disabled = true)
    private ProviderType Type;
    @UIComponent(label = "模型名称",componentType = ComponentType.JCOMBOBOX,tabs = ModeType.class,order = 2)
    private ModeType modelName;
    @UIComponent(label = "服务主机地址",componentType = ComponentType.JTEXTFILED,order = 3)
    private String hostName;
    @UIComponent(label = "openapi类型",componentType = ComponentType.JCOMBOBOX,tabs = OpenApiType.class)
    private OpenApiType openApiType;
    @UIComponent(label = "服务协议",componentType = ComponentType.JRADIOBUTTON,tabs = ProtocolType.class,order = 2)
    private ProtocolType protocol;
    @UIComponent(label = "服务端口",componentType = ComponentType.JTEXTFILED,order = 5)
    private Integer port;
    @UIComponent(label = "服务路径",componentType = ComponentType.JTEXTFILED,order = 6)
    private String Path;
    @UIComponent(label = "API-KEY",componentType = ComponentType.JTEXTFILED,order = 7)
    private String apiKey;

    @Override
    public String ID() {
        return CodeBundle.message("code.configurable.fim.provider.name");
    }
}
