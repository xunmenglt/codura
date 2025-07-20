package com.xunmeng.codura.setting.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xunmeng.codura.constants.Constants;
import com.xunmeng.codura.constants.enums.*;
import com.xunmeng.codura.setting.component.annotation.UIComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FimModelProvider extends ModelProvider{
    @UIComponent(label = "填充类型",componentType = ComponentType.JCOMBOBOX,tabs = FimTemplateType.class,order = 2)
    @JsonProperty("fillType")
    private FimTemplateType fimTemplateType;

    public static FimModelProvider defaultProvider() {
        FimModelProvider provider = new FimModelProvider();
        provider.setLabel("代码补全模型-CodeQwen");
        provider.setType(ProviderType.FIM);
        provider.setOpenApiType(OpenApiType.OPENAI);
        provider.setModelName("gpt-3.5-turbo");
        provider.setHostName(Constants.HOST);
        provider.setProtocol(ProtocolType.HTTP);
        provider.setPort(Constants.PORT);
        provider.setPath(Constants.FIM_SERVER_PATH);
        provider.setFimTemplateType(FimTemplateType.USECHAT);
        return provider;
    }

    @Override
    public String ID() {
        return "";
    }
}
